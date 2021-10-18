package com.example.BlogAppApi.repository;

import com.example.BlogAppApi.models.GroupModel;
import com.example.BlogAppApi.models.GroupRequesModel;
import com.example.BlogAppApi.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRequestModelRepository extends JpaRepository<GroupRequesModel,Long> {
     List<GroupRequesModel> findGroupRequesModelsByGroup(GroupModel groupModel);
     List<GroupRequesModel> findGroupRequesModelsByUser(UserModel userModel);
     GroupRequesModel findGroupRequesModelById(Long id);
     void deleteGroupRequesModelByUser(UserModel userModel);
}
