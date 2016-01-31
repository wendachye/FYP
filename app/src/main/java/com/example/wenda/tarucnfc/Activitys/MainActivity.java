package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.Fragments.AccountFragment;
import com.example.wenda.tarucnfc.Fragments.BusScheduleFragment;
import com.example.wenda.tarucnfc.Fragments.ClassScheduleFragment;
import com.example.wenda.tarucnfc.Fragments.DashboardFragment;
import com.example.wenda.tarucnfc.Fragments.FoodOrderFragment;
import com.example.wenda.tarucnfc.Fragments.WalletFragment;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    ImageButton mImageButtonEditProfile;
    ImageView mImageViewProfilePicture;
    TextView mTextViewUserID;
    private final static String GET_JSON_URL = "http://tarucandroid.comxa.com/Login/get_profilePicturePath.php";
    private Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set fragmentHome as main page
        DashboardFragment fragmentDashboard = new DashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragmentDashboard).commit();

        // setup UIL
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        //mNavigationView.setCheckedItem(R.id.nav_menu_dashboard);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    case R.id.nav_menu_dashboard:
                        getSupportActionBar().setTitle(R.string.dashboard);
                        DashboardFragment fragmentHome = new DashboardFragment();
                        fragmentTransaction.replace(R.id.frame, fragmentHome);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.nav_menu_classSchedule:
                        getSupportActionBar().setTitle(R.string.timetable);
                        ClassScheduleFragment fragmentTimetable = new ClassScheduleFragment();
                        fragmentTransaction.replace(R.id.frame, fragmentTimetable);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.nav_menu_busSchedule:
                        getSupportActionBar().setTitle(R.string.busSchedule);
                        BusScheduleFragment fragmentBusSchedule = new BusScheduleFragment();
                        fragmentTransaction.replace(R.id.frame, fragmentBusSchedule);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.nav_menu_wallet:
                        getSupportActionBar().setTitle(R.string.wallet);
                        WalletFragment fragmentWallet = new WalletFragment();
                        fragmentTransaction.replace(R.id.frame, fragmentWallet);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.nav_menu_foodOrder:
                        getSupportActionBar().setTitle(R.string.foodOrder);
                        FoodOrderFragment fragmentFoodOrder = new FoodOrderFragment();
                        fragmentTransaction.replace(R.id.frame, fragmentFoodOrder);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.nav_menu_sign_out:
                        removeLoginDetail();
                        Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }

        });

        // Initializing Drawer Layout and ActionBarToggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();

        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_view_header);

        mTextViewUserID = (TextView) headerView.findViewById(R.id.navigation_view_userID);

        mImageButtonEditProfile = (ImageButton) headerView.findViewById(R.id.navigation_view_account_setting);
        mImageButtonEditProfile.setOnClickListener(this);

        mImageViewProfilePicture = (ImageView) headerView.findViewById(R.id.navigation_view_profile_picture);
        mImageViewProfilePicture.setOnClickListener(this);

        initProfileDetail();

    }

    public void initProfileDetail() {

        OfflineLogin offlineLogin = getLoginDetail(this);

        if (offlineLogin != null) {
            mTextViewUserID.setText(offlineLogin.getAccountID());
            Log.d("track", "aaa " + offlineLogin.getAccountID());
            new GetJson(offlineLogin.getAccountID()).execute();
            Log.d("track", "pix" + account.getProfilePicturePath());

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
        if (id == R.id.action_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_change_pincode) {
            Intent intent = new Intent(this, ChangePinCodeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_view_account_setting:
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(this, EditAccountActivity.class);
                intent.putExtra(KEY_ACCOUNT_ID, getLoginDetail(this).getAccountID());
                startActivity(intent);
                break;

            case R.id.navigation_view_profile_picture:
                AccountFragment fragmentAccount = new AccountFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame, fragmentAccount).commit();
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String accountID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIUtils.getProgressDialog(PinEntryActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //UIUtils.getProgressDialog(PinEntryActivity.this, "OFF");
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_ACCOUNT_ID, accountID);
            Log.d("track", "id " + accountID);
            return rh.sendPostRequest(GET_JSON_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setProfilePicturePath(jsonObject.getString(AccountContract.AccountRecord.KEY_PROFILE_PICTURE_PATH));
            Log.d("track", "pix1 " + account.getProfilePicturePath());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        ImageLoader.getInstance().displayImage(account.getProfilePicturePath(), mImageViewProfilePicture, options);
    }

}