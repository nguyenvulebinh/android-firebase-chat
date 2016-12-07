package com.hieuapp.rivchat.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hieuapp.rivchat.MainActivity;
import com.hieuapp.rivchat.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hieuttc on 05/12/2016.
 */

public class LoginActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText editTextUsername, editTextPassword;
    public static int REQUEST_CODE_REGISTER = 2000;
    public static String STR_EXTRA_ACTION_LOGIN = "login";
    public static String STR_EXTRA_ACTION_RESET = "resetpass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        editTextUsername = (EditText) findViewById(R.id.et_username);
        editTextPassword = (EditText) findViewById(R.id.et_password);
    }

    public void clickRegisterLayout(View view) {
        getWindow().setExitTransition(null);
        getWindow().setEnterTransition(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
            startActivityForResult(new Intent(this, RegisterActivity.class),REQUEST_CODE_REGISTER, options.toBundle());
        } else {
            startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_CODE_REGISTER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_OK){
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public void clickLogin(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        if(validate(username, password)){
            Intent data = new Intent();
            data.putExtra(MainActivity.STR_EXTRA_USERNAME, username);
            data.putExtra(MainActivity.STR_EXTRA_PASSWORD, password);
            data.putExtra(MainActivity.STR_EXTRA_ACTION, STR_EXTRA_ACTION_LOGIN);
            setResult(RESULT_OK, data);
            finish();
        }else {
            Toast.makeText(this, "Invalid email or empty password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    private boolean validate(String emailStr, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return (password.length() > 0 || password.equals(";")) && matcher.find();
    }

    public void clickResetPassword(View view) {
        String username = editTextUsername.getText().toString();
        if(validate(username, ";")){
            Intent data = new Intent();
            data.putExtra(MainActivity.STR_EXTRA_USERNAME, username);
            data.putExtra(MainActivity.STR_EXTRA_ACTION, STR_EXTRA_ACTION_RESET);
            setResult(RESULT_OK, data);
            finish();
        }else {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        }
    }
}
