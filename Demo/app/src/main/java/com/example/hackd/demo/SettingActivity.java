package com.example.hackd.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackd.demo.databinding.ActivitySettingBinding;

/**
 * Created by hackd on 19-Aug-17.
 */

public class SettingActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivitySettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_setting,container,false);

        return binding.getRoot();
    }
}
