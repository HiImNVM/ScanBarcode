package com.example.hackd.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.hackd.demo.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;
    private String[] userIdName;
    private boolean isInternet;
    private SaveAccount saveAccount;
    private DialogWatting dialogWatting = new DialogWatting();
    private Message message;
    private Fragment[] arrFragment = new Fragment[2];
    private JSONArray arrJSONTimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Start waitting
        startDialogWaitting();

        // Get data intent
        Intent intent = getIntent();
        userIdName = intent.getStringArrayExtra("data");
        if (userIdName != null) {
            isInternet = true;
        }

        // Get data timetable
        saveAccount = SaveAccount.GET();
        SharedPreferences sharedPreferences = getSharedPreferences(saveAccount.getKey(), MODE_PRIVATE | MODE_APPEND);
        String timeTable = sharedPreferences.getString(saveAccount.getKey_timeTable(), null);

        if (timeTable == null) {
            // Emit Server send data
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (SocketClient.create(MainActivity.this).connectServer()) {
                        try {
                            SocketClient.create(MainActivity.this).sendData("getTimeTable", "");
                        } catch (Exception ex) {
                            message = new Message();
                            message.what = 1;
                            message.obj = "Yêu cầu Server thất bại. Vui lòng thử lại!";
                            handler.sendMessage(message);
                        }
                    } else {
                        message = new Message();
                        message.what = 1;
                        message.obj = "Kết nối Server xảy ra lỗi. Vui lòng thử lại!";
                        handler.sendMessage(message);
                    }

                }
            }).start();
        } else {
            try {
                arrJSONTimeTable = new JSONArray(timeTable);
                setTabLayout(arrJSONTimeTable);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    public void setTabLayout(JSONArray data){
        // Add Fragment
        arrFragment[0] = new CheckListActivity(data);
        arrFragment[1] = new SettingActivity();
        binding.vpMain.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        binding.tlMain.setupWithViewPager(binding.vpMain);

        // Set icon
        TabLayout.Tab tabMain = binding.tlMain.getTabAt(0);
        tabMain.setIcon(R.drawable.ic_rollup);

        TabLayout.Tab tabSetting = binding.tlMain.getTabAt(1);
        tabSetting.setIcon(R.drawable.ic_settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            closeDialogWaitting();
            if (msg.what == 1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Thông báo");
                alertDialogBuilder.setMessage(msg.obj.toString());
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else if (msg.what == 2) {
                arrJSONTimeTable = (JSONArray) msg.obj;
                setTabLayout(arrJSONTimeTable);
            }
            return true;
        }
    });

    public void startDialogWaitting() {
        dialogWatting.setCancelable(false);
        dialogWatting.show(getSupportFragmentManager(), null);
    }

    public void closeDialogWaitting() {
        dialogWatting.dismiss();
    }

    private class HomePagerAdapter extends FragmentPagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return arrFragment[position];
        }

        @Override
        public int getCount() {
            return arrFragment.length;
        }
    }
}
