package com.example.opensourcepoe_part_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {

        private List<viewUserObservations> itemList;
        private Context context;

        public ItemListAdapter(List<viewUserObservations> itemList, Context context) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            viewUserObservations bird = itemList.get(position);

            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText("Bird : " + bird.DB_birdName + "\nLocation : " + bird.DB_birdLocation
                    + "\nBird count : " + bird.DB_birdCount );

            return view;
        }
    }


