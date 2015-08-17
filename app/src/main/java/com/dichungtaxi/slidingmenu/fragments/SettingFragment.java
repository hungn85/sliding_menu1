package com.dichungtaxi.slidingmenu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dichungtaxi.slidingmenu.MainActivity;
import com.dichungtaxi.slidingmenu.R;
import com.dichungtaxi.slidingmenu.adapters.MyListAdapter;

import java.util.ArrayList;

/**
 * Created by CHIP on 13/08/2015.
 */
public class SettingFragment extends AbstractFragments {

    ArrayList<String> data = new ArrayList<String>();
    @Override
    int getLayout() {
        return R.layout.fragment_settings;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ListView listView = (ListView)getActivity().findViewById(R.id.ride_list);
//        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//        listView.setClickable(false);
        for(int i = 0; i < 99; i++) {
            data.add("Data line "+(i+1));
        }

        listView.setAdapter(
                new MyListAdapter(getActivity(),
                        R.layout.list_ride_item,
                        data));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("POSITION "+position);
//                MainActivity activity = (MainActivity)getActivity();
//                activity.goToFragment(0);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}