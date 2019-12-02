package com.qian.demo.entity;

import org.springframework.context.annotation.Bean;


public class User {
    private Integer id;
    private String userNo;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userNo='" + userNo + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}