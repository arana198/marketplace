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

    private final BrokerService brokerService;

    public boolean checkIfUserAuthorized(final String userId) {

        final List<String> roles = this.getAuthenticatedUserRole();
        if (roles.contains(UserRole.ROLE_ADMIN)) {
            return true;
        }

        if (!AuthUser.getUserId().equalsIgnoreCase(userId)) {
            throw new UnauthorizedUserException("Unauthorized user");
        }

        return true;
    }

    public boolean isCompanyAdmin(final String companyId) {

        final List<String> roles = this.getAuthenticatedUserRole();
        if (roles.contains(UserRole.ROLE_ADMIN)) {
            return true;
        }

        if (roles.contains(UserRole.ROLE_ADMIN) || (companyId == null && roles.contains(UserRole.ROLE_COMPANY_ADMIN))) {
            return true;
        }

        if (!roles.contains(UserRole.ROLE_COMPANY_ADMIN) || !this.checkIfCompanyAdmin(companyId)) {
            throw new UnauthorizedUserException("Unauthorized user");
        }

        return true;
    }

    public boolean isCompanyEmployee(final String companyId, final String brokerId) {

        final List<String> roles = this.getAuthenticatedUserRole();
        if (roles.contains(UserRole.ROLE_ADMIN)) {
            return true;
        }

        if ((roles.contains(UserRole.ROLE_COMPANY_ADMIN) && !this.checkIfCompanyAdmin(companyId)) ||
                !brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerId)
                        .filter(bp -> bp.isActive())
                        .isPresent()) {
            throw new UnauthorizedUserException("Unauthorized user");
        }

        return true;
    }

    private boolean checkIfCompanyAdmin(final String companyId) {
        return brokerService.findByUserId(AuthUser.getUserId())
                .filter(bp -> bp.getCompanyId().equalsIgnoreCase(companyId))
                .filter(bp -> bp.isAdmin())
                .isPresent();
    }

    private List<String> getAuthenticatedUserRole() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
