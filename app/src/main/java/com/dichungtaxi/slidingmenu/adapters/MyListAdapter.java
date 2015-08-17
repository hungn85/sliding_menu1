package com.dichungtaxi.slidingmenu.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dichungtaxi.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHIP on 17/08/2015.
 */
public class MyListAdapter extends ArrayAdapter<String> {


    private int layout;
    public MyListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewHolder = null;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ride_item_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.ride_item_text);
            viewHolder.button = (Button) convertView.findViewById(R.id.ride_item_btn);
//            viewHolder.button.setOnClickListener(new View.OnClickListener(){
//
//
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getContext(), " postion "+position, Toast.LENGTH_SHORT).show();
//                }
//            });
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
        Button button;
    }
}
