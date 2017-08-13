package com.android.rivchat.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.android.rivchat.data.SharedPreferenceHelper;
import com.android.rivchat.data.StaticConfig;
import com.android.rivchat.model.Friend;
import com.android.rivchat.model.ListFriend;

import java.util.HashMap;



public class ServiceUtils {

    private static ServiceConnection connectionServiceFriendChatForStart = null;
    private static ServiceConnection connectionServiceFriendChatForDestroy = null;

    public static boolean isServiceFriendChatRunning(Context context) {
        Class<?> serviceClass = FriendChatService.class;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopServiceFriendChat(Context context, final boolean kill) {
        if (isServiceFriendChatRunning(context)) {
            Intent intent = new Intent(context, FriendChatService.class);
            if (connectionServiceFriendChatForDestroy != null) {
                context.unbindService(connectionServiceFriendChatForDestroy);
            }
            connectionServiceFriendChatForDestroy = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    FriendChatService.LocalBinder binder = (FriendChatService.LocalBinder) service;
                    binder.getService().stopSelf();
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                }
            };
            context.bindService(intent, connectionServiceFriendChatForDestroy, Context.BIND_NOT_FOREGROUND);
        }
    }

    public static void stopRoom(Context context, final String idRoom) {
        if (isServiceFriendChatRunning(context)) {
            Intent intent = new Intent(context, FriendChatService.class);
            if (connectionServiceFriendChatForDestroy != null) {
                context.unbindService(connectionServiceFriendChatForDestroy);
                connectionServiceFriendChatForDestroy = null;
            }
            connectionServiceFriendChatForDestroy = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    FriendChatService.LocalBinder binder = (FriendChatService.LocalBinder) service;
                    binder.getService().stopNotify(idRoom);
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                }
            };
            context.bindService(intent, connectionServiceFriendChatForDestroy, Context.BIND_NOT_FOREGROUND);
        }
    }

    public static void startServiceFriendChat(Context context) {
        if (!isServiceFriendChatRunning(context)) {
            Intent myIntent = new Intent(context, FriendChatService.class);
            context.startService(myIntent);
        } else {
            if (connectionServiceFriendChatForStart != null) {
                context.unbindService(connectionServiceFriendChatForStart);
            }
            connectionServiceFriendChatForStart = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    FriendChatService.LocalBinder binder = (FriendChatService.LocalBinder) service;
                    for (Friend friend : binder.getService().listFriend.getListFriend()) {
                        binder.getService().mapMark.put(friend.idRoom, true);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                }
            };
            Intent intent = new Intent(context, FriendChatService.class);
            context.bindService(intent, connectionServiceFriendChatForStart, Context.BIND_NOT_FOREGROUND);
        }
    }

    public static void updateUserStatus(Context context){
        if(isNetworkConnected(context)) {
            String uid = SharedPreferenceHelper.getInstance(context).getUID();
            if (!uid.equals("")) {
                FirebaseDatabase.getInstance().getReference().child("user/" + uid + "/status/isOnline").setValue(true);
                FirebaseDatabase.getInstance().getReference().child("user/" + uid + "/status/timestamp").setValue(System.currentTimeMillis());
            }
        }
    }

    public static void updateFriendStatus(Context context, ListFriend listFriend){
        if(isNetworkConnected(context)) {
            for (Friend friend : listFriend.getListFriend()) {
                final String fid = friend.id;
                FirebaseDatabase.getInstance().getReference().child("user/" + fid + "/status").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            HashMap mapStatus = (HashMap) dataSnapshot.getValue();
                            if ((boolean) mapStatus.get("isOnline") && (System.currentTimeMillis() - (long) mapStatus.get("timestamp")) > StaticConfig.TIME_TO_OFFLINE) {
                                FirebaseDatabase.getInstance().getReference().child("user/" + fid + "/status/isOnline").setValue(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public static boolean isNetworkConnected(Context context) {
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
        }catch (Exception e){
            return true;
        }
    }
}
