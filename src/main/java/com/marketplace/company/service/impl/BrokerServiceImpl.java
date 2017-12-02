package com.marketplace.company.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.InternalServerException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.company.domain.BrokerProfileBO;
import com.marketplace.company.domain.CompanyEmployeeInviteBO;
import com.marketplace.company.dto.BrokerProfileRequest;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.company.exception.CompanyEmployeeInviteTokenNotFoundException;
import com.marketplace.company.exception.CompanyEmployeeNotFoundException;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.service.BrokerService;
import com.marketplace.company.service.CompanyService;
import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
@Service
class BrokerServiceImpl implements BrokerService {

    private final BrokerProfileRepository brokerProfileRepository;
    private final CompanyEmployeeInviteRepository companyEmployeeInviteRepository;
    private final CompanyService companyService;
    private final UserService userService;
    private final BrokerProfileResponseConverter brokerProfileResponseConverter;
    private final BrokerProfileRequestConverter brokerProfileRequestConverter;
    private final PublishService publishService;

    @Override
    public Optional<BrokerProfileResponse> findByUserId(final String userId) {
        return brokerProfileRepository.findByUserId(userId)
                .map(brokerProfileResponseConverter::convert);
    }

    @Override
    public Optional<BrokerProfileResponse> findByCompanyIdAndBrokerProfileId(final String companyId, String brokerProfileId) {
        return brokerProfileRepository.findById(brokerProfileId)
                .filter(bp -> bp.getCompanyId().equalsIgnoreCase(companyId))
                .map(brokerProfileResponseConverter::convert);
    }

