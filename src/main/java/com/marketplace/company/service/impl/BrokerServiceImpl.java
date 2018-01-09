package com.marketplace.company.service.impl;

import com.marketplace.common.exception.BadRequestException;
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
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.BrokerService;
import com.marketplace.company.service.BrokerValidatorService;
import com.marketplace.company.service.CompanyService;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileRequest.FileType;
import com.marketplace.storage.dto.FileResponse;
import com.marketplace.storage.service.FileStoreService;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
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
    private final BrokerValidatorService brokerValidatorService;
    private final FileStoreService fileStoreService;
    private final BrokerProfileResponseConverter brokerProfileResponseConverter;
    private final BrokerProfileRequestConverter brokerProfileRequestConverter;
    private final CompanyFileRequestConverter fileRequestConverter;
    private final CompanyPublishService publishService;

    @Override
    public Optional<BrokerProfileResponse> findByUserId(final String userId) {
        return brokerProfileRepository.findByUserId(userId)
                .parallelStream()
                .filter(bp -> bp.isActive())
                .findFirst()
                .map(brokerProfileResponseConverter::convert);
    }

    @Override
    public Optional<BrokerProfileResponse> findByBrokerProfileId(String brokerProfileId) {
        return brokerProfileRepository.findById(brokerProfileId)
                .map(brokerProfileResponseConverter::convert);
    }

    @Override
    public Optional<BrokerProfileResponse> findByCompanyIdAndBrokerProfileId(final String companyId, final String brokerProfileId) {
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
    public BrokerProfileResponse addBrokerToCompany(final String userId,
                                                    final String companyId,
                                                    final CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest)
            throws CompanyNotFoundException, CompanyEmployeeInviteTokenNotFoundException {

        log.info("Adding broker with token [ {} ] to company [ {} ]", companyEmployeeInviteTokenRequest.getToken(), companyId);

        final CompanyEmployeeInviteBO companyEmployeeInviteBO = companyEmployeeInviteRepository.findByCompanyIdAndToken(companyId, companyEmployeeInviteTokenRequest.getToken())
                .orElseThrow(() -> new CompanyEmployeeInviteTokenNotFoundException(companyEmployeeInviteTokenRequest.getToken()));

        if (!companyId.equalsIgnoreCase(companyEmployeeInviteTokenRequest.getBrokerProfile().getCompanyId())) {
            throw new BadRequestException("Invalid companyId in the body");
        }

        final UserResponse userResponse = userService.findByUsername(companyEmployeeInviteBO.getEmail())
                .orElseThrow(() -> new BadRequestException("User not registered"));

        if (!userId.equalsIgnoreCase(userResponse.getUserId())) {
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
        publishService.sendMessage(CompanyPublishAction.BROKER_ADDED_TO_COMPANY, brokerProfileResponseConverter.convert(brokerProfileBO));

        return brokerProfileResponseConverter.convert(brokerProfileBO);
    }

    @Override
    public void updateBrokerProfile(final String userId,
                                    final String companyId,
                                    final String brokerProfileId,
                                    final BrokerProfileRequest brokerProfileRequest)
            throws CompanyNotFoundException, BrokerNotFoundException {

        log.info("Updating broker [ {} ] for company [ {} ]", brokerProfileId, companyId);

        final BrokerProfileBO oldBrokerProfileBO = brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .filter(ce -> ce.getUserId().equalsIgnoreCase(userId))
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
        this.updateBrokerActiveFlag(oldBrokerProfileBO, newBrokerProfileBO);

        brokerProfileRepository.save(newBrokerProfileBO);
        publishService.sendMessage(CompanyPublishAction.BROKER_PROFILE_UPDATED, brokerProfileResponseConverter.convert(newBrokerProfileBO));
    }

    @Override
    public void updateBrokerActiveFlag(final String userId, final boolean isActive) {

        log.info("Updating broker with user id [ {} ] active flag to [ {} ]", userId, isActive);

        brokerProfileRepository.findByUserId(userId)
                .parallelStream()
                .filter(brokerProfileBO -> brokerProfileBO.isActive() != isActive)
                .forEach(brokerProfile -> this.updateAndPublishBrokerActiveFlag(brokerProfile, isActive));
    }

    @Override
    public void updateBrokerActiveFlagByCompany(final String companyId, final boolean isActiveFlag) {
        log.info("Updating broker for company [ {} ] active flag to [ {} ]", companyId, isActiveFlag);


        int index = 0;
        boolean hasNext;

        do {
            Page<BrokerProfileBO> brokerProfiles = brokerProfileRepository.findByCompanyId(companyId, new PageRequest(index, 20));
            hasNext = brokerProfiles.hasNext();
            brokerProfiles.getContent()
                    .parallelStream()
                    .filter(brokerProfileBO -> brokerProfileBO.isActive() != isActiveFlag)
                    .forEach(brokerProfile -> {
                        try {
                            this.updateAndPublishBrokerActiveFlag(brokerProfile, isActiveFlag);
                        } catch (BadRequestException ex) {
                            log.debug("User {} inactive", brokerProfile.getUserId());
                        }
                    });
            index++;
        } while (hasNext);
    }

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
                    publishService.sendMessage(CompanyPublishAction.BROKER_REMOVED_FROM_COMPANY, brokerProfileResponseConverter.convert(bp));
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
                    publishService.sendMessage(CompanyPublishAction.BROKER_ADDED_AS_ADMIN, brokerProfileResponseConverter.convert(bp));
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
                    publishService.sendMessage(CompanyPublishAction.BROKER_REMOVED_AS_ADMIN, brokerProfileResponseConverter.convert(bp));
                });
    }

    @Override
    public BrokerProfileResponse addOrUpdateImage(final String userId,
                                                  final String companyId,
                                                  final String brokerProfileId,
                                                  final MultipartFile multipartFile)
            throws BrokerNotFoundException, IOException {

        log.info("Add or updating broker [ {} ] profile image", brokerProfileId);

        final BrokerProfileBO oldBrokerProfileBO = brokerProfileRepository.findById(brokerProfileId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .filter(ce -> ce.getUserId().equalsIgnoreCase(userId))
                .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerProfileId));

        final FileRequest fileRequest = fileRequestConverter.getFileRequest(oldBrokerProfileBO.getId(), multipartFile.getOriginalFilename(), "Profile Image", FileType.PROFILE_IMAGE);
        final FileResponse fileResponse = fileStoreService.store(fileRequest, multipartFile);
        oldBrokerProfileBO.setImageUrl(String.format("/documents/%s", fileResponse.getFileId()));

        brokerProfileRepository.save(oldBrokerProfileBO);
        return brokerProfileResponseConverter.convert(oldBrokerProfileBO);
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
        publishService.sendMessage(CompanyPublishAction.INVITE_BROKER,
                new InviteBrokerTokenVerificationResponse(companyId,
                        companyResponse.getName(),
                        companyEmployeeInviteRequest.getEmail(),
                        companyEmployeeInviteBO.getToken()));
    }


    private void updateBrokerActiveFlag(final BrokerProfileBO oldBrokerProfileBO, final BrokerProfileBO newBrokerProfileBO) {
        if (!oldBrokerProfileBO.isActive()
                && newBrokerProfileBO.isActive()
                && brokerValidatorService.checkIfBrokerVerified(oldBrokerProfileBO.getCompanyId(), oldBrokerProfileBO.getId())) {

            userService.findById(oldBrokerProfileBO.getUserId())
                    .map(UserResponse::getUserRoles)
                    .filter(urStream -> urStream.parallelStream()
                            .filter(ur -> ur.getRole().equalsIgnoreCase(UserRole.ROLE_BROKER.getValue()) || ur.getRole().equalsIgnoreCase(UserRole.ROLE_COMPANY_ADMIN.getValue()))
                            .filter(ur -> ur.getUserStatus().equalsIgnoreCase("ACTIVE"))
                            .count() > 0)
                    .orElseThrow(() -> new BadRequestException("User not active"));

            newBrokerProfileBO.setActive(true);
        }
    }

    private void updateAndPublishBrokerActiveFlag(final BrokerProfileBO oldBrokerProfileBO, final boolean isActiveFlag) {
        BrokerProfileBO newBrokerProfileBO = oldBrokerProfileBO.setActive(isActiveFlag);
        this.updateBrokerActiveFlag(oldBrokerProfileBO, newBrokerProfileBO);
        brokerProfileRepository.save(newBrokerProfileBO);
        publishService.sendMessage(CompanyPublishAction.BROKER_PROFILE_UPDATED, brokerProfileResponseConverter.convert(newBrokerProfileBO));
    }
}
