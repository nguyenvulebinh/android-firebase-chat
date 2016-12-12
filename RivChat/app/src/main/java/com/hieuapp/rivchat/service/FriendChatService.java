package com.hieuapp.rivchat.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hieuapp.rivchat.R;
import com.hieuapp.rivchat.data.FriendDB;
import com.hieuapp.rivchat.data.GroupDB;
import com.hieuapp.rivchat.model.Friend;
import com.hieuapp.rivchat.model.Group;
import com.hieuapp.rivchat.model.ListFriend;
import com.hieuapp.rivchat.ui.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nguyenbinh on 12/12/2016.
 */

public class FriendChatService extends Service {
    private static String TAG = "FriendChatService";
    // Binder given to clients
    public final IBinder mBinder = new LocalBinder();
    public Map<String, Boolean> mapMark;
    public Map<String, Query> mapQuery;
    public Map<String, ChildEventListener> mapChildEventListenerMap;
    public ArrayList<String> listKey;
    public ListFriend listFriend;
    public ArrayList<Group> listGroup;

    public FriendChatService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mapMark = new HashMap<>();
        mapQuery = new HashMap<>();
        mapChildEventListenerMap = new HashMap<>();
        listFriend = FriendDB.getInstance(this).getListFriend();
        listGroup = GroupDB.getInstance(this).getListGroups();
        listKey = new ArrayList<>();

        if(listFriend.getListFriend().size() > 0 || listGroup.size() > 0) {
            //Dang ky lang nghe cac room tai day
            for (final Friend friend : listFriend.getListFriend()) {
                if (!listKey.contains(friend.idRoom)) {
                    mapQuery.put(friend.idRoom, FirebaseDatabase.getInstance().getReference().child("message/" + friend.idRoom).limitToLast(1));
                    mapChildEventListenerMap.put(friend.idRoom, new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (mapMark.get(friend.idRoom) != null && mapMark.get(friend.idRoom)) {
                                Toast.makeText(FriendChatService.this, friend.name + ": " + ((HashMap)dataSnapshot.getValue()).get("text"), Toast.LENGTH_SHORT).show();
                            } else {
                                mapMark.put(friend.idRoom, true);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    listKey.add(friend.idRoom);
                }
                mapQuery.get(friend.idRoom).addChildEventListener(mapChildEventListenerMap.get(friend.idRoom));
            }

            for(final Group group: listGroup){
                if (!listKey.contains(group.id)) {
                    mapQuery.put(group.id, FirebaseDatabase.getInstance().getReference().child("message/" + group.id).limitToLast(1));
                    mapChildEventListenerMap.put(group.id, new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (mapMark.get(group.id) != null && mapMark.get(group.id)) {
                                Toast.makeText(FriendChatService.this, group.groupInfo.get("name") + ": " + ((HashMap)dataSnapshot.getValue()).get("text"), Toast.LENGTH_SHORT).show();
                            } else {
                                mapMark.put(group.id, true);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    listKey.add(group.id);
                }
                mapQuery.get(group.id).addChildEventListener(mapChildEventListenerMap.get(group.id));
            }

        }else{
            stopSelf();
        }
    }

    public void stopNotify(String id) {
        mapMark.put(id, false);
    }

    public void createNotify(String title, String content, String url) {
        Intent activityIntent = new Intent(this, ChatActivity.class);
        activityIntent.setAction(Intent.ACTION_SEND);
        activityIntent.setType("text/plain");
        activityIntent.putExtra(Intent.EXTRA_TEXT, url);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_avata)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(
                        Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        notificationManager.notify(0,
                notificationBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "OnStartService");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "OnBindService");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(String id: listKey){
            mapQuery.get(id).removeEventListener(mapChildEventListenerMap.get(id));
        }
        mapQuery.clear();
        mapChildEventListenerMap.clear();
        Log.d(TAG, "OnDestroyService");
    }

    public class LocalBinder extends Binder {
        public FriendChatService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FriendChatService.this;
        }
    }
}
