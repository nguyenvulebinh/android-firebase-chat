package com.hieuapp.rivchat.model;

/**
 * Created by nguyenbinh on 11/12/2016.
 */

public class Group extends Room{
    public String id;
    public ListFriend listFriend;

    public Group(){
        listFriend = new ListFriend();
    }
}
