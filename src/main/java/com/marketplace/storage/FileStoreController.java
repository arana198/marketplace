package com.marketplace.storage;

import com.marketplace.common.security.AuthUser;
import com.marketplace.storage.dto.FileResponse;
import com.marketplace.storage.exception.FileNotFoundException;
import com.marketplace.storage.service.FileStoreService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Data
@Slf4j
@Controller
@RequestMapping("/documents")
public class FileStoreController {

    private final FileStoreService fileStoreService;

    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<FileResponse> downloadDocument(@PathVariable final String fileId) throws IOException, FileNotFoundException {

        final FileResponse document = fileStoreService.findById(AuthUser.getUserId(), fileId)
                .orElseThrow(() -> new FileNotFoundException(fileId));

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, document.getFormat());
        responseHeaders.add(HttpHeaders.CONTENT_LENGTH, Long.toString(document.getFile().length));

        return new ResponseEntity(document.getFile(), responseHeaders, HttpStatus.OK);
    }
}
