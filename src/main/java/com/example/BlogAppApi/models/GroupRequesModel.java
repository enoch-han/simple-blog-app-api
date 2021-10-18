package com.example.BlogAppApi.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private GroupModel group;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserModel user;
    private boolean approval;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
