package com.marketplace.company.queue.consume;

import com.google.gson.Gson;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.BrokerService;
import com.marketplace.storage.dto.BucketPermissionResponse;
import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.BucketResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Slf4j
@Service
class CompanyMessageHandlerImpl implements MessageHandler {

    private final Gson gson;
    private final BrokerService brokerService;
    private final CompanyPublishService publishService;

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        log.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
        if (message.getPayload() instanceof String) {
            final CompanyConsumeAction consumedAction = CompanyConsumeAction.getActionFromString(message.getHeaders().get("action").toString());
            final String payload = (String) message.getPayload();

            switch (consumedAction) {
                case BUCKET_CREATED:
                    BucketResponse bucketResponse = gson.fromJson(payload, BucketResponse.class);
                    if (BucketRequest.BucketType.COMPANY.getValue().equalsIgnoreCase(bucketResponse.getType())) {

                        int index = 0;
                        Page<BrokerProfileResponse> brokerProfiles;

                        do {
                            brokerProfiles = brokerService.findByCompanyId(bucketResponse.getBucketId(), new PageRequest(index, 20));
                            publishService.sendMessage(CompanyPublishAction.COMPANY_BROKERS, this.createBucketPermissionResponse(bucketResponse.getBucketId(), brokerProfiles.getContent()));
                            index++;
                        } while (brokerProfiles.hasNext());

                    } else if (BucketRequest.BucketType.USER.getValue().equalsIgnoreCase(bucketResponse.getType())) {

                        brokerService.findByUserId(bucketResponse.getBucketId())
                                .map(BrokerProfileResponse::getCompanyId)
                                .map(brokerService::findByCompanyAdmin)
                                .map(admins -> this.createBucketPermissionResponse(bucketResponse.getBucketId(), admins))
                                .ifPresent(admins -> publishService.sendMessage(CompanyPublishAction.COMPANY_BROKERS, admins));

                    } else if (BucketRequest.BucketType.APPLICATION.getValue().equalsIgnoreCase(bucketResponse.getType())) {
                        //TODO mortgage documents - client + broker + company admin
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private BucketPermissionResponse createBucketPermissionResponse(final String bucketId, final List<BrokerProfileResponse> brokerProfileResponse) {
        return new BucketPermissionResponse(bucketId, brokerProfileResponse);
    }
}
