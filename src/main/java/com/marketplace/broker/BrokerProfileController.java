package com.marketplace.broker;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.common.exception.ResourceAlreadyExistsException;
import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.common.security.UserRole;
import com.marketplace.company.dto.CompanyRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.service.CompanyService;
import com.marketplace.utils.PagedResourceConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;

@Data
@Slf4j
@Controller
@RequestMapping("/companies")
public class BrokerProfileController {


}
