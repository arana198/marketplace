package com.marketplace.config;

import com.marketplace.common.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

     @Autowired
     private ApplicationContext applicationContext;

     @Bean
     public RoleHierarchy roleHierarchy() {
          RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
          roleHierarchy.setHierarchy(String.format("%s > %s %s > %s %s > %s",
              UserRole.ROLE_ADMIN,
              UserRole.ROLE_COMPANY_ADMIN,
              UserRole.ROLE_COMPANY_ADMIN,
              UserRole.ROLE_BROKER,
              UserRole.ROLE_ADMIN,
              UserRole.ROLE_USER));

          return roleHierarchy;
     }

     @Primary
     @Bean
     public RoleHierarchyVoter roleVoter() {
          return new RoleHierarchyVoter(roleHierarchy());
     }

     @Primary
     @Bean
     protected AffirmativeBased getAccessDecisionManager() {
          DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
          expressionHandler.setRoleHierarchy(this.roleHierarchy());

          WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
          webExpressionVoter.setExpressionHandler(expressionHandler);

          return new AffirmativeBased(Arrays.asList(this.roleVoter(), webExpressionVoter));
     }

     @Override
     public AccessDecisionManager accessDecisionManager() {
          List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
          ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
          expressionAdvice.setExpressionHandler(getExpressionHandler());
          decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));
          decisionVoters.add(new Jsr250Voter());
          decisionVoters.add(this.roleVoter());

          return new AffirmativeBased(decisionVoters);
     }

     @Override
     protected MethodSecurityExpressionHandler createExpressionHandler() {
          OAuth2MethodSecurityExpressionHandler handler = new OAuth2MethodSecurityExpressionHandler();
          handler.setApplicationContext(applicationContext);
          handler.setRoleHierarchy(roleHierarchy());
          return handler;
     }
}
