package com.marketplace.company;

import com.marketplace.company.dto.AdviceResponse;
import com.marketplace.company.service.AdviceMethodService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/advices")
public class AdviceMethodController {

  private final AdviceMethodService adviceMethodService;

  @GetMapping
  public ResponseEntity<List<AdviceResponse>> getAdvices() {

    List<AdviceResponse> response = adviceMethodService.getAdviceMethods();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
