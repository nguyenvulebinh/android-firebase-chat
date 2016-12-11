package com.hieuapp.rivchat.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class Room {
    public ArrayList<String> member;
    public Map<String, String> groupInfo;

    public Room(){
        member = new ArrayList<>();
        groupInfo = new HashMap<String, String>();
    }
}
