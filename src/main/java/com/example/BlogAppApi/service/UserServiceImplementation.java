package com.example.BlogAppApi.service;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.BlogAppApi.exceptions.DuplicateUserException;
import com.example.BlogAppApi.models.*;
import com.example.BlogAppApi.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @Transactional @Slf4j @RequiredArgsConstructor
public class UserServiceImplementation implements UserService,GroupService, UserDetailsService {

    private final UserModelRepository userModelRepository;
    private final RoleModelRepository roleModelRepository;
    private final GroupModelRepository groupModelRepository;
    private final PostModelRepository postModelRepository;
    private final GroupRequestModelRepository groupRequestModelRepository;
    private final PasswordEncoder passwordEncoder;


//    UserServiceImplementation(UserModelRepository userModelRepository,RoleModelRepository roleModelRepository,GroupModelRepository groupModelRepository,PostModelRepository postModelRepository,GroupRequestModelRepository groupRequestModelRepository){
//        this.userModelRepository = userModelRepository;
//        this.roleModelRepository = roleModelRepository;
//        this.groupModelRepository = groupModelRepository;
//        this.postModelRepository = postModelRepository;
//        this.groupRequestModelRepository = groupRequestModelRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        /*
        * A method that defines how to get the user data for the purpose
        * of authentication
        * */

        UserModel userModel = userModelRepository.findUserModelByUserName(userName);
        if(userModel == null){
            System.out.println("user not found for authentication");
            throw new UsernameNotFoundException("user not found in the database for authentication");
        }else {
            System.out.println("user is found in database for authentication");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userModel.getRoles().forEach(roleModel -> {
            authorities.add(new SimpleGrantedAuthority(roleModel.getName()));
        });
        return new User(userModel.getUserName(),userModel.getPassword(),authorities);
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        /*
        * checks if a user exists with the same username is so throws an exception and retruns null
        * but if not saves the user to the database and returns user model object
        */

        UserModel checkUser = userModelRepository.findUserModelById(userModel.getId());
        UserModel returnedUser;
        System.out.println(checkUser);
        try {
            if(checkUser == null){
                userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                returnedUser= userModelRepository.save(userModel);
                System.out.println("user saved to the database successfully");
            }else{
                returnedUser = null;
                throw new DuplicateUserException("There is another user with the same user name so please try to use another user name");
            }
        }catch (DuplicateUserException e){
            returnedUser = null;
            System.out.println(e.getMessage());
        }
        return returnedUser;
    }

    @Override
    public RoleModel saveRole(RoleModel roleModel) {
        /*
        * checks if role is already existing in the database if so returns an exception,
        * but if not saves the role and returns role model
        * */

        RoleModel checkRole = roleModelRepository.findRoleModelByName(roleModel.getName());
        RoleModel returnedRole;
        try {
            if(checkRole == null){
                returnedRole = roleModelRepository.save(roleModel);
                System.out.println("role saved to database successfully");
            }else{
                returnedRole = null;
                throw new Exception("role already exists no need to save it again");
            }
        }catch (Exception e){
            returnedRole = null;
            System.out.println(e.getMessage());
        }
        return returnedRole;
    }

