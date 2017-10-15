package com.example.hackd.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.hackd.demo.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hackd on 11-Aug-17.
 */

public class LoginActivity extends AppCompatActivity implements DialogWarrning.CallBack {
    private ActivityLoginBinding binding = null;
    private SaveAccount saveAccount;
    private SharedPreferences sharedPreferences;
    private DialogWarrning dialogWarrning;
    private Message message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        saveAccount = SaveAccount.GET();

        sharedPreferences = getSharedPreferences(saveAccount.getKey(), MODE_PRIVATE | MODE_APPEND);

        binding.edtID.setText(sharedPreferences.getString(saveAccount.getKey_Id(), null));
        binding.edtPass.setText(sharedPreferences.getString(saveAccount.getKey_Pass(), null));
        binding.chkSave.setChecked(sharedPreferences.getBoolean(saveAccount.getKey_checkAuto(), false));

        binding.btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertDialogBuilder.setTitle("Thông báo");
                alertDialogBuilder.setMessage("Bạn có muốn thoát?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialogBuilder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check empty
                String error;
                if (TextUtils.isEmpty(binding.edtID.getText())) {
                    error = "Tài khoản rỗng. Vui lòng kiểm tra lại!";
                } else if (TextUtils.isEmpty(binding.edtPass.getText())) {
                    error = "Mật khẩu rỗng. Vui lòng kiểm tra lại!";
                } else {
                    // Kiem tra internet
                    final String id = binding.edtID.getText().toString();
                    final String pass = binding.edtPass.getText().toString();

                    if (!isINTERNET()) {
                        // 1: Kiem tra data o SharedReferences trung thi OK
                        if (id.compareTo(sharedPreferences.getString(saveAccount.getKey_Id(), null)) == 0 && pass.compareTo(sharedPreferences.getString(saveAccount.getKey_Pass(), null)) == 0) {
                            // Show dialog canh bao
                            if (!sharedPreferences.getBoolean(saveAccount.getKey_checkAgain(), false)) {
                                dialogWarrning = new DialogWarrning(saveAccount.getTextWarrning());
                                dialogWarrning.setCancelable(false);
                                dialogWarrning.show(getSupportFragmentManager(), null);
                            } else {
                                updateSharedPreferences();
                                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainActivity);
                            }
                            return;
                        } else {
                            error = "Tài khoản hoặc mật khẩu không đúng. Vui lòng kiểm tra lại!";
                        }
                    } else {
                        // Co internet
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Connect server
                                if (SocketClient.create(LoginActivity.this).connectServer()) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    // Send id, password, day
                                    try {
                                        JSONObject data = new JSONObject();
                                        data.put("id", id);
                                        data.put("pass", pass);
                                        // Send data login
                                        SocketClient.create(LoginActivity.this).sendData("login", data.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                        message = new Message();
                                        message.what = 1;
                                        message.obj = "Quá trình đăng nhập xảy ra lỗi. Vui lòng thử lại!";
                                        handler.sendMessage(message);
                                    }

                                } else {
                                    message = new Message();
                                    message.what = 1;
                                    message.obj = "Kết nối Server xảy ra lỗi. Vui lòng thử lại!";
                                    handler.sendMessage(message);

                                }
                            }
                        });
                        v.setEnabled(false);
                        thread.start();
                        try {
                            thread.join();
                            thread.interrupt();
                            v.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isINTERNET() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!binding.chkSave.isChecked()) {
            editor.remove(SaveAccount.GET().getKey_Id());
            editor.remove(SaveAccount.GET().getKey_Pass());
            editor.remove(SaveAccount.GET().getKey_checkAuto());
        } else {
            editor.putString(SaveAccount.GET().getKey_Id(), binding.edtID.getText().toString());
            editor.putString(SaveAccount.GET().getKey_Pass(), binding.edtPass.getText().toString());
            editor.putBoolean(SaveAccount.GET().getKey_checkAuto(), true);
        }
        editor.commit();
    }

    @Override
    public void CloseDialog(boolean checkAgain) {
        dialogWarrning.dismiss();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(saveAccount.getKey_checkAgain(), checkAgain);
        editor.commit();
        updateSharedPreferences();
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
                updateSharedPreferences();
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                mainActivity.putExtra("data", (String[]) msg.obj);
                startActivity(mainActivity);
            }
            return true;
        }
    });
}
