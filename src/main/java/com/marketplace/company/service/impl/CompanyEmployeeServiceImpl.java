package com.marketplace.company.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.InternalServerException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.company.domain.CompanyEmployeeBO;
import com.marketplace.company.domain.CompanyEmployeeInviteBO;
import com.marketplace.company.dto.CompanyEmployeeInviteRequest;
import com.marketplace.company.dto.CompanyEmployeeInviteTokenRequest;
import com.marketplace.company.dto.CompanyEmployeeRequest;
import com.marketplace.company.dto.CompanyEmployeeResponse;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.company.exception.CompanyAlreadyExistsException;
import com.marketplace.company.exception.CompanyEmployeeInviteTokenNotFoundException;
import com.marketplace.company.exception.CompanyEmployeeNotFoundException;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.service.CompanyEmployeeService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Data
@Service
class CompanyEmployeeServiceImpl implements CompanyEmployeeService {

    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final CompanyEmployeeInviteRepository companyEmployeeInviteRepository;
    private final CompanyService companyService;
    private final UserService userService;
    private final CompanyEmployeeResponseConverter companyEmployeeResponseConverter;
    private final PublishService publishService;

    @Override
    public List<CompanyEmployeeResponse> findByCompanyAdmin(String companyId) {
        return companyEmployeeRepository.findByCompanyIdAndAdminPrivilege(companyId, true)
                .parallelStream()
                .map(companyEmployeeResponseConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CompanyEmployeeResponse> findByCompanyId(final String companyId, final Pageable pageable) {
        return null;
    }

    @Transactional
    @Override
    public String addEmployeeToCompany(final String companyId, final CompanyEmployeeInviteTokenRequest companyEmployeeInviteTokenRequest)
            throws CompanyNotFoundException, CompanyAlreadyExistsException, CompanyEmployeeInviteTokenNotFoundException {

        final CompanyEmployeeInviteBO companyEmployeeInviteBO = companyEmployeeInviteRepository.findByCompanyIdAndToken(companyId, companyEmployeeInviteTokenRequest.getToken())
                .orElseThrow(() -> new CompanyEmployeeInviteTokenNotFoundException(companyEmployeeInviteTokenRequest.getToken()));

        UserResponse userResponse = userService.findByUsername(companyEmployeeInviteBO.getEmail())
                .orElseThrow(() -> new BadRequestException("User not registered"));

        if (!AuthUser.getUserId().equalsIgnoreCase(userResponse.getUserId())) {
            throw new BadRequestException("User not authorized");
        }

        companyService.findById(companyId)
                .orElseThrow(() -> new InternalServerException("Company " + companyId + " not found"));

        final CompanyEmployeeBO companyEmployeeBO = new CompanyEmployeeBO()
                .setUserId(userResponse.getUserId())
                .setCompanyId(companyId)
                .setAdminPrivilege(false);

        companyEmployeeInviteRepository.delete(companyEmployeeInviteBO);
        companyEmployeeRepository.save(companyEmployeeBO);
        return companyEmployeeBO.getId();
    }

    @Override
    public void updateEmployeeInCompany(final String companyId, final String employeeId, final CompanyEmployeeRequest companyEmployeeRequest)
            throws CompanyNotFoundException, CompanyAlreadyExistsException, CompanyEmployeeNotFoundException {

        if (!companyEmployeeRequest.isAdmin() && companyEmployeeRepository.findByCompanyIdAndAdminPrivilege(companyId, true)
                .parallelStream()
                .filter(CompanyEmployeeBO::isAdminPrivilege)
                .count() == 1) {
            throw new BadRequestException("Cannot remove the only admin associated to company");
        }

        CompanyEmployeeBO companyEmployeeBO = companyEmployeeRepository.findById(employeeId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .orElseThrow(() -> new CompanyEmployeeNotFoundException(companyId, employeeId));

        companyEmployeeBO.setAdminPrivilege(companyEmployeeRequest.isAdmin());
        companyEmployeeRepository.save(companyEmployeeBO);
    }

    @Override
    public void removeEmployeeFromCompany(final String companyId, final String employeeId) {
        if (employeeId.equalsIgnoreCase(AuthUser.getUserId()) && companyEmployeeRepository.findByCompanyIdAndAdminPrivilege(companyId, true)
                .parallelStream()
                .filter(CompanyEmployeeBO::isAdminPrivilege)
                .count() == 1) {
            log.info("Company admin [ {} ] trying to remove the only admin (himself)", employeeId);
            throw new BadRequestException("Cannot remove the only admin associated to company");
        }

        companyEmployeeRepository.findById(employeeId)
                .filter(ce -> ce.getCompanyId().equalsIgnoreCase(companyId))
                .ifPresent(companyEmployeeRepository::delete);
    }

    @Override
    public void inviteEmployee(final String companyId, final CompanyEmployeeInviteRequest companyEmployeeInviteRequest) throws CompanyNotFoundException {

        final CompanyResponse companyResponse = companyService.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        final CompanyEmployeeInviteBO companyEmployeeInviteBO = companyEmployeeInviteRepository.findByCompanyIdAndEmail(companyId, companyEmployeeInviteRequest.getEmail())
                .map(cei -> cei.setToken(UUID.randomUUID().toString()))
                .map(cei -> (CompanyEmployeeInviteBO) cei.setCreatedAt(LocalDate.now()))
                .orElse(new CompanyEmployeeInviteBO()
                        .setCompanyId(companyId)
                        .setEmail(companyEmployeeInviteRequest.getEmail())
                        .setToken(UUID.randomUUID().toString()));

        //TODO: Email the user
        companyEmployeeInviteRepository.save(companyEmployeeInviteBO);
        publishService.sendMessage(PublishAction.INVITE_BROKER,
                new InviteBrokerTokenVerificationResponse(companyId,
                        companyResponse.getName(),
                        companyEmployeeInviteRequest.getEmail(),
                        companyEmployeeInviteBO.getToken()));
    }
}
