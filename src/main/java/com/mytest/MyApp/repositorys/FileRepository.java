package com.mytest.MyApp.repositorys;

import com.mytest.MyApp.models.MyFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface FileRepository extends JpaRepository<MyFile, Long>, JpaSpecificationExecutor<MyFile> {
    Page<MyFile> findAllByUserId(Pageable pageable, Long id);
}
