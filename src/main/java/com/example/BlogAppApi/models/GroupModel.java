package com.example.BlogAppApi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private UserModel admin;
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<UserModel> members = new ArrayList<UserModel>();
    private LocalDateTime createdAt;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<PostModel> posts = new ArrayList<PostModel>();
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<GroupRequesModel> requests = new ArrayList<GroupRequesModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<UserModel> getMembers() {
        return members;
    }

    public void setMembers(Collection<UserModel> members) {
        this.members = members;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Collection<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(Collection<PostModel> posts) {
        this.posts = posts;
    }

    public Collection<GroupRequesModel> getRequests() {
        return requests;
    }

    public void setRequests(Collection<GroupRequesModel> requests) {
        this.requests = requests;
    }

    public UserModel getAdmin() {
        return admin;
    }

    public void setAdmin(UserModel admin) {
        this.admin = admin;
    }
}
