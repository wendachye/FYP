package com.example.wenda.tarucnfc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wenda.tarucnfc.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.login_title);

    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        //intent.putExtra(KEY_ACCOUNT_ID, getLoginDetail().getAccountId());
        startActivity(intent);
    }

    public void loginButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(KEY_ACCOUNT_ID, getLoginDetail().getAccountId());
        startActivity(intent);
    }
}
