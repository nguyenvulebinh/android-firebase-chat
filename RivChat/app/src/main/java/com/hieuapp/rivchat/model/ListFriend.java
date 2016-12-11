package com.hieuapp.rivchat.model;

import com.hieuapp.rivchat.MainActivity;
import com.hieuapp.rivchat.data.StaticConfig;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nguyenbinh on 08/12/2016.
 */

public class ListFriend {
    private ArrayList<Friend> listFriend;

    public ArrayList<Friend> getListFriend() {
        return listFriend;
    }

    public ListFriend(){
        listFriend = new ArrayList<>();
    }

    public String getAvataById(String id){
        for(Friend friend: listFriend){
            if(id.equals(friend.id)){
                return friend.avata;
            }
        }
        return "";
    }

    public void setListFriend(ArrayList<Friend> listFriend) {
        this.listFriend = listFriend;
    }
}
