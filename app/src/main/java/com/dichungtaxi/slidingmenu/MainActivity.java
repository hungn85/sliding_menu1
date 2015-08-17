package com.dichungtaxi.slidingmenu;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.dichungtaxi.container.myFragmentContainer;
import com.dichungtaxi.slidingmenu.adapters.NavListAdapter;
import com.dichungtaxi.slidingmenu.fragments.AboutFragment;
import com.dichungtaxi.slidingmenu.fragments.HomeFragment;
import com.dichungtaxi.slidingmenu.fragments.SettingFragment;
import com.dichungtaxi.slidingmenu.models.NavItem;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;
    List<NavItem> listNavItems;

    final public static int FRAGMENT_HOME = 0;
    final public static int FRAGMENT_ABOUT = 1;
    final public static int FRAGMENT_SETTING = 2;

    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prepare
        prepare();

    }

    private void prepare() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //set title
        setTitle(R.string.app_name);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1B7EBA"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        //assign layout
        prepareControls();

        //create nav items
        prepareListNavDatas();

        //set adapter for ListView lvNav
        prepareListNavAdapter_ClickEvent();

        //add com.dichungtaxi.slidingmenu.fragments
        prepareFragment();

        //prepare something
        goToFragment(0);

        //prepare actionBarDrawerToggle
        prepareActionBarDrawerToggle();
    }



    private void prepareControls() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvNav = (ListView) findViewById(R.id.nav_list);
    }

    private void prepareListNavDatas () {
        listNavItems = new ArrayList<NavItem>();
        listNavItems.add(new NavItem("Home", "Home page", R.drawable.ic_action_home));
        listNavItems.add(new NavItem("Setting", "Setting page", R.drawable.ic_action_setting));
        listNavItems.add(new NavItem("About", "About page", R.drawable.ic_action_about));
    }

    private void prepareFragment() {
        myFragmentContainer.add(new HomeFragment());
        myFragmentContainer.add(new SettingFragment());
        myFragmentContainer.add(new AboutFragment());
    }

    public void goToFragment(int i) {
        myFragmentContainer.goTo(getSupportFragmentManager(), i);

        //setTitle(listNavItems.get(i).getTitle());
        lvNav.setItemChecked(i, true);
        drawerLayout.closeDrawer(drawerPane);
    }

    private void prepareActionBarDrawerToggle() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void prepareListNavAdapter_ClickEvent() {
        NavListAdapter navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav_list, listNavItems);
        lvNav.setAdapter(navListAdapter);
        lvNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                goToFragment(position);
            }
        });
    }

    private void gotoHomepage() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }


        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
