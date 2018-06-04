package com.marketplace.company.queue.publish;

import com.marketplace.company.queue.publish.domain.CompanyPublishAction;

public interface CompanyPublishService {
  void sendMessage(CompanyPublishAction action, Object object);
}