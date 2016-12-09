package com.hieuapp.rivchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hieuapp.rivchat.model.User;
import com.hieuapp.rivchat.ui.FriendsFragment;
import com.hieuapp.rivchat.ui.GroupFragment;
import com.hieuapp.rivchat.ui.LoginActivity;
import com.hieuapp.rivchat.ui.RegisterActivity;
import com.hieuapp.rivchat.ui.UserProfileFragment;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static int REQUEST_CODE_LOGIN = 1000;
    public static String STR_EXTRA_ACTION = "action";
    public static String STR_EXTRA_USERNAME = "username";
    public static String STR_EXTRA_PASSWORD = "password";
    public static String STR_DEFAULT_BASE64 = "default";
    public static String STR_FRIEND_FRAGMENT = "FRIEND";
    public static String STR_GROUP_FRAGMENT = "GROUP";
    public static String STR_INFO_FRAGMENT = "INFO";

    private AuthUtils authUtils;
    private static boolean haveActivityResult;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mFirebaseDatabaseReference;
    private FloatingActionButton floatButton;
    private ViewPagerAdapter adapter;
    private LovelyProgressDialog waitingDialog;
//    public static String UID = "";

    //TODO only use this UID for debug mode
    public static String UID = "6kU0SbJPF5QJKZTfvW1BqKolrx22";

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        haveActivityResult = true;
        initTab();
        initFirebase();
    }

    /**
     * Khoi tao 3 tab
     */
    private void initTab() {
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorIndivateTab));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_person,
                R.drawable.ic_tab_group,
                R.drawable.ic_tab_infor
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FriendsFragment(), STR_FRIEND_FRAGMENT);
        adapter.addFrag(new GroupFragment(), STR_GROUP_FRAGMENT);
        adapter.addFrag(new UserProfileFragment(), STR_INFO_FRAGMENT);
        floatButton.setOnClickListener(((FriendsFragment)adapter.getItem(0)).onClickFloatButton);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(adapter.getItem(position) instanceof FriendsFragment){
                    floatButton.setOnClickListener(((FriendsFragment)adapter.getItem(position)).onClickFloatButton);
                    floatButton.setImageResource(R.drawable.plus);
                }else if(adapter.getItem(position) instanceof GroupFragment){
                    floatButton.setOnClickListener(((GroupFragment)adapter.getItem(position)).onClickFloatButton);
                    floatButton.setImageResource(R.drawable.ic_float_add_group);
                }else{
                    floatButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAuth.signOut();
                            Snackbar.make(view, "Logout", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Khởi tạo các thành phần cần thiết cho việc quản lý đăng nhập
     */
    private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    UID = user.getUid();
                    Toast.makeText(MainActivity.this, "Uid: " + user.getUid(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out, start activity login
                    if (haveActivityResult) {

                        //TODO only debug mode
//                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN);
                        haveActivityResult = false;
                    }
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //Khoi tao realtime database
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //Khoi tao dialog waiting khi dang nhap
        waitingDialog = new LovelyProgressDialog(this).setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        haveActivityResult = true;
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            if (data.getStringExtra(STR_EXTRA_ACTION).equals(LoginActivity.STR_EXTRA_ACTION_LOGIN)) {
                authUtils.signIn(data.getStringExtra(STR_EXTRA_USERNAME), data.getStringExtra(STR_EXTRA_PASSWORD));
            } else if (data.getStringExtra(STR_EXTRA_ACTION).equals(RegisterActivity.STR_EXTRA_ACTION_REGISTER)) {
                authUtils.createUser(data.getStringExtra(STR_EXTRA_USERNAME), data.getStringExtra(STR_EXTRA_PASSWORD));
            }else if(data.getStringExtra(STR_EXTRA_ACTION).equals(LoginActivity.STR_EXTRA_ACTION_RESET)){
                authUtils.resetPassword(data.getStringExtra(STR_EXTRA_USERNAME));
            }
        } else if (resultCode == RESULT_CANCELED) {
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Adapter hien thi tab
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    /**
     * Dinh nghia cac ham tien ich cho quas trinhf dang nhap, dang ky,...
     */
    class AuthUtils{
        /**
         * Action register
         * @param email
         * @param password
         */
        void createUser(String email, String password) {
            waitingDialog.setIcon(R.drawable.ic_add_friend)
                    .setTitle("Registering....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            waitingDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                new LovelyInfoDialog(MainActivity.this){
                                    @Override
                                    public LovelyInfoDialog setConfirmButtonText(String text) {
                                        findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dismiss();
                                                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN);
                                                haveActivityResult = false;
                                            }
                                        });
                                        return super.setConfirmButtonText(text);
                                    }
                                }
                                        .setTopColorRes(R.color.colorAccent)
                                        .setIcon(R.drawable.ic_add_friend)
                                        .setTitle("Register false")
                                        .setMessage("Email exist or weak password!")
                                        .setConfirmButtonText("ok")
                                        .setCancelable(false)
                                        .show();
                            } else {
                                initNewUserInfo();
                                Toast.makeText(MainActivity.this, "Register and Login success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        /**
         * Action Login
         * @param email
         * @param password
         */
        void signIn(String email, String password) {
            waitingDialog.setIcon(R.drawable.ic_person_low)
                    .setTitle("Login....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            waitingDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                new LovelyInfoDialog(MainActivity.this){
                                    @Override
                                    public LovelyInfoDialog setConfirmButtonText(String text) {
                                        findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dismiss();
                                                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN);
                                                haveActivityResult = false;
                                            }
                                        });
                                        return super.setConfirmButtonText(text);
                                    }
                                }
                                        .setTopColorRes(R.color.colorAccent)
                                        .setIcon(R.drawable.ic_person_low)
                                        .setTitle("Login false")
                                        .setMessage("Email not exist or wrong password!")
                                        .setCancelable(false)
                                        .setConfirmButtonText("Ok")
                                        .show();
                            } else {
                                Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        /**
         * Action reset password
         * @param email
         */
        void resetPassword(final String email){
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            new LovelyInfoDialog(MainActivity.this)
                                    .setTopColorRes(R.color.colorPrimary)
                                    .setIcon(R.drawable.ic_pass_reset)
                                    .setTitle("Password Recovery")
                                    .setMessage("Sent email to " + email)
                                    .show();
                        }
                    });
        }

        /**
         * Khoi tao thong tin mac dinh cho tai khoan moi
         */
        void initNewUserInfo(){
            User newUser = new User();
            newUser.email = user.getEmail();
            newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
            newUser.avata = STR_DEFAULT_BASE64;
            mFirebaseDatabaseReference.child("user/"+user.getUid()).setValue(newUser);
        }
    }
}