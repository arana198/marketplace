package com.marketplace.profile.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.profile.domain.UserProfileBO;
import com.marketplace.profile.dto.UserProfileRequest;
import org.springframework.stereotype.Service;

@Service
class UserProfileRequestConverter implements BaseConverter<UserProfileRequest, UserProfileBO> {

     @Override
     public UserProfileBO convert(final UserProfileRequest source) {
          return new UserProfileBO()
              .setUserId(source.getUserId())
              .setFirstName(source.getFirstName())
              .setLastName(source.getLastName())
              .setDateOfBirth(source.getDateOfBirth())
              .setMobileNumber(source.getMobileNumber());
     }


     public UserProfileBO convert(final UserProfileBO oldUserProfile, final UserProfileRequest newUserProfile) {
          return oldUserProfile
              .setMobileNumber(newUserProfile.getMobileNumber())
              .setDateOfBirth(newUserProfile.getDateOfBirth())
              .setFirstName(newUserProfile.getFirstName())
              .setLastName(newUserProfile.getLastName());
     }
}
