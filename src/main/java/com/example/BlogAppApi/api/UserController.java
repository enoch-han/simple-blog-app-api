package com.example.BlogAppApi.api;


import com.example.BlogAppApi.models.GroupModel;
import com.example.BlogAppApi.models.GroupRequesModel;
import com.example.BlogAppApi.models.PostModel;
import com.example.BlogAppApi.models.UserModel;
import com.example.BlogAppApi.service.UserServiceImplementation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogapp/api")
@Data
public class UserController {
    private final UserServiceImplementation userServiceImplementation;
    UserController(UserServiceImplementation userServiceImplementation){
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getUsers(){
        List<UserModel> checkUsers = userServiceImplementation.getUsers();
        if(!checkUsers.isEmpty()){
            return ResponseEntity.ok().body(checkUsers);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(checkUsers);
        }
    }

    @PostMapping(value = "/user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUser(@RequestBody Long id){
        //groups allowed should be removed from user
        //projects should be uploaded to git
        System.out.println("check 1");
        UserModel checkUser = null;
            if (id != null){
                checkUser = userServiceImplementation.getUser(id);
                if (checkUser != null){
                    return ResponseEntity.ok().body(checkUser);
                }else {
                    return ResponseEntity.ok().body(checkUser);
                }
            }else {
                System.out.println("empty string");
                return ResponseEntity.ok().body(checkUser);
            }

    }

    @PostMapping(value = "/postsfromgroup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostModel>> getPostFromGroup(@RequestBody GroupModel groupModel){
        List<PostModel> checkPosts = userServiceImplementation.getPostsByGroup(groupModel);
        if (checkPosts ==  null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkPosts);
        }else{
            return ResponseEntity.ok().body(checkPosts);
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupModel>> getGroups(){
        List<GroupModel> checkGroup = userServiceImplementation.getGroups();
        if (checkGroup == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkGroup);
        }else {
            return ResponseEntity.ok().body(checkGroup);
        }
    }

    @PostMapping(value = "/group",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupModel> getGroup(@RequestBody Long id){
        GroupModel checkGroup = userServiceImplementation.getGroup(id);
        if (checkGroup == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkGroup);
        }else {
            return ResponseEntity.ok().body(checkGroup);
        }
    }

    @PostMapping(value = "/requestsfromuser",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupRequesModel>> getRequestsFromGroup(@RequestBody UserModel userModel){
        List<GroupRequesModel> checkRequest = userServiceImplementation.getRequestByUser(userModel);
        if (checkRequest == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkRequest);
        }else {
            return ResponseEntity.ok().body(checkRequest);
        }
    }

    @PostMapping(value = "/deleterequest",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteRequestByUser(@RequestBody GroupRequesModel groupRequesModel){
        boolean status = userServiceImplementation.deleteRequestByUser(groupRequesModel);
        if (status){
            return ResponseEntity.ok().body(status);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(status);
        }
    }

    @PostMapping(value = "/sendrequest",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sendRequest(@RequestBody GroupRequesModel groupRequesModel){
        userServiceImplementation.saveRequest(groupRequesModel);
        boolean status = userServiceImplementation.addRequestToGroup(groupRequesModel);
        if (status){
            return ResponseEntity.ok().body(status);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(status);
        }
    }

    @PostMapping(value = "/groupsbyuser",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupModel>> getGroupsByUser (@RequestBody UserModel userModel){
        List<GroupModel> checkGroups = userServiceImplementation.getGroupsUserIsMemberTo(userModel);
        if(checkGroups != null){
            return ResponseEntity.ok().body(checkGroups);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkGroups);
        }
    }

    @PostMapping(value = "/savepost",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> savePost(@RequestBody PostModel postModel){
        boolean status = userServiceImplementation.savePost(postModel)!=null?true:false;
        if (status){
            return ResponseEntity.ok().body(status);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(status);
        }
    }

    @PostMapping(value = "/acceptrequest",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> acceptRequest(@RequestBody GroupRequesModel groupRequesModel,UserModel userModel){
        boolean status = userServiceImplementation.Approve(userModel,groupRequesModel);
        if (status){
            return ResponseEntity.ok().body(status);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(status);
        }
    }
}
