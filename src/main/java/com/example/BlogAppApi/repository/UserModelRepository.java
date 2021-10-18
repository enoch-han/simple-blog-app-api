package com.example.BlogAppApi.repository;

import com.example.BlogAppApi.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel,Long> {
    UserModel findUserModelByUserName(String userName);
    UserModel findUserModelById(Long id);
}
