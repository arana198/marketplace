package com.marketplace.storage.queue.consume;

import com.google.gson.Gson;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.storage.dto.BucketPermissionResponse;
import com.marketplace.storage.service.BucketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
class StorageMessageHandlerImpl implements MessageHandler {

  private final Gson gson;
  private final BucketService bucketService;

  @Override
  public void handleMessage(final Message<?> message) throws MessagingException {
    LOGGER.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
    if (message.getPayload() instanceof String) {
      StorageConsumeAction.getActionFromString(message.getHeaders().get("action").toString())
          .ifPresent(consumedAction -> {
            final String payload = (String) message.getPayload();

            switch (consumedAction) {
              case COMPANY_BROKERS:
                BucketPermissionResponse bucketPermissions = gson.fromJson(payload, BucketPermissionResponse.class);
                bucketService.addPermissions(bucketPermissions);
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