    @Override
    public boolean addRoleToUser(String userName, String roleName) {
        /*
        * checks if the user exists and the role exists if not throws the exception
        * with the respective messages, but if the user and the role is found it adds
        * the role to the user and returns true
        * */

        boolean status = false;
        UserModel checkUser = userModelRepository.findUserModelByUserName(userName);
        RoleModel checkRole = roleModelRepository.findRoleModelByName(roleName);
        try{
            if(checkUser!= null){
                if(roleName != null){
                    checkUser.getRoles().add(checkRole);
                    status = true;
                }else{
                    throw new Exception("no such role exists, add the role first");
                }
            }else {
                throw new Exception("no user by this name exists in the database, add the user first");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return status;
    }

    @Override
    public UserModel getUser(Long id) {

        /*
        * searches the database and returns the desired user if found,
        * if not throws an exception
        * */

        UserModel checkUser = userModelRepository.findUserModelById(id);
        try{
            if (checkUser == null){
                throw new Exception("No user by this id is found");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return checkUser;
    }

    @Override
    public List<UserModel> getUsers() {

        /*
         * searches the database and returns all users if found,
         * if not throws an exception
         * */

        List<UserModel> checkUsers = userModelRepository.findAll();
        try{
            if(checkUsers.isEmpty()){
                throw new RuntimeException("There are no users currently in the database");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return checkUsers;
        }finally {
            return checkUsers;
        }
    }

    @Override
    public GroupModel saveGroup(GroupModel groupModel) {

        /*
        * searches the database for existing group with the same id,
        * id found throws and exception and returns null,
        * id not saves the group to the database
        * */

        GroupModel checkGroup = groupModelRepository.findGroupModelById(groupModel.getId());
        GroupModel returnedGroup;
        try {
            if (checkGroup != null){
                returnedGroup = null;
                throw new Exception("A group already exists by this id join the group not create");
            }else {
                returnedGroup = groupModelRepository.save(groupModel);
                System.out.println("A new group added to the database");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            returnedGroup = null;
        }
        return returnedGroup;
    }

    @Override
    public PostModel savePost(PostModel postModel) {
        /*
        * checks the database if there is a user by this id,
        * id found throws and exception,
        * id not saves to the database
        * */
        PostModel checkPost = postModelRepository.findPostModelById(postModel.getId());
        PostModel returnedPost;
        try{
            if(checkPost != null){
                returnedPost= null;
                throw new Exception("A post by this id already exists add other post");
            }else {
                returnedPost = postModelRepository.save(postModel);
                System.out.println("A new post added to the database");
            }
        }catch (Exception e){
            returnedPost = null;
            System.out.println(e.getMessage());
        }
        return returnedPost;
    }

    @Override
    public GroupRequesModel saveRequest(GroupRequesModel groupRequesModel) {
        /*
         * checks the database if there is a group request by this id,
         * id found throws and exception,
         * id not saves to the database
         * */
        GroupRequesModel checkModel = groupRequestModelRepository.findGroupRequesModelById(groupRequesModel.getId());
        GroupRequesModel returnedRequest;
        try {
            if(checkModel != null){
                returnedRequest = null;
                throw new Exception("A group request by this id already exists");
            }else{
                returnedRequest = groupRequestModelRepository.save(groupRequesModel);
                System.out.println("Group request added to the database");
            }
        }catch (Exception e){
            returnedRequest =null;
            System.out.println(e.getMessage());
        }
        return returnedRequest;
    }

    @Override
    public boolean addMemberToGroup(UserModel userModel,GroupModel groupModel) {
        /*
        * searches the database for requests specific to this group checks if empty
        * if so returns exception if not searches for the user in the list if found
        * and is approved adds to the members of the of the group
        * */
        boolean status = false;
        //searches the database for requests for this group
        List<GroupRequesModel> requestToThisGroup = groupRequestModelRepository.findGroupRequesModelsByGroup(groupModel);
        try {
            //check if list is empty
            if(!requestToThisGroup.isEmpty()){
                //checks if specific user is found and is approved
                requestToThisGroup.removeIf(request->request.getUser()!=userModel && request.isApproval());
                //adds the user to members
                if (!requestToThisGroup.isEmpty()){
                    status = true;
                    requestToThisGroup.forEach(request->{
                        groupModel.getMembers().add(userModel);
                    });
                }
            }else {
                throw new Exception("No requests sent to this group");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return status;
    }

    boolean checkMember(GroupModel groupModel,UserModel userModel){
        if (groupModel.getMembers().contains(userModel)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addPostToGroup(PostModel postModel) {
        /*
        * searches the database for the group if found,
        * checks if user is a member if user is a member
        * saves the post to database and returns true
        * if not throws and exception
        * */
        boolean status = false;
        //searches the database for all the specific group
        GroupModel Group = groupModelRepository.findGroupModelById(postModel.getGroup().getId());
        try {
            //checks if group found
            if (postModel.getGroup() != null){
                //checks if user is a member
                if (checkMember(postModel.getGroup(),postModel.getUser())){
                    savePost(postModel);
                    status = true;
                }else {
                    throw new Exception("The user is not a member of this group send a request");
                }
            }else {
                throw new Exception("No group by this name ");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return status;
    }

    @Override
    public boolean addRequestToGroup(GroupRequesModel groupRequesModel) {
        /*
        * checks the database if for the group if found,
        * adds the request to its requests field
        * */

        boolean status = false;
        GroupRequesModel checkRequest = groupRequestModelRepository.findGroupRequesModelById(groupRequesModel.getId());
        GroupModel checkGroup = groupModelRepository.findGroupModelById(groupRequesModel.getGroup().getId());
        try {
            if (checkGroup != null){
                if (checkRequest != null){
                    groupRequesModel.getGroup().getRequests().add(groupRequesModel);
                    status = true;
                    System.out.println("request added to group successfully");
                }else {
                    throw new Exception("No request found it the database");
                }
            }else {
                throw new Exception("No group find by this name");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return status;
    }

    public boolean deleteRequestFromGroup(GroupRequesModel groupRequesModel){
        /*
        * checks if request exists then if it is found in the groups request field
        * if so deletes it if not throws and exception
        * */
        boolean status = false;
        GroupRequesModel checkRequest = groupRequestModelRepository.findGroupRequesModelById(groupRequesModel.getId());
        try {
            //checks if request exists
            if(checkRequest != null){
                boolean status2 = groupRequesModel.getGroup().getRequests().contains(groupRequesModel);
                //checks if request exists in the group
                if(status2){
                    groupRequesModel.getGroup().getRequests().remove(groupRequesModel);
                    boolean status3 = groupRequesModel.getGroup().getRequests().remove(groupRequesModel);
                    //checks if deletion is successful
                    if(status){
                        throw new Exception(" deletion from group unsuccessful");
                    }else {
                        status = true;
                    }
                }else {
                    throw new Exception("request is not found in group: {}" + groupRequesModel.getGroup().getName());
                }
            }else {
                throw new Exception("no request found by this id {}" + groupRequesModel.getId());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return status;
        }
    }

    @Override
    public List<GroupModel> getGroups() {
        /*
        * returns all groups found in the database
        * */
        List<GroupModel> returnedGroups = groupModelRepository.findAll();
        try {
            if(!returnedGroups.isEmpty()){
                System.out.println("fetching all groups from the database");
            }else {
                throw new Exception("no groups in the database");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return returnedGroups;
        }
    }

    @Override
    public List<PostModel> getPosts() {
        /*
         * returns all posts found in the database
         * */
        List<PostModel> returnedPosts = postModelRepository.findAll();
        try {
            if(!returnedPosts.isEmpty()){
                System.out.println("fetching all posts from the database");
            }else {
                throw new Exception("no posts in the database");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return returnedPosts;
        }
    }

    @Override
    public List<GroupRequesModel> getRequests() {
        /*
         * returns all requests found in the database
         * */
        List<GroupRequesModel> returnedRequests = groupRequestModelRepository.findAll();
        try {
            if(!returnedRequests.isEmpty()){
                System.out.println("fetching all requests from the database");
            }else {
                throw new Exception("no requests in the database");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return returnedRequests;
        }
    }

    @Override
    public boolean Approve(UserModel approver,GroupRequesModel request){
        /*
        * checks the the existence of the user, the group, and the request if all true
        * then checks is the approver is the admin if it is adds approves the reques
        * and adds the user to the group as a member
        * */
        boolean status = false;
        //fetches the data from database
        GroupRequesModel checkRequest = groupRequestModelRepository.findGroupRequesModelById(request.getId());
        UserModel checkUser = userModelRepository.findUserModelById(approver.getId());
        GroupModel checkGroup = groupModelRepository.findGroupModelById(request.getGroup().getId());
        try {
            //checks if request exists
            if(checkRequest != null){
                try {
                    //checks if user exists
                    if (checkUser != null){
                        try {
                            //checks if group exists
                            if(checkGroup != null){
                                try{
                                    //checks if the user is an admin of the group
                                    if(request.getGroup().getAdmin().getId() == approver.getId()){
                                        //approves the requests
                                        request.setApproval(true);
                                        //adds the user to the members of the group
                                        request.getGroup().getMembers().add(request.getUser());
                                        status = true;
                                    }else {
                                        throw new Exception("The user is not the admin therefore can't approve request");
                                    }
                                }catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                            }else {
                                throw new Exception("No group by this id found");
                            }
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }else {
                        throw new Exception("No user found by this id");
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }else {
                throw new Exception("No request found by the provided id  in the database");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return status;
    }

    @Override
    public GroupModel getGroup(Long id){
        GroupModel checkGroup = groupModelRepository.findGroupModelById(id);
        try {
            if(checkGroup != null){
                System.out.println("fetching group successful");
            }else {
                throw new Exception("NO user found by provided id: {}" + id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return checkGroup;
    }

    @Override
    public PostModel getPost(Long id){
        PostModel checkPost = postModelRepository.findPostModelById(id);
        try {
            if(checkPost != null){
                System.out.println("fetching post successful");
            }else {
                throw new Exception("NO post found by provided id: {}" + id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return checkPost;
    }

    @Override
    public GroupRequesModel getRequest(Long id){
        GroupRequesModel checkRequest = groupRequestModelRepository.findGroupRequesModelById(id);
        try {
            if(checkRequest != null){
                System.out.println("fetching request successful");
            }else {
                throw new Exception("NO request found by provided id: {}" + id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return checkRequest;
    }

//    public List<GroupModel> groupsUserIsMemberTo(UserModel userModel){
//        /*
//        * checks the database and returns the groups in which the
//        * user is member to
//        * */
//        List<GroupModel> checkGroup = getGroups();
//        List<GroupModel> returnedGroup = new ArrayList<GroupModel>();
//        try {
//            //checks if groups exist
//            if (checkGroup != null){
//                checkGroup.forEach(group->{
//                    //checks if user is member of the group
//                    if(group.getMembers().contains(userModel)) {
//                        returnedGroup.add(group);
//                    }
//                });
//            }else{
//                throw new Exception("No groups in the database");
//            }
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return returnedGroup;
//    }

    @Override
    public List<PostModel> getPostsByGroup(GroupModel groupModel){
        /*
        * fetches the database for posts respective to the groups
        * and provides if cant find throws exception
        * */
        List<PostModel> checkPosts = postModelRepository.findPostModelsByGroupOrderByCreatedAt(groupModel);
        try {
            if (checkPosts != null){
                System.out.println("posts provided successfully");
            }else {
                throw new Exception("No posts found in group");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return checkPosts;
        }

    }

    @Override
    public List<GroupRequesModel> getRequestByUser(UserModel userModel){
        /*
        * checks the database if there are any requests sent by the provided
        * user and if found returns the list if not throws and exception
        * */

        List<GroupRequesModel> checkRequests = groupRequestModelRepository.findGroupRequesModelsByUser(userModel);
        try{
            if(!checkRequests.isEmpty()){
                System.out.println("fetching requests by user : {}" + userModel.getUserName());
            }else {
                throw new Exception("No requests sent by this user");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return checkRequests;
        }
    }

    @Override
    public boolean deleteRequestByUser(GroupRequesModel groupRequesModel){
        /*
        * checks if request exists then if found deletes it from database
        * and from the groups requests field
        * */
        boolean status = false;
        GroupRequesModel checkRequest = groupRequestModelRepository.findGroupRequesModelById(groupRequesModel.getId());
        try {
            if(checkRequest!= null){
                groupRequestModelRepository.deleteGroupRequesModelByUser(groupRequesModel.getUser());
                GroupRequesModel checkDelete = groupRequestModelRepository.findGroupRequesModelById(groupRequesModel.getId());
                if(checkDelete == null && deleteRequestFromGroup(groupRequesModel)){
                    status = true;
                    System.out.println("deletion of the request is successful");
                }
                else {
                    throw new Exception("deletion unsuccessful");
                }
            }else {
                throw new Exception("no request found by this id : {}" + groupRequesModel.getId());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            return status;
        }
    }

    @Override
    public List<GroupModel> getGroupsUserIsMemberTo(UserModel userModel){
        List<GroupModel> checkGroups = groupModelRepository.findGroupModelsByMembersContains(userModel);
        try {
            if (checkGroups != null){
                System.out.println("fetching groups that a user is member at");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            return checkGroups;
        }
    }

}
