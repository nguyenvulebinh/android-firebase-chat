package com.hieuapp.rivchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hieuapp.rivchat.MainActivity;
import com.hieuapp.rivchat.R;
import com.hieuapp.rivchat.model.ListFriend;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerListFrends;
    private ListFriendsAdapter adapter;
    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_people, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerListFrends = (RecyclerView) layout.findViewById(R.id.recycleListFriend);
        recyclerListFrends.setLayoutManager(linearLayoutManager);
        adapter = new ListFriendsAdapter(getContext(), new ListFriend());
        recyclerListFrends.setAdapter(adapter);
        return layout;
    }

}

class ListFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ListFriend listFriend;
    private Context context;
    public ListFriendsAdapter(Context context, ListFriend listFriend){
        this.listFriend = listFriend;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_friend, parent, false);
        return new ItemFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemFriendViewHolder) holder).txtName.setText(listFriend.getListFriend().get(position).name);
        ((ItemFriendViewHolder) holder).txtMessage.setText(listFriend.getListFriend().get(position).message.text);
        ((ItemFriendViewHolder) holder).txtTime.setText(listFriend.getListFriend().get(position).message.timestamp + ":00");
        if(listFriend.getListFriend().get(position).avata.equals(MainActivity.STR_DEFAULT_BASE64)){
            ((ItemFriendViewHolder) holder).avata.setImageResource(R.drawable.default_avata);
        }
        if(listFriend.getListFriend().get(position).status.isOnline){
            ((ItemFriendViewHolder) holder).avata.setBorderWidth(10);
        }else {
            ((ItemFriendViewHolder) holder).avata.setBorderWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return listFriend.getListFriend() != null? listFriend.getListFriend().size():0;
    }
}

class ItemFriendViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView avata;
    public TextView txtName, txtTime, txtMessage;
    ItemFriendViewHolder(View itemView) {
        super(itemView);
        avata = (CircleImageView) itemView.findViewById(R.id.icon_avata);
        txtName = (TextView) itemView.findViewById(R.id.txtName);
        txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
    }
}