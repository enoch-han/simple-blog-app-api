package com.example.BlogAppApi.service;

import com.example.BlogAppApi.models.*;

import java.util.List;

public interface UserService {
    UserModel saveUser(UserModel userModel);
    RoleModel saveRole(RoleModel roleModel);
    boolean addRoleToUser(String userName,String roleName);
    UserModel getUser(Long id);
    List<UserModel> getUsers();
    boolean Approve(UserModel approver, GroupRequesModel request);
    GroupModel getGroup(Long id);
    PostModel getPost(Long id);
    GroupRequesModel getRequest(Long id);
    List<PostModel> getPostsByGroup(GroupModel groupModel);
    List<GroupRequesModel> getRequestByUser(UserModel userModel);
    boolean deleteRequestByUser(GroupRequesModel groupRequesModel);
    List<GroupModel> getGroupsUserIsMemberTo(UserModel userModel);

}
