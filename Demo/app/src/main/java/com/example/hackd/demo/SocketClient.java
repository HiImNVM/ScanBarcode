package com.example.hackd.demo;

import android.content.Context;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by hackd on 11-Aug-17.
 */

public class SocketClient {

    private static SocketClient socketClient;

    private static Context context;
    private Socket mSocket;

    private String IP = "169.254.171.159";
    private String PORT = "3456";

    public boolean connectServer() {
        if (mSocket != null && mSocket.connected()) {
            return true;
        }

        try {
            mSocket = IO.socket("http://" + IP + ":" + PORT);
            mSocket.on("message", onReceiveData);
            mSocket.connect();
            // Create cac event listening in there.

            return true;

        } catch (URISyntaxException e) {
        }
        return false;
    }

    public boolean sendData(String nameEvent, String text) {
        try {
            mSocket.emit(nameEvent, text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static SocketClient create(Context context) {
        if (socketClient == null) {
            socketClient = new SocketClient();
        }
        SocketClient.context = context;
        return socketClient;
    }

    // Event Emiter
    private Emitter.Listener onReceiveData = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject jsonObject = (JSONObject) args[0];
                Message message = new Message();
                switch (jsonObject.getString("TYPE")) {
                    case "ResultLogin": {
                        if (jsonObject.getBoolean("RESULT")) {
                            message.what = 2; // true
                            JSONObject jsonObjectChild = jsonObject.getJSONObject("DATA");

                            String[] arr = new String[2];
                            arr[0] = jsonObjectChild.getString("id");
                            arr[1] = jsonObjectChild.getString("name");

                            message.obj = arr;
                        } else {
                            message.what = 1; // false
                            message.obj = jsonObject.getString("DATA").toString();
                        }
                        ((LoginActivity) context).handler.sendMessage(message);
                        break;
                    }
                    case "ResultTimeTable": {
                        if (jsonObject.getBoolean("RESULT")) {
                            message.what = 2; // true
                            message.obj = jsonObject.getJSONArray("DATA");
                        } else {
                            message.what = 1; // false
                            message.obj = jsonObject.getString("DATA").toString();
                        }
                        ((MainActivity) context).handler.sendMessage(message);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
