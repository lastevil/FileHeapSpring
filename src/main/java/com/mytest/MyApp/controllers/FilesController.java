package com.mytest.MyApp.controllers;

import com.mytest.MyApp.dto.FilesDTO;
import com.mytest.MyApp.exeptions.AppError;
import com.mytest.MyApp.models.MyFile;
import com.mytest.MyApp.services.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FilesController {
    private final FilesService filesService;

    @GetMapping()
    Page<FilesDTO> getFilePage(@RequestParam(name = "page", defaultValue = "1") Integer page, @HeadersSecurityMarker UsernamePasswordAuthenticationToken token) {
        log.info("USER : " + token.toString());
        return filesService.getFiles(page, token.getPrincipal().toString());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable Long id) {
        MyFile file = filesService.findById(id);
        if (file == null)
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "File note exist"), HttpStatus.NOT_FOUND);
        else {
            byte[] data = file.getData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(file.getFileType()));
            ContentDisposition build = ContentDisposition
                    .builder("attachment")
                    .filename(file.getFileName())
                    .build();
            headers.setContentDisposition(build);

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    void deleteFile(@PathVariable Long id) {
        filesService.deleteFile(id);
    }

    @PostMapping()
    FilesDTO addFile(@RequestPart("data") MultipartFile file, @HeadersSecurityMarker UsernamePasswordAuthenticationToken token) throws IOException {
        return filesService.save(file.getOriginalFilename(), file.getContentType(), file.getBytes(), token.getPrincipal().toString());
    }
}

