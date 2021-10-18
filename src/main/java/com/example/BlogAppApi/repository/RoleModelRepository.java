package com.example.BlogAppApi.repository;

import com.example.BlogAppApi.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleModelRepository extends JpaRepository<RoleModel,Long> {
    RoleModel findRoleModelByName(String name);
}
