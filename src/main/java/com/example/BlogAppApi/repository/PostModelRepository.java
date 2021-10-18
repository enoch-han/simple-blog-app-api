package com.example.BlogAppApi.repository;

import com.example.BlogAppApi.models.GroupModel;
import com.example.BlogAppApi.models.PostModel;
import com.example.BlogAppApi.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostModelRepository extends JpaRepository<PostModel,Long> {
    List<PostModel> findPostModelsByGroup(GroupModel groupModel);
    List<PostModel> findPostModelsByUser(UserModel userModel);
    PostModel findPostModelById(Long id);
    List<PostModel> findPostModelsByGroupOrderByCreatedAt(GroupModel groupModel);
}
