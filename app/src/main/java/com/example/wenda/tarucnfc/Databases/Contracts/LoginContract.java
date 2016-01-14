package com.example.wenda.tarucnfc.Databases.Contracts;

import android.provider.BaseColumns;

public class LoginContract {
    public LoginContract(){
    }

    public static abstract class LoginRecord implements BaseColumns {
        public final static String LOGIN_TABLE = "Login";
        public final static String KEY_LOGIN_ID = "LoginID";
        public final static String KEY_ACCOUNT_ID = "AccountID";
        public final static String KEY_PASSWORD = "Password";

    }
}
