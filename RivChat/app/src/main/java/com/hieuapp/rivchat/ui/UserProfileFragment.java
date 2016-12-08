package com.hieuapp.rivchat.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hieuapp.rivchat.R;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class UserProfileFragment extends Fragment {
    TextView tvUserName;
    ImageView avatar;

    public UserProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        avatar = (ImageView) view.findViewById(R.id.img_avatar);
        tvUserName = (TextView)view.findViewById(R.id.tv_username);
        tvUserName.setText("Hieu Trung Tran");

        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.hieuapp);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        avatar.setImageDrawable(dr);

        return view;
    }

}
