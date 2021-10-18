package com.example.BlogAppApi.api;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.BlogAppApi.models.*;
import com.example.BlogAppApi.service.UserServiceImplementation;
import com.example.BlogAppApi.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/blogapp/api")
@Data
public class UserController {
    private final UserServiceImplementation userServiceImplementation;
    private final Constants constants = new Constants();
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

    @PostMapping(value = "/refreshtoken",produces = MediaType.APPLICATION_JSON_VALUE)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        * a method to get an access token by using the refresh token
        * once the access token expires which lasts for 30 minutes
        * another access token is generated and given to requester
        * but to do that it must send the refresh token which will
        * not expire for about 10 days
        * */
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        //checks if the request authorization header exists and have the string owner before it
        if (authorizationHeader != null && authorizationHeader.startsWith("owner ")){
            try {
                String refresh_token = authorizationHeader.substring("owner ".length());
                Algorithm algorithm = constants.getAlgorithm();
                JWTVerifier verifier = JWT.require(algorithm).build();
                //decodes the rest of the token after removing the owner key
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String userName = decodedJWT.getSubject();
                UserModel user = userServiceImplementation.getUserByName(userName);
                //generation of the access token
                String access_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(RoleModel::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e){
                response.setHeader("error",e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String,String> message = new HashMap<>();
                message.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),message);
            }
        } else{
            throw new RuntimeException("refresh token not found send it please");
        }
    }
}
