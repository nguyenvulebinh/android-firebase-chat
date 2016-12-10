package com.hieuapp.rivchat.model;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class User {
    public String name;
    public String email;
    public String avata;
    public Status status;
    public Message message;


    public User(){
        status = new Status();
        message = new Message();
        status.isOnline = true;
        status.timestamp = System.currentTimeMillis();
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;
    }
}
