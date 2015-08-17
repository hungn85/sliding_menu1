package com.dichungtaxi.container;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dichungtaxi.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHIP on 17/08/2015.
 */
public class myFragmentContainer  {

    private static List<Fragment> getFragmentList = new ArrayList<Fragment>();

    public static List<Fragment> getFragmentList () {
        return getFragmentList;
    }

    public static void add(Fragment fragment) {
        getFragmentList ().add(fragment);
    }

    public static Fragment get(int index) {
        return getFragmentList ().get(index);
    }

    public static void goTo(FragmentManager fragmentManager, int index) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content,  myFragmentContainer.get(index));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
