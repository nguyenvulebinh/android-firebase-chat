package com.hieuapp.rivchat.model;

import com.hieuapp.rivchat.MainActivity;

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
        //Test init 10 item
        listFriend = new ArrayList<>();
//        Random random = new Random(System.currentTimeMillis());
//        for(int i = 0; i <10; i++){
//            User temp = new User();
//            temp.name ="Nguyen Binh";
//            temp.message.text = "Chao ngay moi";
//            temp.message.timestamp = 12;
//            temp.status.isOnline = (random.nextInt() % 2 == 0);
//            temp.avata = MainActivity.STR_DEFAULT_BASE64;
//            listFriend.add(temp);
//        }
    }

    public void setListFriend(ArrayList<Friend> listFriend) {
        this.listFriend = listFriend;
    }
}
