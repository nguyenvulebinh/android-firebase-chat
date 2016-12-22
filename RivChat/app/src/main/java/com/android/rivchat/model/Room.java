package com.android.rivchat.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Room {
    public ArrayList<String> member;
    public Map<String, String> groupInfo;

    public Room(){
        member = new ArrayList<>();
        groupInfo = new HashMap<String, String>();
    }
}