    @Override
    public List<BrokerProfileResponse> findByCompanyAdmin(final String companyId) {

        log.info("Getting admin brokers for a company [ {} ]", companyId);
        return brokerProfileRepository.findByCompanyIdAndAdmin(companyId, true)
                .parallelStream()
                .map(brokerProfileResponseConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BrokerProfileResponse> findByCompanyId(final String companyId, final Pageable pageable) {

        log.info("Getting brokers for a company [ {} ]", companyId);
        return brokerProfileRepository.findByCompanyId(companyId, pageable)
                .map(brokerProfileResponseConverter::convert);
    }

    @Transactional
    @Override
    public BrokerProfileResponse addBrokerToCompany(final String companyId, final CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest)
            throws CompanyNotFoundException, CompanyEmployeeInviteTokenNotFoundException {

        log.info("Adding broker with token [ {} ] to company [ {} ]", companyEmployeeInviteTokenRequest.getToken(), companyId);

        final CompanyEmployeeInviteBO companyEmployeeInviteBO = companyEmployeeInviteRepository.findByCompanyIdAndToken(companyId, companyEmployeeInviteTokenRequest.getToken())
                .orElseThrow(() -> new CompanyEmployeeInviteTokenNotFoundException(companyEmployeeInviteTokenRequest.getToken()));

        final UserResponse userResponse = userService.findByUsername(companyEmployeeInviteBO.getEmail())
                .orElseThrow(() -> new BadRequestException("User not registered"));

        if (!AuthUser.getUserId().equalsIgnoreCase(userResponse.getUserId())) {
            throw new BadRequestException("User not authorized");
        }

        companyService.findById(companyId)
                .orElseThrow(() -> new InternalServerException("Company " + companyId + " not found"));

        final BrokerProfileBO brokerProfileBO = brokerProfileRequestConverter.convert(companyEmployeeInviteTokenRequest.getBrokerProfile());

        companyEmployeeInviteRepository.delete(companyEmployeeInviteBO);
        brokerProfileRepository.save(brokerProfileBO);
        publishService.sendMessage(PublishAction.BROKER_ADDED_TO_COMPANY, brokerProfileResponseConverter.convert(brokerProfileBO));

        return brokerProfileResponseConverter.convert(brokerProfileBO);
    }

    @Override
    public void updateBrokerProfile(final String companyId, final String brokerProfileId, final BrokerProfileRequest brokerProfileRequest)
            throws CompanyNotFoundException, CompanyEmployeeNotFoundException {

        log.info("Updating broker [ {} ] for company [ {} ]", brokerProfileId, companyId);

        final BrokerProfileBO oldBrokerProfileBO = brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .orElseThrow(() -> new CompanyEmployeeNotFoundException(companyId, brokerProfileId));

        if (!companyId.equalsIgnoreCase(brokerProfileRequest.getCompanyId())) {
            log.info("Changing company for broker [ {} ], from: [ {} ] to: [ {} ]", oldBrokerProfileBO.getId(), companyId, brokerProfileRequest.getCompanyId());
            companyService.findById(companyId)
                    .orElseThrow(() -> new CompanyNotFoundException(companyId));
        }

        final BrokerProfileBO newBrokerProfileBO = (BrokerProfileBO) brokerProfileRequestConverter.convert(brokerProfileRequest)
                .setUserId(oldBrokerProfileBO.getUserId())
                .setAdmin(oldBrokerProfileBO.isAdmin())
                .setId(oldBrokerProfileBO.getId());

        brokerProfileRepository.save(newBrokerProfileBO);
    }

    //TODO: Add broker back (i.e. activated)
    @Override
    public void removeBrokerFromCompany(final String companyId, final String brokerProfileId) {

        log.info("Removing broker [ {} ] from company [ {} ]", brokerProfileId, companyId);

        final Stream<BrokerProfileBO> brokerProfileBOStream = brokerProfileRepository.findByCompanyIdAndAdmin(companyId, true)
                .parallelStream()
                .filter(BrokerProfileBO::isAdmin);

        if (brokerProfileBOStream.count() == 1
                && brokerProfileBOStream.anyMatch(brokerProfileBO -> brokerProfileBO.getId().equalsIgnoreCase(brokerProfileId))) {

            log.info("Company admin [ {} ] trying to remove the only admin (himself)", brokerProfileId);
            throw new BadRequestException("Cannot remove the only admin associated to the company");
        }

        brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .ifPresent(bp -> {
                    bp.setActive(false);
                    brokerProfileRepository.save(bp);
                    userService.removeAsCompanyAdmin(bp.getUserId());
                });
    }

    @Override
    public void addAdminBrokerForCompany(final String companyId, final String brokerProfileId) {
        log.info("Add broker [ {} ] as an admin for a company [ {} ]", brokerProfileId, companyId);
        brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .ifPresent(bp -> {
                    bp.setAdmin(true);
                    brokerProfileRepository.save(bp);
                    userService.addAsCompanyAdmin(bp.getUserId());
                    publishService.sendMessage(PublishAction.BROKER_ADDED_AS_ADMIN, brokerProfileResponseConverter.convert(bp));
                });
    }

    @Transactional
    @Override
    public void removeAdminBrokerFromCompany(final String companyId, final String brokerProfileId) {

        log.info("Removing admin privilege for broker [ {} ] from company [ {} ]", brokerProfileId, companyId);

        final Stream<BrokerProfileBO> brokerProfileBOStream = brokerProfileRepository.findByCompanyIdAndAdmin(companyId, true)
                .parallelStream()
                .filter(BrokerProfileBO::isAdmin);

        if (brokerProfileBOStream.count() == 1
                && brokerProfileBOStream.anyMatch(brokerProfileBO -> brokerProfileBO.getId().equalsIgnoreCase(brokerProfileId))) {

            log.info("Company admin [ {} ] trying to remove the only admin (himself)", brokerProfileId);
            throw new BadRequestException("Cannot remove the only admin associated to the company");
        }

        brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .ifPresent(bp -> {
                    bp.setAdmin(false);
                    brokerProfileRepository.save(bp);
                    userService.removeAsCompanyAdmin(bp.getUserId());
                    publishService.sendMessage(PublishAction.BROKER_REMOVED_AS_ADMIN, brokerProfileResponseConverter.convert(bp));
                });
    }

    @Override
    public void inviteEmployee(final String companyId, final CompanyEmployeeInviteRequest companyEmployeeInviteRequest) throws CompanyNotFoundException {
        log.info("Inviting broker [ {} ] for company [ {} ]", companyEmployeeInviteRequest.getEmail(), companyId);

        final CompanyResponse companyResponse = companyService.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        final CompanyEmployeeInviteBO companyEmployeeInviteBO = companyEmployeeInviteRepository.findByCompanyIdAndEmail(companyId, companyEmployeeInviteRequest.getEmail())
                .map(cei -> cei.setToken(UUID.randomUUID().toString()))
                .map(cei -> (CompanyEmployeeInviteBO) cei.setCreatedAt(LocalDate.now()))
                .orElse(new CompanyEmployeeInviteBO()
                        .setCompanyId(companyId)
                        .setEmail(companyEmployeeInviteRequest.getEmail())
                        .setToken(UUID.randomUUID().toString()));

        companyEmployeeInviteRepository.save(companyEmployeeInviteBO);
        publishService.sendMessage(PublishAction.INVITE_BROKER,
                new InviteBrokerTokenVerificationResponse(companyId,
                        companyResponse.getName(),
                        companyEmployeeInviteRequest.getEmail(),
                        companyEmployeeInviteBO.getToken()));
    }
}
