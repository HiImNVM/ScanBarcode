package com.example.hackd.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackd.demo.databinding.ActivityChecklistBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by hackd on 19-Aug-17.
 */

public class CheckListActivity extends Fragment {

    private Map<Integer, List<Subject>> mapSubject;
    private ActivityChecklistBinding binding;
    private List<Fragment> listFragmentDay;
    public CheckListActivity(JSONArray data) {
        convertJsonToDayObject(data);
    }

    private void convertJsonToDayObject(JSONArray arrJSON) {
        mapSubject = new Hashtable<>();
        listFragmentDay = new ArrayList<>();
        Fragment fragment;
        for (int i = 0; i < arrJSON.length(); ++i) {
            try {
                JSONObject data = arrJSON.optJSONObject(i);
                int day = data.getInt("day");
                JSONArray listMonHoc = data.getJSONArray("list_monhoc");
                List<Subject> listSubJect = new ArrayList<>();
                for (int j = 0; j < listMonHoc.length(); ++j) {
                    Subject subject = new Subject(listMonHoc.getJSONObject(j));
                    listSubJect.add(subject);
                }
                mapSubject.put(day, listSubJect);
                // New Fragment
                fragment = new DayActivity(listSubJect, day);
                listFragmentDay.add(fragment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_checklist, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.vpDay.setAdapter(new DayPagerAdapter(getChildFragmentManager()));
        binding.tlDay.setupWithViewPager(binding.vpDay);

        // Set icon
        for (int i = 0; i < listFragmentDay.size(); ++i) {
            binding.tlDay.getTabAt(i).setIcon(R.drawable.ic_dayofweek);
        }
    }

    private class DayPagerAdapter extends FragmentPagerAdapter {

        public DayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragmentDay.get(position);
        }

        @Override
        public int getCount() {
            return listFragmentDay.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mapSubject.keySet().toArray()[mapSubject.size() - position - 1].toString();
            //return mapSubject.keySet().toArray()[position].toString();
            return position + 2 + "";
        }
    }
}
