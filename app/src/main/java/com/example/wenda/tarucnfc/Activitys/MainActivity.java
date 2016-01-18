package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.Fragments.AccountFragment;
import com.example.wenda.tarucnfc.Fragments.DashboardFragment;
import com.example.wenda.tarucnfc.Fragments.WalletFragment;
import com.example.wenda.tarucnfc.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ImageButton mimageButton;
    private ImageView mimageView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set fragmentHome as main page
        AccountFragment fragmentAccount = new AccountFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragmentAccount).commit();

        mimageButton = (ImageButton) findViewById(R.id.edit_account);
        mimageButton.setOnClickListener(this);

        mimageView = (ImageView) findViewById(R.id.image_profile);
        mimageView.setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.navigation_view_studentID);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setCheckedItem(R.id.nav_menu_dashboard);
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

                    case R.id.nav_menu_wallet:
                        getSupportActionBar().setTitle(R.string.wallet);
                        WalletFragment fragmentWallet = new WalletFragment();
                        fragmentTransaction.replace(R.id.frame, fragmentWallet);
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

        initProfileDetail();
    }

    public void initProfileDetail() {
        // if account haven't login
        OfflineLogin offlineLogin = getLoginDetail(this);

        if (offlineLogin != null) {
            mTextView.setText(offlineLogin.getAccountID());
            //mTextName.setText(offlineLogin.getName());
            //mTextEmail.setText(offlineLogin.getEmail());
            //ImageLoader.getInstance().displayImage(offlineLogin.getProfileImagePath(), mImageProfile, options);
            //ImageLoader.getInstance().displayImage(offlineLogin.getCoverImagePath(), mImageCover, options);
            //mNavigationView.getMenu().getItem(9).setVisible(true);
        } else {
            //mImageProfile.setImageResource(R.drawable.ic_github_circle);
           // mTextName.setText(R.string.sign_in);
            //mTextEmail.setText("");
            //mNavigationView.setCheckedItem(R.id.nav_menu_home);
            //mImageCover.setVisibility(View.GONE);
            //mNavigationView.getMenu().getItem(9).setVisible(false);
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
        }
        else if (id == R.id.action_change_pincode) {
            Intent intent = new Intent(this, ChangePinCodeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_account:
                Intent intent = new Intent(this, EditAccountActivity.class);
                //intent.putExtra(KEY_ACCOUNT_ID, getLoginDetail().getAccountId());
                startActivity(intent);
                break;
            case R.id.image_profile:
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
}