package com.example.java.listeners;

import com.example.vshare.Model.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);
    void initiateAudioMeeting(User user);
}
