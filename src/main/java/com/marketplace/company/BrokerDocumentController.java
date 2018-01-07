package com.marketplace.company;

import com.marketplace.common.annotation.IsActive;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.BrokerDocumentResponse;
import com.marketplace.company.service.BrokerDocumentService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;

@Data
@Slf4j
@Controller
@RequestMapping("/companies/{companyId}/brokers/{brokerId}/documents")
public class BrokerDocumentController {

    private final BrokerDocumentService brokerDocumentService;

    @RolesAllowed({UserRole.ROLE_BROKER})
    @GetMapping
    public ResponseEntity<List<BrokerDocumentResponse>> getDocuments(@PathVariable final String companyId,
                                                                     @PathVariable final String brokerId)
            throws ResourceNotFoundException {

        log.info("Adding broker: {} certification document", brokerId);
        List<BrokerDocumentResponse> brokerDocuments = brokerDocumentService.getBrokerDocuments(AuthUser.getUserId(), companyId, brokerId);
        return new ResponseEntity<>(brokerDocuments, HttpStatus.OK);
    }

    @RolesAllowed({UserRole.ROLE_BROKER})
    @PostMapping
    public ResponseEntity<Void> addDocument(@PathVariable final String companyId,
                                            @PathVariable final String brokerId,
                                            @RequestPart(name = "file") final MultipartFile multipartFile)
            throws ResourceNotFoundException, IOException {

        log.info("Adding broker: {} certification document", brokerId);
        brokerDocumentService.addDocument(AuthUser.getUserId(), companyId, brokerId, multipartFile);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @IsActive
    @RolesAllowed({UserRole.ROLE_ADMIN})
    @PutMapping(path = "/{documentId}")
    public ResponseEntity<Void> verifyCDocument(@PathVariable final String companyId,
                                                @PathVariable final String brokerId,
                                                @PathVariable final String documentId)
            throws ResourceNotFoundException {

        log.info("Verifying broker: {} document {}", brokerId, documentId);
        brokerDocumentService.verifyDocument(companyId, brokerId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
