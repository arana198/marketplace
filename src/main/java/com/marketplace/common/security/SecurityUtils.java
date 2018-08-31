package com.marketplace.common.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class SecurityUtils {

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

          if ((roles.contains(UserRole.ROLE_COMPANY_ADMIN) && !this.checkIfCompanyAdmin(companyId))) {
               throw new UnauthorizedUserException("Unauthorized user");
          }

          return true;
     }

     private boolean checkIfCompanyAdmin(final String companyId) {
          //TODO: Implement
          return true;
     }

     private List<String> getAuthenticatedUserRole() {
          final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          return authentication.getAuthorities()
              .parallelStream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toList());
     }
}
