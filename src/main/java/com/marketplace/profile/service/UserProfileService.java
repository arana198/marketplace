package com.marketplace.profile.service;

import com.marketplace.profile.dto.UserProfileRequest;
import com.marketplace.profile.dto.UserProfileResponse;
import com.marketplace.profile.exception.MobileNumberAlreadyExistsException;
import com.marketplace.profile.exception.UserProfileAlreadyExistsException;
import com.marketplace.profile.exception.UserProfileNotFoundException;
import com.marketplace.profile.exception.UsernameAlreadyExistsException;
import com.marketplace.user.exception.UserNotFoundException;

import java.util.Optional;

public interface UserProfileService {
     Optional<UserProfileResponse> findByUserId(String userId);

     UserProfileResponse createProfile(String userId, UserProfileRequest userProfile)
         throws UserProfileAlreadyExistsException, MobileNumberAlreadyExistsException, UserNotFoundException;

     void updateProfile(String userId, String profileId, UserProfileRequest userProfile)
         throws UserProfileNotFoundException, MobileNumberAlreadyExistsException, UsernameAlreadyExistsException;
}
