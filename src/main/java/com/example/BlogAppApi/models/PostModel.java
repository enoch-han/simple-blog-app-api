package com.example.BlogAppApi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private GroupModel group;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserModel user;
    private String message;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupModel getGroupId() {
        return group;
    }

    public void setGroupId(GroupModel group) {
        group = group;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
