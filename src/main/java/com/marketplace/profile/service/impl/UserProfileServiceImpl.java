package com.marketplace.profile.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.profile.domain.UserProfileBO;
import com.marketplace.profile.dto.UserProfileRequest;
import com.marketplace.profile.dto.UserProfileResponse;
import com.marketplace.profile.exception.MobileNumberAlreadyExistsException;
import com.marketplace.profile.exception.UserProfileAlreadyExistsException;
import com.marketplace.profile.exception.UserProfileNotFoundException;
import com.marketplace.profile.exception.UsernameAlreadyExistsException;
import com.marketplace.profile.queue.publish.ProfilePublishService;
import com.marketplace.profile.queue.publish.domain.ProfilePublishAction;
import com.marketplace.profile.service.UserProfileService;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
class UserProfileServiceImpl implements UserProfileService {

  private final UserProfileResponseConverter userProfileResponseConverter;
  private final UserProfileRepository userProfileRepository;
  private final UserProfileRequestConverter userProfileRequestConverter;
  private final UserService userService;
  private final ProfilePublishService publishService;

  @Override
  public Optional<UserProfileResponse> findByUserId(final String userId) {
    return userProfileRepository.findByUserId(userId)
        .map(userProfileResponseConverter::convert);
  }

  @Override
  public UserProfileResponse createProfile(final String userId, final UserProfileRequest userProfile)
      throws UserProfileAlreadyExistsException, MobileNumberAlreadyExistsException, UserNotFoundException {
    LOGGER.info("Creating company for user {}", userId);
    if (!userId.equalsIgnoreCase(userProfile.getUserId())) {
      LOGGER.info("User id [ {} ] does not match body's user id [ {} ]", userId, userProfile.getUserId());
      throw new BadRequestException("User id does not match body's user id");
    }

    if (userProfileRepository.findByUserId(userProfile.getUserId()).isPresent()) {
      LOGGER.info("User [ {} ] company already exists", userId);
      throw new UserProfileAlreadyExistsException(userProfile.getUserId());
    }

    final UserResponse user = userService.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

    userProfile.setEmail(user.getEmail());
    final UserProfileBO userProfileBO = userProfileRequestConverter.convert(userProfile);

    userProfileRepository.save(userProfileBO);
    LOGGER.info("User company created [ {} ]", userId);

    final UserProfileResponse userProfileResponse = userProfileResponseConverter.convert(userProfileBO);
    publishService.sendMessage(ProfilePublishAction.USER_PROFILE_CREATED, userProfileResponse);
    return userProfileResponse;
  }

  @Override
  public void updateProfile(final String userId, final String profileId, final UserProfileRequest userProfile)
      throws UserProfileNotFoundException, MobileNumberAlreadyExistsException, UsernameAlreadyExistsException {

    LOGGER.info("Updating company for user {}", userId);
    if (!userId.equalsIgnoreCase(userProfile.getUserId())) {
      LOGGER.info("User id [ {} ] does not match body's user id [ {} ]", userId, userProfile.getUserId());
      throw new BadRequestException("User id does not match body's user id");
    }

    final UserProfileBO existingUserProfile = userProfileRepository.findByIdAndUserId(profileId, userId)
        .orElseThrow(() -> new UserProfileNotFoundException(userId, profileId));

    final UserProfileBO newUserProfile = userProfileRequestConverter.convert(existingUserProfile, userProfile);
    userProfileRepository.save(newUserProfile);
    publishService.sendMessage(ProfilePublishAction.USER_PROFILE_UPDATED, userProfileResponseConverter.convert(newUserProfile));
  }
}
