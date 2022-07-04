package com.mytest.MyApp.dto;

import lombok.Data;

@Data
public class FilesDTO {
    private Long id;

    private String fileName;

    private String fileType;

    private Integer fileSize;
}
