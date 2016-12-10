package com.hieuapp.rivchat.model;

import java.util.ArrayList;

/**
 * Created by nguyenbinh on 10/12/2016.
 */

public class Consersation {
    private ArrayList<Message> listMessageData;
    public Consersation(){
        listMessageData = new ArrayList<>();
    }

    public ArrayList<Message> getListMessageData() {
        return listMessageData;
    }
}
