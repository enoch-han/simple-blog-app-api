package com.example.BlogAppApi.service;

import com.example.BlogAppApi.models.GroupModel;
import com.example.BlogAppApi.models.GroupRequesModel;
import com.example.BlogAppApi.models.PostModel;
import com.example.BlogAppApi.models.UserModel;

import java.util.List;

public interface GroupService {
    GroupModel saveGroup(GroupModel groupModel);
    PostModel savePost(PostModel postModel);
    GroupRequesModel saveRequest(GroupRequesModel groupRequesModel);
    boolean addMemberToGroup(UserModel userModel,GroupModel groupModel);
    boolean addPostToGroup(PostModel postModel);
    boolean addRequestToGroup(GroupRequesModel groupRequesModel);
    List<GroupModel> getGroups();
    List<PostModel> getPosts();
    List<GroupRequesModel> getRequests();

}
