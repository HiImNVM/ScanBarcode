package com.example.hackd.demo;

/**
 * Created by hackd on 13-Aug-17.
 */

public class SaveAccount {
    private static SaveAccount instance;

    public String getTextWarrning() {
        return textWarrning;
    }

    private String textWarrning = "Lưu ý! Với việc không có kết nối Internet dữ liệu trên máy sẽ được đồng bộ ngay khi có mạng.";

    public String getKey() {
        return key;
    }

    public String getKey_Id() {
        return key_Id;
    }

    public String getKey_Pass() {
        return key_Pass;
    }

    public String getKey_checkAuto() {
        return key_checkAuto;
    }

    public String getKey_checkAgain() {
        return key_checkAgain;
    }

    public String getKey_timeTable() {
        return key_timeTable;
    }


    private String key = "user";
    private String key_Id = "user_id";
    private String key_Pass = "user_pass";
    private String key_checkAuto = "user_checkAuto";
    private String key_checkAgain = "user_checkAgain";
    private String key_timeTable = "user_timeTable";


    public static SaveAccount GET() {
        if (instance == null) {
            instance = new SaveAccount();
        }
        return instance;
    }
}
