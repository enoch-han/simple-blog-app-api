package com.example.BlogAppApi.repository;

import com.example.BlogAppApi.models.GroupModel;
import com.example.BlogAppApi.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupModelRepository extends JpaRepository<GroupModel,Long> {
    GroupModel findGroupModelByName(String name);
    GroupModel findGroupModelById(Long id);
    List<GroupModel> findGroupModelsByMembersContains(UserModel userModel);
}
