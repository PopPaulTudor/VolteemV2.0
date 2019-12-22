package volteem.com.volteem.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.MainActivityPresenter;

import static android.view.Gravity.LEFT;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MainActivityPresenter presenter;
    private MenuItem selectedItem, eventsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this);
        presenter.onCreate();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Events");
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(LEFT);
            }
        });
        replaceFragmentByClass(new EventsFragment());
        navigationView.setItemIconTintList(null);
        eventsItem = navigationView.getMenu().findItem(R.id.nav_events);
        eventsItem.setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_log_out).setCheckable(false);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment instanceof EventsFragment) {
                super.onBackPressed();
            } else {
                replaceFragmentByClass(new EventsFragment());

                if (selectedItem != null) {
                    selectedItem.setChecked(false);
                }
                eventsItem.setChecked(true);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Events");
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        selectedItem = item;
        if (isNetworkAvailable()) {
            Fragment fragment = null;
            String actionBarTitle = getActionBar() == null ? "" : String.valueOf(getActionBar()
                    .getTitle());
            switch (id) {
                case R.id.nav_events: {
                    fragment = new EventsFragment();
                    replaceFragmentByClass(fragment);
                    actionBarTitle = "Events";
                    break;

                }
                case R.id.nav_ngos: {
                    fragment=new NgosFragment();
                    replaceFragmentByClass(fragment);
                    actionBarTitle="NGOs";
                    break;
                }

                case R.id.nav_profile: {
                    fragment = new ProfileFragment();
                    replaceFragmentByClass(fragment);
                    actionBarTitle = "Profile";
                    break;

                }
                case R.id.nav_news: {
                    fragment = new NewsFragment();
                    replaceFragmentByClass(fragment);
                    actionBarTitle = "News";
                    break;
                }

                case R.id.nav_settings: {
                    fragment = new SettingsFragment();
                    replaceFragmentByClass(fragment);
                    actionBarTitle = "Settings";
                    break;
                }
                case R.id.nav_about: {
                    fragment = new AboutFragment();
                    replaceFragmentByClass(fragment);
                    actionBarTitle = "About";
                    break;
                }

                case R.id.nav_log_out: {

                    final AlertDialog logoutAlertDialog = new AlertDialog.Builder(this)
                            .setTitle(getString(R
                                    .string.logout_message_title))
                            .setMessage(getString(R
                                    .string.log_out_message))
                            .setCancelable(true)
                            .setPositiveButton(getString(R
                                    .string.logout_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    presenter.logOut();
                                }
                            })
                            .setNegativeButton(getString(R
                                    .string.logout_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create();
                    logoutAlertDialog.show();
                    break;
                }
                default:
                    Toast.makeText(getApplicationContext(),
                            "Couldn't find item", Toast.LENGTH_SHORT).show();
                    break;
            }
            if (fragment != null) {
                replaceFragmentByClass(fragment);
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(actionBarTitle);
            }

        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;

    }

    private void replaceFragmentByClass(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onLogOutSuccessful() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onLogOutInformationFailed(VolteemCommonException volteemCommonException) {
        Toast.makeText(getApplicationContext(), "Log out failed", Toast.LENGTH_SHORT).show();
    }
}