package com.example.wenda.tarucnfc.Databases.Contracts;

import android.provider.BaseColumns;

public class AccountContract {
    public AccountContract(){
    }

    public static abstract class AccountRecord implements BaseColumns {
        public final static String KEY_ACCOUNT_ID = "AccountID";
        public final static String KEY_PROGRAMME = "Programme";
        public final static String KEY_GROUPNO = "GroupNo";
        public final static String KEY_FACULTY = "Faculty";
        public final static String KEY_CAMPUS = "Campus";
        public final static String KEY_SCHOOL_EMAIL = "SchoolEmail";
        public final static String KEY_SESSION_JOINED = "SessionJoined";
        public final static String KEY_NAME = "Name";
        public final static String KEY_NRIC_NO = "NRICNO";
        public final static String KEY_CONTACT_NO = "ContactNo";
        public final static String KEY_EMAIL_ADDRESS = "EmailAddress";
        public final static String KEY_GENDER = "Gender";
        public final static String KEY_HOME_ADDRESS = "HomeAddress";
        public final static String KEY_CAMPUS_ADDRESS = "CampusAddress";
        public final static String KEY_ACCOUNT_TYPE = "AccountType";
        public final static String KEY_ACCOUNT_BALANCE = "AccountBalance";
        public final static String KEY_PIN_CODE = "PINCode";
        public final static String KEY_STATUS = "Status";
        public final static String KEY_PROFILE_PICTURE_PATH = "ProfilePicturePath";
    }
}
