package com.shahen.shackhands;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.shahen.shackhands.APIS.API;
import com.shahen.shackhands.APIS.WebServiceConnection.ApiConfig;
import com.shahen.shackhands.APIS.WebServiceConnection.AppConfig;
import com.shahen.shackhands.APIS.WebServiceConnection.ResponseModel;
import com.shahen.shackhands.APIS.WebServiceConnection.ResponsePass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView mWebview;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ResponseModel serverResponse;
    String valid_date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mWebview = findViewById(R.id.web);

        pass();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String name = item.toString();
        if (serverResponse != null) {
            if (name.equals(serverResponse.getMenu().get(69).getName())) {
                setWebView("http://shakehands.devmahmoudadel.com/Users/DoctorsSearch/70");
            }

            for (int i = 0; i < serverResponse.getMenu().size() - 1; i++) {
                if (name.equals(serverResponse.getMenu().get(i).getName())) {
                    // Handle the camera action
                    setWebView(API.CAT + serverResponse.getMenu().get(i).getId());
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setWebView(String link) {
        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebview.loadUrl(link);
//        setContentView(mWebview);
    }

    private void loadMenu() {
        // Map is used to multipart the file using okhttp3.RequestBody
        AppConfig appConfig = new AppConfig(API.BASE_URL);

        ApiConfig reg = appConfig.getRetrofit().create(ApiConfig.class);
        Call<ResponseModel> call = reg.loadMenu();
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                serverResponse = response.body();
                if (serverResponse != null) {
                    Log.e("Menu", "Loaded");
                    Menu m = navigationView.getMenu();
                    SubMenu topChannelMenu = m.addSubMenu(getString(R.string.department));
                    for (int i = 0; i < serverResponse.getMenu().size(); i++) {
                        topChannelMenu.add(serverResponse.getMenu().get(i).getName());
                    }
                    MenuItem mi = m.getItem(m.size() - 1);
                    mi.setTitle(mi.getTitle());

                } else {
                    //textView.setText(serverResponse.toString());
                    Log.e("Err", "Menu");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //textView.setText(t.getMessage());
                Log.e("Err", t.getMessage());

            }
        });
    }

    private void pass() {
        AppConfig appConfig = new AppConfig(API.BASE_PASS);
        ApiConfig reg = appConfig.getRetrofit().create(ApiConfig.class);
        Call<ResponsePass> call = reg.getPass();
        call.enqueue(new Callback<ResponsePass>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<ResponsePass> call, retrofit2.Response<ResponsePass> response) {
                String s = response.body().getDate();
                if (s != null) {
                    valid_date = s;
                    Log.e("ServerDate", valid_date);
                    try {
                        if (valid_date != null) {
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            String currentDate = df.format(c);
                            Log.e("currentDate", currentDate);
                            Date strDate = df.parse(valid_date);

                            if (currentDate.equals(strDate) || new Date().after(strDate)) {
                                mWebview.setVisibility(View.GONE);
                                Log.e("Date", "Not Available");
                            } else {
                                try {
                                    mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "الموقع لا يدعم الجافا سكربت", Toast.LENGTH_SHORT).show();
                                }


                                loadMenu();

                                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                                        MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                                drawer.addDrawerListener(toggle);
                                toggle.syncState();

                                navigationView = (NavigationView) findViewById(R.id.nav_view);
                                navigationView.setNavigationItemSelectedListener(MainActivity.this);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //textView.setText(serverResponse.toString());
                    Log.e("Err", "pass");
                }
            }

            @Override
            public void onFailure(Call<ResponsePass> call, Throwable t) {
                //textView.setText(t.getMessage());
                Log.e("Err", t.getMessage());

            }
        });
    }
}
