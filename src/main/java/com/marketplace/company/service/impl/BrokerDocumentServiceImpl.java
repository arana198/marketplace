package com.marketplace.company.service.impl;

import com.marketplace.company.domain.BrokerDocumentBO;
import com.marketplace.company.dto.BrokerDocumentResponse;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.exception.BrokerDocumentNotFoundException;
import com.marketplace.company.exception.BrokerNotFoundException;
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.BrokerDocumentService;
import com.marketplace.company.service.BrokerService;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileRequest.FileType;
import com.marketplace.storage.dto.FileResponse;
import com.marketplace.storage.service.FileStoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
class BrokerDocumentServiceImpl implements BrokerDocumentService {

  private final BrokerDocumentRepository brokerDocumentRepository;
  private final BrokerService brokerService;
  private final FileStoreService fileStoreService;
  private final CompanyFileRequestConverter fileRequestConverter;
  private final BrokerDocumentResponseConverter brokerDocumentResponseConverter;
  private final CompanyPublishService publishService;

    /* TODO:
        Billing no longer valid -> isActive flag is false -> Broker Status Pending
        Publish event -> documentVerified, billingSpecified, isActive, fcaNumberVerified
    */

  @Override
  public List<BrokerDocumentResponse> getBrokerDocuments(final String userId,
                                                         final String companyId,
                                                         final String brokerProfileId)
      throws BrokerNotFoundException {

    LOGGER.info("Getting broker [ {} ] documents", brokerProfileId);
    this.verifyBrokerProfile(userId, companyId, brokerProfileId);

    return brokerDocumentRepository.findByBrokerProfileId(brokerProfileId)
        .parallelStream()
        .map(brokerDocumentResponseConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public void addDocument(final String userId,
                          final String companyId,
                          final String brokerProfileId,
                          final MultipartFile multipartFile)
      throws BrokerNotFoundException, IOException {

    LOGGER.info("Add broker [ {} ] document", brokerProfileId);

    final BrokerProfileResponse brokerProfile = brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerProfileId)
        .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
        .filter(ce -> ce.getUserId().equalsIgnoreCase(userId))
        .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerProfileId));

    final FileRequest fileRequest = fileRequestConverter.getFileRequest(
        brokerProfile.getBrokerProfileId(),
        multipartFile.getOriginalFilename(),
        "Broker Certificate",
        FileType.BROKER_CERTIFICATE);

    final FileResponse fileResponse = fileStoreService.store(fileRequest, multipartFile);

    BrokerDocumentBO brokerDocumentBO = this.getBrokerDocumentBO(brokerProfile, fileResponse);
    brokerDocumentRepository.save(brokerDocumentBO);
    publishService.sendMessage(CompanyPublishAction.BROKER_SUBMITTED_CERTIFICATION, brokerDocumentResponseConverter.convert(brokerDocumentBO));
  }

  @Override
  public void verifyDocument(final String companyId,
                             final String brokerProfileId,
                             final String brokerDocumentId)
      throws BrokerNotFoundException, BrokerDocumentNotFoundException {

    LOGGER.info("Verifying broker document [ {} ]", brokerProfileId);

    brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerProfileId)
        .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
        .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerProfileId));

    BrokerDocumentBO brokerDocumentBO = brokerDocumentRepository.findById(brokerDocumentId)
        .orElseThrow(() -> new BrokerDocumentNotFoundException(brokerDocumentId));

    if (!brokerDocumentBO.isVerified()) {
      brokerDocumentBO.setVerified(true);
      brokerDocumentRepository.save(brokerDocumentBO);
      publishService.sendMessage(CompanyPublishAction.BROKER_CERTIFICATE_VERIFIED, brokerDocumentResponseConverter.convert(brokerDocumentBO));
    }
  }

  private BrokerDocumentBO getBrokerDocumentBO(final BrokerProfileResponse brokerProfile, final FileResponse file) {
    return new BrokerDocumentBO()
        .setBrokerProfileId(brokerProfile.getBrokerProfileId())
        .setFileId(file.getFileId())
        .setVerified(false);
  }

  private void verifyBrokerProfile(final String userId,
                                   final String companyId,
                                   final String brokerProfileId)
      throws BrokerNotFoundException {

    brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerProfileId)
        .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
        .filter(ce -> ce.getUserId().equalsIgnoreCase(userId))
        .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerProfileId));
  }
}
