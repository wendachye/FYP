package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.Login;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import java.util.HashMap;
import java.util.StringTokenizer;

public class VerifyAccount extends BaseActivity {

    private EditText mEditTextCode;
    private String Code, Name, NRIC, Contact,Email;

    private static final String Add_New_Account = "http://fypproject.host56.com/Login/register_account.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        Code = getIntent().getStringExtra("Code");
        Name = getIntent().getStringExtra("Name");
        NRIC = getIntent().getStringExtra("NRIC");
        Contact = getIntent().getStringExtra("Contact");
        Email = getIntent().getStringExtra("Email");
        Log.d("track", "Code " + Code);

        mEditTextCode = (EditText) findViewById(R.id.code);
    }

    public void confirmButton(View view) {
        Log.d("track", "Code1 " + mEditTextCode.getText().toString());
        if (mEditTextCode.getText().toString().equals(Code)){
            new AddAccount(Name, NRIC, Contact, Email).execute();
            shortToast(VerifyAccount.this, "Account registration successful");
            finish();
            Intent intent = new Intent(getApplicationContext(), ViewRegistrationAccount.class);
            startActivity(intent);
        }else{
            shortToast(this, "Activation code not correct");
        }
    }

    public class AddAccount extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String name, nric, contact, email;

        public AddAccount(String name, String nric, String contact, String email) {
            this.name = name;
            this.nric = nric;
            this.contact = contact;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIUtils.getProgressDialog(VerifyAccount.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //shortToast(VerifyAccount.this, "Account registration successful");
            //UIUtils.getProgressDialog(VerifyAccount.this, "OFF");
            //finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("Name", name);
            data.put("NRIC", nric);
            data.put("Contact", contact);
            data.put("Email", email);

            return requestHandler.sendPostRequest(Add_New_Account, data);
        }
    }
}
