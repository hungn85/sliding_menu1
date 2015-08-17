package com.dichungtaxi.slidingmenu.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mygoogle.GMapV2Direction;
import com.dichungtaxi.slidingmenu.R;
import com.mygoogle.GooglePlacesAutocompleteAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import static com.dichungtaxi.slidingmenu.R.*;

/**
 * Created by CHIP on 13/08/2015.
 */
public class AboutFragment extends AbstractFragments implements AdapterView.OnItemClickListener, OnMapReadyCallback {

    @Override
    int getLayout() {
        return layout.fragment_about;
    }


    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(myView == null) {
            myView = inflater.inflate(getLayout(), null, false);
            initAutoComplete();
            initGoogleMap();
        }
        getStartAddress().setSelected(false);
        getEndAddress().setSelected(false);
        return myView;
    }

    private void initAutoComplete() {
        getStartAddress();
        getEndAddress();
    }

    GooglePlacesAutocompleteAdapter autoStartAdapter;
    AutoCompleteTextView autoStartAddress;

    GooglePlacesAutocompleteAdapter autoEndAdapter;
    AutoCompleteTextView autoEndAddress;

    AutoCompleteTextView getStartAddress() {
        if(autoStartAddress == null) {
            autoStartAddress = (AutoCompleteTextView) myView.findViewById(id.start_address);
            autoStartAddress.setAdapter(getStartAdapter());
            autoStartAddress.setOnItemClickListener(this);
        }
        return autoStartAddress;
    }


    AutoCompleteTextView getEndAddress() {
        if(autoEndAddress == null) {
            autoEndAddress = (AutoCompleteTextView) myView.findViewById(id.end_address);
            autoEndAddress.setAdapter(getEndAdapter());
            autoEndAddress.setOnItemClickListener(this);

        }
        return autoEndAddress;
    }

    GooglePlacesAutocompleteAdapter getStartAdapter() {
        if(autoStartAdapter == null) {
            autoStartAdapter = new GooglePlacesAutocompleteAdapter(getActivity(), layout.autocomplete_list_item,R.id.start_address);
        }
        return autoStartAdapter;
    }

    GooglePlacesAutocompleteAdapter getEndAdapter() {
        if(autoEndAdapter == null) {
            autoEndAdapter = new GooglePlacesAutocompleteAdapter(getActivity(), layout.autocomplete_list_item, id.end_address);
        }
        return autoEndAdapter;
    }

    GoogleMap map;
    GMapV2Direction md ;
    SupportMapFragment mMapFragment;
    Polyline polyline;
    //21.030443, 105.853816
    private void initGoogleMap () {


        FragmentManager fm = getActivity().getSupportFragmentManager();
        mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mMapFragment).commit();
        }
        mMapFragment.getMapAsync(this);

    }


    String start = "";
    String end = "";
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isStart = id == R.id.start_address;
        boolean isEnd = id == R.id.end_address;

        if( isStart || isEnd) {

            if(isStart) {
                start = getStartAdapter().getItem(position);
            }

            if(isEnd) {
                end = getEndAdapter().getItem(position);
            }

            if(start.length() > 0 && end.length() > 0) {
                drawRoute(getStartAdapter().getLatLng(position), getEndAdapter().getLatLng(position));
            }
            else if(isStart) {
                addMarker(getStartAdapter().getLatLng(position), "A");
            }
            else if(isEnd) {
                addMarker(getEndAdapter().getLatLng(position), "B");
            }
        }

    }


    ArrayList<LatLng> directionPoint;
    private void drawRoute(LatLng sourcePosition, LatLng destPosition) {

        hideKeyboard();

        if(polyline != null) {
            polyline.remove();
        }
        map.clear();

        md = new GMapV2Direction(sourcePosition, destPosition);
        directionPoint = md.getDirection();
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.BLUE);

        int size = directionPoint.size();
        for (int i = 0; i < size; i++) {
            rectLine.add(directionPoint.get(i));
        }
        polyline = map.addPolyline(rectLine);

        if(size > 0) {
            addMarker(directionPoint.get(0), "A");
            addMarker(directionPoint.get(directionPoint.size()-1), "B");
        }

        bound(directionPoint);

        TextView t = (TextView)getActivity().findViewById(id.view_km);
        t.setText("Khoảng cách "+Math.round(md.getMet() / 1000)+"Km");
    }

    private void bound(ArrayList<LatLng> lls) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng ll : lls) {
            builder.include(ll);
        }
        LatLngBounds bounds = builder.build();
        int padding = 15; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cu);
    }

    private void addMarker(LatLng latLng, String title) {

        map.addMarker(new MarkerOptions().position(latLng)
                .title(title));
        ArrayList<LatLng> lls = new ArrayList<>();
        lls.add(latLng);
        bound(lls);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //if(map == null )
        {
            map = googleMap;

            MapsInitializer.initialize(getActivity());
            map.setMyLocationEnabled(true);
            LatLng latLng = new LatLng(21.030443, 105.853816);
            CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latLng,12);
            map.animateCamera(camera);
        }
    }
}