package com.example.hackd.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by hackd on 20-Aug-17.
 */

public class StudentObject implements Serializable {
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JSONArray getArrJSONCheckDay() {
        return arrJSONCheckDay;
    }

    private String id, name;

    public void setArrJSONCheckDay(JSONArray arrJSONCheckDay) {
        this.arrJSONCheckDay = arrJSONCheckDay;
    }

    private JSONArray arrJSONCheckDay;

    public StudentObject(JSONObject data) throws JSONException {

        this.id = data.getString("id");
        this.name = data.getString("name");
        this.arrJSONCheckDay = data.getJSONArray("check");
    }

    public StudentObject(){}
}
