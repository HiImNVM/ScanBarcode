package com.example.hackd.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackd on 20-Aug-17.
 */

public class Subject {
    public String getName() {
        return name;
    }

    public String getNumbers() {
        return numbers;
    }

    public String getName_room() {
        return name_room;
    }

    public String getTimebegin() {
        return timebegin;
    }

    public List<StudentObject> getListStudentObject() {
        return listStudentObject;
    }

    private String name, numbers, name_room, timebegin;

    public void setListStudentObject(List<StudentObject> listStudentObject) {
        this.listStudentObject = listStudentObject;
    }

    private List<StudentObject> listStudentObject;

    public Subject(JSONObject data) throws JSONException {
        this.name = data.getString("name");
        this.numbers = data.getString("numbers");
        this.name_room = data.getString("name_room");
        this.timebegin = data.getString("timebegin");

        listStudentObject = new ArrayList<>();
        JSONArray arrJSONStudent = data.getJSONArray("list_student");
        for (int i = 0; i < arrJSONStudent.length(); ++i) {
            StudentObject studentObject = new StudentObject(arrJSONStudent.optJSONObject(i));
            listStudentObject.add(studentObject);
        }
    }
}
