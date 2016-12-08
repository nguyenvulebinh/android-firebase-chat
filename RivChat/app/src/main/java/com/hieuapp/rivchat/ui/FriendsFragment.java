package com.hieuapp.rivchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hieuapp.rivchat.MainActivity;
import com.hieuapp.rivchat.R;
import com.hieuapp.rivchat.model.ListFriend;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

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
        onClickFloatButton = new FragFriendClickFloatButton();
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
                        Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}