package com.marketplace.company.queue.consume;

import com.google.gson.Gson;
import com.marketplace.company.dto.BrokerDocumentResponse;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.BrokerService;
import com.marketplace.company.service.BrokerValidatorService;
import com.marketplace.storage.dto.BucketPermissionResponse;
import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.BucketResponse;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.dto.UserStatusRequest.UserStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Slf4j
@Service
class CompanyMessageHandlerImpl implements MessageHandler {

  private final Gson gson;
  private final BrokerService brokerService;
  private final BrokerValidatorService brokerValidatorService;
  private final CompanyPublishService publishService;

  @Override
  public void handleMessage(final Message<?> message) throws MessagingException {
    LOGGER.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
    if (message.getPayload() instanceof String) {
      CompanyConsumeAction.getActionFromString(message.getHeaders().get("action").toString())
          .ifPresent(consumedAction -> {
            final String payload = (String) message.getPayload();

            switch (consumedAction) {
              case BUCKET_CREATED:

                BucketResponse bucketResponse = gson.fromJson(payload, BucketResponse.class);
                if (BucketRequest.BucketType.COMPANY.getValue().equalsIgnoreCase(bucketResponse.getType())) {

                  int index = 0;
                  Page<BrokerProfileResponse> brokerProfiles;

                  do {
                    brokerProfiles = brokerService.findByCompanyId(bucketResponse.getBucketId(), new PageRequest(index, 20));
                    BucketPermissionResponse bucketPermissionResponse = this.createBucketPermissionResponse(bucketResponse.getBucketId(), brokerProfiles.getContent());
                    publishService.sendMessage(CompanyPublishAction.COMPANY_BROKERS, bucketPermissionResponse);
                    index++;
                  } while (brokerProfiles.hasNext());

                } else if (BucketRequest.BucketType.BROKER.getValue().equalsIgnoreCase(bucketResponse.getType())) {

                  Optional<BrokerProfileResponse> profileResponse = brokerService.findByBrokerProfileId(bucketResponse.getBucketId());
                  profileResponse
                      .map(BrokerProfileResponse::getCompanyId)
                      .map(brokerService::findByCompanyAdmin)
                      .ifPresent(admins -> {
                        admins.add(profileResponse.get());
                        BucketPermissionResponse bucketPermissionResponse = this.createBucketPermissionResponse(bucketResponse.getBucketId(), admins);
                        publishService.sendMessage(CompanyPublishAction.COMPANY_BROKERS, bucketPermissionResponse);
                      });

                } else if (BucketRequest.BucketType.APPLICATION.getValue().equalsIgnoreCase(bucketResponse.getType())) {
                  //TODO mortgage documents - client + broker + company admin
                }

                break;
              case USER_STATUS_UPDATED:

                final UserResponse userResponse = gson.fromJson(payload, UserResponse.class);
                userResponse.getUserRoles()
                    .parallelStream()
                    .filter(userRole -> userRole.getRole()
                        .equalsIgnoreCase(UserRole.ROLE_BROKER.getValue()) || userRole.getRole().equalsIgnoreCase(UserRole.ROLE_COMPANY_ADMIN.getValue()))
                    .forEach(userRole -> {
                      if (userRole.getUserStatus().equalsIgnoreCase(UserStatus.ACTIVE.getValue())) {
                        brokerService.updateBrokerActiveFlag(userResponse.getUserId(), true);
                      } else {
                        brokerService.updateBrokerActiveFlag(userResponse.getUserId(), false);
                      }
                    });
                break;
              case BROKER_CERTIFICATE_VERIFIED:

                final BrokerDocumentResponse brokerDocumentResponse = gson.fromJson(payload, BrokerDocumentResponse.class);
                brokerService.findByBrokerProfileId(brokerDocumentResponse.getBrokerProfileId())
                    .ifPresent(broker -> brokerValidatorService.certificationVerified(broker.getCompanyId(), broker.getBrokerProfileId()));
                break;
              case COMPANY_ACTIVATED:
              case COMPANY_INACTIVATED:

                final boolean isActiveFlag = consumedAction == CompanyConsumeAction.COMPANY_ACTIVATED ? true : false;
                final CompanyResponse companyResponse = gson.fromJson(payload, CompanyResponse.class);
                brokerService.updateBrokerActiveFlagByCompany(companyResponse.getCompanyId(), isActiveFlag);
                break;
              default:
                break;
            }
          });
    }
  }

  private BucketPermissionResponse createBucketPermissionResponse(final String bucketId, final List<BrokerProfileResponse> brokerProfileResponse) {
    return new BucketPermissionResponse(bucketId, brokerProfileResponse);
  }
}
