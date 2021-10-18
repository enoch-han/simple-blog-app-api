package com.example.BlogAppApi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<RoleModel> roles = new ArrayList<RoleModel>();

    //constructor
    public UserModel(String userName, String password, String email){
        this.id = null;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.roles = new ArrayList<RoleModel>();

    }
/*
    UserModel(Long id,String userName,String password,String email,LocalDateTime createdAt,Collection<RoleModel> roleModels){
        setId(id);
        setUserName(userName);
        setPassword(password);
        setEmail(email);
        setCreatedAt(createdAt);
        setRoles(roleModels);
    }
    UserModel(){}*/


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Collection<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleModel> roles) {
        this.roles = roles;
    }

}
