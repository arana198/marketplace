package com.marketplace.user.service.impl;

import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserRoleBO;
import com.marketplace.user.domain.UserStatusBO.UserStatus;
import com.marketplace.user.dto.UserRequest.LoginProvider;
import com.marketplace.user.exception.UserAuthenticationException;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
class UserAuthenticationManager implements AuthenticationManager, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        Object password = authentication.getCredentials();

        return userRepository.findByUsername(username)
                .filter(user -> !user.getRoles()
                        .parallelStream()
                        .anyMatch(ur -> ur.getProvider().equalsIgnoreCase(LoginProvider.LOCAL.getValue())) || passwordEncoder.matches(password.toString(), user.getPassword()))
                .filter(user -> !user.getRoles()
                        .parallelStream()
                        .anyMatch(ur -> ur.getUserStatus().equals(UserStatus.ACTIVE) || ur.getUserStatus().equals(UserStatus.PENDING)))
                .map(u -> new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), this.getRoleFromUser(u)))
                .orElseThrow(() -> new UserAuthenticationException("User authentication failed"));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(u -> new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword() == null ? "pass" : u.getPassword(), this.getRoleFromUser(u)))
                .orElseThrow(() -> new UserAuthenticationException("User authentication failed"));
    }

    private List<RoleBO> getRoleFromUser(final UserBO userBO) {
        return userBO.getRoles()
                .parallelStream()
                .map(UserRoleBO::getRole)
                .collect(Collectors.toList());
    }
}
