package com.marketplace.company.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.company.domain.BrokerProfileBO;
import com.marketplace.company.domain.CompanyEmployeeInviteBO;
import com.marketplace.company.dto.BrokerProfileRequest;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.company.exception.BrokerNotFoundException;
import com.marketplace.company.exception.CompanyEmployeeInviteTokenNotFoundException;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.service.BrokerService;
import com.marketplace.company.service.CompanyService;
import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import com.marketplace.user.dto.RoleRequest.UserRole;
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
import java.util.function.Supplier;
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
                .parallelStream()
                .filter(bp -> bp.isActive())
                .findFirst()
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

        if (!companyId.equalsIgnoreCase(companyEmployeeInviteTokenRequest.getBrokerProfile().getCompanyId())) {
            throw new BadRequestException("Invalid companyId in the body");
        }

        final UserResponse userResponse = userService.findByUsername(companyEmployeeInviteBO.getEmail())
                .orElseThrow(() -> new BadRequestException("User not registered"));

        if (!AuthUser.getUserId().equalsIgnoreCase(userResponse.getUserId())) {
            throw new BadRequestException("User not authorized");
        }

        final Supplier<Stream<BrokerProfileBO>> brokerProfileStreamSupplier = () -> brokerProfileRepository.findByUserId(userResponse.getUserId())
                .parallelStream();

        if (brokerProfileStreamSupplier.get().anyMatch(bp -> bp.isActive())) {
            throw new BadRequestException("User already part of another company");
        }

        final BrokerProfileBO brokerProfileBO = brokerProfileRequestConverter.convert(companyEmployeeInviteTokenRequest.getBrokerProfile());

        brokerProfileStreamSupplier.get()
                .filter(bp -> !bp.isActive())
                .findFirst()
                .ifPresent(bp -> brokerProfileBO.update(bp));

        brokerProfileBO.setActive(false);

        companyEmployeeInviteRepository.delete(companyEmployeeInviteBO);
        brokerProfileRepository.save(brokerProfileBO);
        publishService.sendMessage(PublishAction.BROKER_ADDED_TO_COMPANY, brokerProfileResponseConverter.convert(brokerProfileBO));

        //TODO: Once the event is published need to check user so we know old user was activated or not
        return brokerProfileResponseConverter.convert(brokerProfileBO);
    }

    @Override
    public void updateBrokerProfile(final String companyId, final String brokerProfileId, final BrokerProfileRequest brokerProfileRequest)
            throws CompanyNotFoundException, BrokerNotFoundException {

        log.info("Updating broker [ {} ] for company [ {} ]", brokerProfileId, companyId);

        final BrokerProfileBO oldBrokerProfileBO = brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerProfileId));

        if (!companyId.equalsIgnoreCase(brokerProfileRequest.getCompanyId())) {
            log.info("Changing company for broker [ {} ], from: [ {} ] to: [ {} ]", oldBrokerProfileBO.getId(), companyId, brokerProfileRequest.getCompanyId());
            companyService.findById(companyId)
                    .orElseThrow(() -> new CompanyNotFoundException(companyId));
        }

        final BrokerProfileBO newBrokerProfileBO = brokerProfileRequestConverter.convert(brokerProfileRequest)
                .setUserId(oldBrokerProfileBO.getUserId())
                .setAdmin(oldBrokerProfileBO.isAdmin());

        newBrokerProfileBO.update(oldBrokerProfileBO);

        if (!oldBrokerProfileBO.isActive() && newBrokerProfileBO.isActive()) {
            userService.findById(oldBrokerProfileBO.getUserId())
                    .map(UserResponse::getUserRoles)
                    .filter(urStream -> urStream.parallelStream()
                            .filter(ur -> ur.getRole().equalsIgnoreCase(UserRole.ROLE_BROKER.getValue()) || ur.getRole().equalsIgnoreCase(UserRole.ROLE_COMPANY_ADMIN.getValue()))
                            .filter(ur -> ur.getUserStatus().equalsIgnoreCase("ACTIVE"))
                            .count() > 0)
                    .orElseThrow(() -> new BadRequestException("User not active"));
        }

        brokerProfileRepository.save(newBrokerProfileBO);
    }

    /*
    TODO; Another idea! The company owner creating profile on our platform should be responsible for checking the compliance of a broker?
    But at the same time to be safe the platform should force a broker to submit this documents
     */
    @Override
    public void removeBrokerFromCompany(final String companyId, final String brokerProfileId) {

        log.info("Removing broker [ {} ] from company [ {} ]", brokerProfileId, companyId);

        final Supplier<Stream<BrokerProfileBO>> brokerProfileStreamSupplier = () -> brokerProfileRepository.findByCompanyIdAndAdmin(companyId, true)
                .parallelStream()
                .filter(BrokerProfileBO::isAdmin);

        if (brokerProfileStreamSupplier.get().count() == 1
                && brokerProfileStreamSupplier.get().anyMatch(brokerProfileBO -> brokerProfileBO.getId().equalsIgnoreCase(brokerProfileId))) {

            log.info("Company admin [ {} ] trying to remove the only admin (himself)", brokerProfileId);
            throw new BadRequestException("Cannot remove the only admin associated to the company");
        }

        brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .ifPresent(bp -> {
                    log.debug("Removing broker [ {} ] as an admin for a company [ {} ]", brokerProfileId, companyId);
                    bp.setActive(false);
                    brokerProfileRepository.save(bp);
                    userService.removeAsCompanyAdmin(bp.getUserId());
                    publishService.sendMessage(PublishAction.BROKER_REMOVED_FROM_COMPANY, brokerProfileResponseConverter.convert(bp));
                });
    }

    @Transactional
    @Override
    public void addAdminBrokerForCompany(final String companyId, final String brokerProfileId) {
        log.info("Add broker [ {} ] as an admin for a company [ {} ]", brokerProfileId, companyId);

        brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .filter(BrokerProfileBO::isActive)
                .ifPresent(bp -> {
                    log.debug("Adding broker [ {} ] as an admin for a company [ {} ]", brokerProfileId, companyId);
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

        final Supplier<Stream<BrokerProfileBO>> brokerProfileStreamSupplier = () -> brokerProfileRepository.findByCompanyIdAndAdmin(companyId, true)
                .parallelStream()
                .filter(BrokerProfileBO::isAdmin);

        if (brokerProfileStreamSupplier.get().count() == 1
                && brokerProfileStreamSupplier.get().anyMatch(brokerProfileBO -> brokerProfileBO.getId().equalsIgnoreCase(brokerProfileId))) {

            log.info("Company admin [ {} ] trying to remove the only admin (himself)", brokerProfileId);
            throw new BadRequestException("Cannot remove the only admin associated to the company");
        }

        brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .ifPresent(bp -> {
                    log.debug("Removing broker [ {} ] from the company [ {} ]", brokerProfileId, companyId);
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
