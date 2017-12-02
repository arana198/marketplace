package com.marketplace.common.security;

import com.marketplace.company.service.BrokerService;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class SecurityUtils {

    private final BrokerService companyEmployeeService;

    public boolean checkIfUserAuthorized(final String userId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final List<String> roles = authentication.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (roles.contains(UserRole.ROLE_ADMIN)) {
            return true;
        }

        if (!AuthUser.getUserId().equalsIgnoreCase(userId)) {
            throw new UnauthorizedUserException("Unauthorized user");
        }

        return true;
    }

    public boolean isCompanyAdmin(final String companyId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final List<String> roles = authentication.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (roles.contains(UserRole.ROLE_ADMIN) || (companyId == null && roles.contains(UserRole.ROLE_COMPANY_ADMIN))) {
            return true;
        }

        if ((companyId == null && !roles.contains(UserRole.ROLE_COMPANY_ADMIN)) || roles.contains(UserRole.ROLE_COMPANY_ADMIN) && this.checkIfCompanyAdmin(companyId)) {
            throw new UnauthorizedUserException("Unauthorized user");
        }

        return true;
    }

    public boolean isCompanyEmployee(final String companyId, final String employeeId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final List<String> roles = authentication.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        //TODO: CHeck if company admin and employee belongs to the company

        return true;
    }

    private boolean checkIfCompanyAdmin(final String companyId) {
        return companyEmployeeService.findByCompanyAdmin(companyId)
                .parallelStream()
                .anyMatch(ce -> ce.getUserId().equalsIgnoreCase(AuthUser.getUserId()));
    }
}
