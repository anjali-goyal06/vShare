package com.example.vshare.Model;

public class User {

    String profilePic;
    public String email;
    public String name;
    String password;
    public String token;
    public String userId;


    public User(){};

    public User(String name,String emailId,String password){
        this.email = emailId;
        this.name = name;
        this.password = password;
    }


    public User(String profilePic, String emailId, String name, String password, String userId, String lastMessage, String summary) {
        this.profilePic = profilePic;
        this.email = emailId;
        this.name = name;
        this.password = password;
        this.userId = userId;

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmailId() {
        return email;
    }

    public void setEmailId(String emailId) {
        this.email = emailId;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
