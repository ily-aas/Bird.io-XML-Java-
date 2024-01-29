package com.example.opensourcepoe_part_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class imageListAdapter  extends BaseAdapter {

    private List<Bitmap> imageList;
    private Context context;

    public imageListAdapter(List<Bitmap> image_List, Context context) {
        this.context = context;
        this.imageList = image_List;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       /* View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout., parent, false);
        }

        Bitmap img = imageList.get(position);
        ImageView imgId = view.findViewById(R.id.click_image);
        imgId.setImageBitmap(img);


        return view;*/

        ImageView imgView;

        // If convertView is null, create a new ImageView programmatically
        if (convertView == null) {
            imgView = new ImageView(context);
            imgView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            imgView.setLayoutParams(new ViewGroup.LayoutParams(
                    800, // Specify the width in pixels or dp as needed
                    800  // Specify the height in pixels or dp as needed
            ));

            int paddingInPixels = 300; // Adjust the padding as needed
            imgView.setPadding(paddingInPixels, 0 ,0, 20);

            // Set the ID for later reference
            imgView.setId(R.id.click_image);

            // Set the ImageView to the convertView
            convertView = imgView;
        } else {
            // If convertView is not null, reuse the existing ImageView
            imgView = (ImageView) convertView.findViewById(R.id.click_image);
        }

        // Set the bitmap to the ImageView
        Bitmap img = imageList.get(position);
        imgView.setImageBitmap(img);

        return convertView;
    }

}
