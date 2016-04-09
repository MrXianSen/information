package com.albery.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Albery
 * @version 1.0
 *          <p>
 *          *************************
 *          描述用户的实体类
 *          *************************
 */
public class User implements Serializable {

    private int head;
    private String id;
    private String name;
    private String password;
    private String gender;
    private Date birthday;
    private String school;
    private String sign = "";

    public User(int head, String name, String gender) {
        this.head = head;
        this.name = name;
        this.gender = gender;
    }

    public User(int head, String name, String school,
                String gender, String sign) {
        this.head = head;
        this.name = name;
        this.school = school;
        this.gender = gender;
        this.sign = sign;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
