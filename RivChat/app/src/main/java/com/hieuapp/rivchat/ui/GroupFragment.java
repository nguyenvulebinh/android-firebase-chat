package com.hieuapp.rivchat.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hieuapp.rivchat.MainActivity;
import com.hieuapp.rivchat.R;
import com.hieuapp.rivchat.model.ListFriend;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class GroupFragment extends Fragment {
    RecyclerView recyclerListGroups;
    public FragGroupClickFloatButton onClickFloatButton;
    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerListGroups = (RecyclerView) layout.findViewById(R.id.recycleListGroup);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerListGroups.setLayoutManager(layoutManager);
        ListGroupsAdapter adapter = new ListGroupsAdapter(getContext());
        recyclerListGroups.setAdapter(adapter);
        onClickFloatButton = new FragGroupClickFloatButton();
        return layout;
    }

}
class ListGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public ListGroupsAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_group, parent, false);
        return new ItemFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

class ItemGroupViewHolder extends RecyclerView.ViewHolder {
    ItemGroupViewHolder(View itemView) {
        super(itemView);
    }
}
class FragGroupClickFloatButton implements View.OnClickListener{

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Add Group", Toast.LENGTH_SHORT).show();
    }
}