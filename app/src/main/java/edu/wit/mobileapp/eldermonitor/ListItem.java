package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ListItem {
    public Bitmap image;
    public String uid;
    public String fname;
    public String lname;
    public String email;
    public boolean broadcast;
    public boolean help;

    ListItem(Context context) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.images);
        uid = "";
        fname = "";
        lname = "";
        email = "";
        broadcast = false;
        help = false;

    }
}
