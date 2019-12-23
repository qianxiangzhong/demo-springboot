package com.qian.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: qxz
 * @datetime : 2019/12/18 18:34
 **/
public class School {

    private Integer id;
    private String name;
    private LocalDate buildDate;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(LocalDate buildDate) {
        this.buildDate = buildDate;
    }

    public LocalDateTime getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(LocalDateTime insertTime) {
        this.insertTime = insertTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", buildDate=" + buildDate +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                '}';
    }
}