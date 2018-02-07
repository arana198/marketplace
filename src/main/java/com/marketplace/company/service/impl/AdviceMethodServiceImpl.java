package com.marketplace.company.service.impl;

import com.marketplace.company.dto.AdviceResponse;
import com.marketplace.company.service.AdviceMethodService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
class AdviceMethodServiceImpl implements AdviceMethodService {

    private final AdviceMethodRepository adviceRepository;
    private final AdviceMethodResponseConverter adviceResponseConverter;

    @Override
    public List<AdviceResponse> getAdviceMethods() {
        return adviceRepository.findByActive(true)
                .parallelStream()
                .map(adviceResponseConverter::convert)
                .collect(Collectors.toList());
    }
}
