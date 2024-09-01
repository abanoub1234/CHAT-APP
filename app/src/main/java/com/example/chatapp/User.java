package com.example.chatapp;

public class User
{
    private String username;
    private String email;
    private String profilePicture;

    public User(String username, String email, String profilePicture)
    {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public User()
    {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
