package com.mytest.MyApp.repositorys;

import com.mytest.MyApp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Collection<Role> findAllById(long l);
}
