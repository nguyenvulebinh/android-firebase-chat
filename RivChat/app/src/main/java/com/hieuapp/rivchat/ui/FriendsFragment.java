package com.hieuapp.rivchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hieuapp.rivchat.MainActivity;
import com.hieuapp.rivchat.R;
import com.hieuapp.rivchat.model.ListFriend;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerListFrends;
    private ListFriendsAdapter adapter;
    public FragFriendClickFloatButton onClickFloatButton;

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
        onClickFloatButton = new FragFriendClickFloatButton(getContext());
        return layout;
    }

    /**
     * Thuc hien ket ban
     *
     * @param email
     */
    private void makeFriend(String email) {

    }

}

class ListFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ListFriend listFriend;
    private Context context;

    public ListFriendsAdapter(Context context, ListFriend listFriend) {
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
        if (listFriend.getListFriend().get(position).avata.equals(MainActivity.STR_DEFAULT_BASE64)) {
            ((ItemFriendViewHolder) holder).avata.setImageResource(R.drawable.default_avata);
        }
        if (listFriend.getListFriend().get(position).status.isOnline) {
            ((ItemFriendViewHolder) holder).avata.setBorderWidth(10);
        } else {
            ((ItemFriendViewHolder) holder).avata.setBorderWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return listFriend.getListFriend() != null ? listFriend.getListFriend().size() : 0;
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

class FragFriendClickFloatButton implements View.OnClickListener {
    Context context;
    LovelyProgressDialog dialogWait;

    public FragFriendClickFloatButton(Context context) {
        this.context = context;
        dialogWait = new LovelyProgressDialog(context);
    }

    @Override
    public void onClick(final View view) {
        new LovelyTextInputDialog(view.getContext(), R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle("Add friend")
                .setMessage("Enter friend email")
                .setIcon(R.drawable.ic_add_friend)
                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .setInputFilter("Email not found", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        Pattern VALID_EMAIL_ADDRESS_REGEX =
                                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
                        return matcher.find();
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        //Tim id user id
                        findIDEmail(text);
                        //Check xem da ton tai ban ghi friend chua
                        //Ghi them 1 ban ghi
                    }
                })
                .show();
    }

    /**
     * TIm id cua email tren server
     *
     * @param email
     */
    private void findIDEmail(String email) {
        dialogWait.setCancelable(false)
                .setIcon(R.drawable.ic_add_friend)
                .setTitle("Finding friend....")
                .setTopColorRes(R.color.colorPrimary)
                .show();
        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialogWait.dismiss();
                if (dataSnapshot.getValue() == null) {
                    //email not found
                    new LovelyInfoDialog(context)
                            .setTopColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.ic_add_friend)
                            .setTitle("Fail")
                            .setMessage("Email not found")
                            .show();
                } else {
                    String id = ((HashMap)dataSnapshot.getValue()).keySet().iterator().next().toString();
                    if(id.equals(MainActivity.UID)){
                        new LovelyInfoDialog(context)
                                .setTopColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.ic_add_friend)
                                .setTitle("Fail")
                                .setMessage("Email not valid")
                                .show();
                    }else {
                        addFriendStep1(id);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Tim cac cap ban dua vao iduser1
     *
     */
    private void addFriendStep1(final String idFriend){
        dialogWait.setCancelable(false)
                .setIcon(R.drawable.ic_add_friend)
                .setTitle("Add friend....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        FirebaseDatabase.getInstance().getReference().child("friend").orderByChild("idUser1").equalTo(idFriend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = mapRecord.keySet().iterator();
                    while (listKey.hasNext()){
                        String key = listKey.next().toString();
                        if(((HashMap)mapRecord.get(key)).get("idUser2").toString().equals(MainActivity.UID)){
                            dialogWait.dismiss();
                            new LovelyInfoDialog(context)
                                    .setTopColorRes(R.color.colorPrimary)
                                    .setIcon(R.drawable.ic_add_friend)
                                    .setTitle("Success")
                                    .setMessage("Add friend success")
                                    .show();
                            return;
                        }
                    }
                }
                addFriendStep2(idFriend);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Tim cac cap ban dua vao iduser2
     *
     */
    private void addFriendStep2(final String idFriend){
        FirebaseDatabase.getInstance().getReference().child("friend").orderByChild("idUser2").equalTo(idFriend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = mapRecord.keySet().iterator();
                    while (listKey.hasNext()) {
                        String key = listKey.next().toString();
                        if (((HashMap) mapRecord.get(key)).get("idUser1").toString().equals(MainActivity.UID)) {
                            dialogWait.dismiss();
                            new LovelyInfoDialog(context)
                                    .setTopColorRes(R.color.colorPrimary)
                                    .setIcon(R.drawable.ic_add_friend)
                                    .setTitle("Success")
                                    .setMessage("Add friend success")
                                    .show();
                            return;
                        }
                    }
                }
                addFriend(idFriend);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add friend
     * @param idFriend
     */
    private void addFriend(final String idFriend){
        Map<String, String> map= new HashMap<>();
        map.put("idUser1", MainActivity.UID);
        map.put("idUser2", idFriend);
        FirebaseDatabase.getInstance().getReference().child("friend").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialogWait.dismiss();
                new LovelyInfoDialog(context)
                        .setTopColorRes(R.color.colorPrimary)
                        .setIcon(R.drawable.ic_add_friend)
                        .setTitle("Success")
                        .setMessage("Add friend success")
                        .show();
            }
        });
    }
}