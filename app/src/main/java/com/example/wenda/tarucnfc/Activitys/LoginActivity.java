package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.Login;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    private static final String LOGIN_URL = "http://fypproject.host56.com/Login/get_account_data.php";
    private static final String KEY_LOGINID = "loginID";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AUTHORIZATION = "Authorization";
    private static final String KEY_RESPONSE = "LoginResponse";

    private Login login = new Login();
    private Account account = new Account();
    private OfflineLogin offlineLogin;

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_login);

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void registerNewAccount(View view) {
        Intent intent = new Intent(this, RegisterNewAccount.class);
        startActivity(intent);
    }

    public void loginButton(View view) {
        try {
            login.verifyLoginID(editTextUsername.getText().toString());
            login.verifyPassword(editTextPassword.getText().toString());

            // check network
            if(new BaseActivity().isNetworkAvailable(this) == true) {
                new LoginAccount(editTextUsername.getText().toString(), editTextPassword.getText().toString()).execute();
            } else {
                shortToast(this, "Network not available");
            }
        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this is get json
    public class LoginAccount extends AsyncTask<String, Void, String> {

        String loginID, password;
        RequestHandler rh = new RequestHandler();

        public LoginAccount(String loginID, String password) {
            this.loginID = loginID;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(LoginActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(LoginActivity.this, "OFF");
            //shortToast(LoginActivity.this, json);
            extractJsonData(json);

           switch (offlineLogin.getLoginResponse()){
                case RESPONSE_404:
                    // account not found
                    shortToast(LoginActivity.this,"Account not found.");
                    editTextUsername.setText("");
                    editTextPassword.setText("");
                    break;

                case RESPONSE_SUCCESS:
                    // login success, save login state and direct to main screen
                    //shortToast(LoginActivity.this,"success");
                    saveLoginDetail(offlineLogin,LoginActivity.this);
                    // go to main screen
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;

                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect
                    shortToast(LoginActivity.this,"Password Incorrect.");
                    editTextPassword.setText("");
                    break;

                case RESPONSE_STATUS_NOT_ACTIVE:
                    // inactive account
                    shortToast(LoginActivity.this,"Account Inactive.");
                    editTextUsername.setText("");
                    editTextPassword.setText("");
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_LOGINID, loginID);
            data.put(KEY_PASSWORD, password);
            Log.d("track", "Inside " + loginID);
            Log.d("track", "Inside "  + password);
            return rh.sendPostRequest(LOGIN_URL, data);
        }
    }

    private void extractJsonData(String json) {

            try {
                JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                offlineLogin = new OfflineLogin();

                offlineLogin.setAccountID(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_ID));
                offlineLogin.setProgramme(jsonObject.getString(AccountContract.AccountRecord.KEY_PROGRAMME));
                offlineLogin.setFaculty(jsonObject.getString(AccountContract.AccountRecord.KEY_FACULTY));
                offlineLogin.setCampus(jsonObject.getString(AccountContract.AccountRecord.KEY_CAMPUS));
                offlineLogin.setSchoolEmail(jsonObject.getString(AccountContract.AccountRecord.KEY_SCHOOL_EMAIL));
                offlineLogin.setSessionJoined(jsonObject.getString(AccountContract.AccountRecord.KEY_SESSION_JOINED));
                offlineLogin.setName(jsonObject.getString(AccountContract.AccountRecord.KEY_NAME));
                offlineLogin.setNRICNo(jsonObject.getString(AccountContract.AccountRecord.KEY_NRIC_NO));
                offlineLogin.setContactNo(jsonObject.getString(AccountContract.AccountRecord.KEY_CONTACT_NO));
                offlineLogin.setEmailAddress(jsonObject.getString(AccountContract.AccountRecord.KEY_EMAIL_ADDRESS));
                offlineLogin.setGender(jsonObject.getString(AccountContract.AccountRecord.KEY_GENDER));
                offlineLogin.setHomeAddress(jsonObject.getString(AccountContract.AccountRecord.KEY_HOME_ADDRESS));
                offlineLogin.setCampusAddress(jsonObject.getString(AccountContract.AccountRecord.KEY_CAMPUS_ADDRESS));
                offlineLogin.setAccountType(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_TYPE));
                offlineLogin.setAccountBalance(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_BALANCE));
                offlineLogin.setPINcode(jsonObject.getString(AccountContract.AccountRecord.KEY_PIN_CODE));
                offlineLogin.setStatus(jsonObject.getString(AccountContract.AccountRecord.KEY_STATUS));
                offlineLogin.setProfilePicturePath(jsonObject.getString(AccountContract.AccountRecord.KEY_PROFILE_PICTURE_PATH));
                offlineLogin.setLoginResponse(jsonObject.getInt(KEY_RESPONSE));
                offlineLogin.set_Authorization(jsonObject.getString(KEY_AUTHORIZATION));
                offlineLogin.setGroupNo(jsonObject.getString(AccountContract.AccountRecord.KEY_GROUPNO));
                offlineLogin.setLoginId(Integer.parseInt(jsonObject.getString(AccountContract.AccountRecord.KEY_LOGIN_ID)));

                Log.d("track", "authorization " + jsonObject.getString(KEY_AUTHORIZATION));
                Log.d("track", "Response " + jsonObject.getInt(KEY_RESPONSE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
