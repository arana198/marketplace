package com.marketplace.company.service;

import com.marketplace.company.dto.BrokerDocumentResponse;
import com.marketplace.company.exception.BrokerDocumentNotFoundException;
import com.marketplace.company.exception.BrokerNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BrokerDocumentService {

    List<BrokerDocumentResponse> getBrokerDocuments(String userId, String companyId, String brokerProfileId) throws BrokerNotFoundException;

    void addDocument(String userId, String companyId, String brokerProfileId, MultipartFile multipartFile) throws BrokerNotFoundException, IOException;

    void verifyDocument(String companyId, String brokerProfileId, String brokerDocumentId) throws BrokerNotFoundException, BrokerDocumentNotFoundException;

}
