package com.hieuapp.rivchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hieuapp.rivchat.R;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
        recyclerChat.setLayoutManager(linearLayoutManager);
        adapter = new ListMessageAdapter(this);
        recyclerChat.setAdapter(adapter);
    }
}

class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    public ListMessageAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE){
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        }else if(viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE){
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return (position % 2 == 0)? ChatActivity.VIEW_TYPE_FRIEND_MESSAGE:ChatActivity.VIEW_TYPE_USER_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

class ItemMessageUserHolder extends RecyclerView.ViewHolder{

    public ItemMessageUserHolder(View itemView) {
        super(itemView);
    }
}

class ItemMessageFriendHolder extends RecyclerView.ViewHolder{

    public ItemMessageFriendHolder(View itemView) {
        super(itemView);
    }
}
