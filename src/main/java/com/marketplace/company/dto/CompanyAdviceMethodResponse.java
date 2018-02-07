package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

import java.util.List;

@Data
public class CompanyAdviceMethodResponse extends BaseResponseDomain {
    private final List<String> adviceIds;
}
