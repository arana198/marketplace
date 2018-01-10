package com.marketplace.company.service;

import com.marketplace.company.dto.BrokerProfileRequest;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.exception.BrokerNotFoundException;
import com.marketplace.company.exception.CompanyEmployeeInviteTokenNotFoundException;
import com.marketplace.company.exception.CompanyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BrokerService {

    Optional<BrokerProfileResponse> findByUserId(String userId);

    Optional<BrokerProfileResponse> findByBrokerProfileId(String brokerProfileId);

    Optional<BrokerProfileResponse> findByCompanyIdAndBrokerProfileId(String companyId, String brokerProfileId);

    List<BrokerProfileResponse> findByCompanyAdmin(String companyId);

    Page<BrokerProfileResponse> findByCompanyId(String companyId, Pageable pageable);

    BrokerProfileResponse addBrokerToCompany(String userId, String companyId, CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest) throws CompanyNotFoundException, CompanyEmployeeInviteTokenNotFoundException;

    void updateBrokerProfile(String userId, String companyId, String brokerProfileId, BrokerProfileRequest brokerProfileRequest) throws CompanyNotFoundException, BrokerNotFoundException;

    void updateBrokerActiveFlag(String userId, boolean isActive);

    void updateBrokerActiveFlagByCompany(String companyId, boolean isActive);

    void removeBrokerFromCompany(String companyId, String brokerProfileId);

    void addAdminBrokerForCompany(String companyId, String brokerProfileId);

    void removeAdminBrokerFromCompany(String companyId, String brokerProfileId);

    BrokerProfileResponse addOrUpdateImage(String userId, String companyId, String brokerProfileId, MultipartFile multipartFile) throws BrokerNotFoundException, IOException;

    void inviteEmployee(String companyId, CompanyEmployeeInviteRequest companyEmployeeInviteRequest) throws CompanyNotFoundException;
}
