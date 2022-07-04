package com.mytest.MyApp.services;

import com.mytest.MyApp.converters.FilesConverter;
import com.mytest.MyApp.dto.FilesDTO;
import com.mytest.MyApp.models.MyFile;
import com.mytest.MyApp.models.User;
import com.mytest.MyApp.repositorys.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final FileRepository fileRepository;
    private final UserService userService;
    @Autowired
    private FilesConverter converter;
    public Page<FilesDTO> getFiles(Integer page, String username) {
        Specification<MyFile> fileSpecification = Specification.where(null);
        User user = userService.findByUsername(username).orElse(null);
        if (user!=null){
            Pageable pageable = PageRequest.of(page-1,20);
       return fileRepository.findAllByUserId(pageable, user.getId())
                .map(converter::EntityToFileDTO);
        }
        else {
            return null;
        }
    }

    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    @Transactional
    public FilesDTO save(String originalFilename, String contentType, byte[] bytes, String username) {
        User user = userService.findByUsername(username).orElse(null);
        MyFile file = new MyFile();
        file.setFileName(originalFilename);
        file.setFileType(contentType);
        file.setData(bytes);
        file.setUser(user);
        fileRepository.save(file);
        return converter.EntityToFileDTO(file);
    }

    public MyFile findById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }
}
