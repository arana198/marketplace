package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class BrokerProfileResponse extends BaseResponseDomain {
     private final String brokerProfileId;
     private final String userId;
     private final String companyId;
     private final String firstName;
     private final String lastName;
     private final String mobileNumber;
     private final String aboutMe;
     private final String imageUrl;
     private final boolean isAdmin;
     private final boolean isActive;
}
