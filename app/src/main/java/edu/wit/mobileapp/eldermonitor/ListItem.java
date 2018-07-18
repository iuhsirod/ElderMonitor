package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ListItem {
    public Bitmap image;
    public String uid;
    public String name;

    ListItem(Context context) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.images);
        uid = "";
        name = "";
    }
}
