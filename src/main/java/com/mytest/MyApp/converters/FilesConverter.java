package com.mytest.MyApp.converters;

import com.mytest.MyApp.dto.FilesDTO;
import com.mytest.MyApp.models.MyFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FilesConverter{
    FilesConverter INSTANCE = Mappers.getMapper( FilesConverter.class );
   @Mapping(target = "fileSize", expression = "java(myFile.getData().length/1024)")
    public FilesDTO EntityToFileDTO(MyFile myFile);
}
