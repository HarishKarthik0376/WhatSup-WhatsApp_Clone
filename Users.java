package com.example.whatsup.Models;

public class Users {
    String profilepic;
    String usernaame;
    String email;
    String password;
    String status;
    String userid;
    String lastmessage;

    public Users(String profilepic, String usernaame, String email, String password, String status, String userid, String lastmessage) {
        this.profilepic = profilepic;
        this.usernaame = usernaame;
        this.email = email;
        this.password = password;
        this.status = status;
        this.userid = userid;
        this.lastmessage = lastmessage;
    }

    public Users(String usernaame, String email, String password) {
        this.usernaame = usernaame;
        this.email = email;
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsernaame() {
        return usernaame;
    }

    public void setUsernaame(String usernaame) {
        this.usernaame = usernaame;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public Users()
    {

    }
}
