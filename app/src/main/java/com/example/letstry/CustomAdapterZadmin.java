package com.example.letstry;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapterZadmin extends BaseAdapter {

    private ArrayList<FirModel> allfirmodels;
    ArrayList<String> trackKey;
    private Context context;
    int size;
    private LayoutInflater layoutInflater;

    public CustomAdapterZadmin(ArrayList<FirModel> allfirmodels,ArrayList<String> trackKey, Context context,int size) {
        this.allfirmodels = allfirmodels;
        this.trackKey = trackKey;
        this.context = context;
        this.size=size;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = layoutInflater.inflate(R.layout.itemlayout, null);
//        v.setBackgroundColor(Color.parseColor("#7FDA69"));//GREEN
//        v.setBackgroundColor(Color.parseColor("#FF0000"));//RED
        TextView textView1 = v.findViewById(R.id.content);
        TextView textView2 = v.findViewById(R.id.location1);
        TextView textView3 = v.findViewById(R.id.status);




            String s1= allfirmodels.get(position).getLocation();
            String s2 = allfirmodels.get(position).getSingleFir();
            String s3 = allfirmodels.get(position).getStatus();
            String u=allfirmodels.get(position).getImgfirebaseuri();

            ImageView i=v.findViewById(R.id.i);
            Glide.with(v.getContext()).load(u).into(i);

        if(s3.equals("pending")){
            //v.setBackgroundColor(Color.parseColor("#FFFF00"));
            v.setBackgroundResource(R.drawable.roundcornerlistitempending);
            //i.setBackgroundResource(R.drawable.roundcornerlistitempending);

        }
        else if(s3.equals("Approved")){
//            v.setBackgroundColor(Color.parseColor("#7FDA69"));//GREEN
            v.setBackgroundResource(R.drawable.roundcornerlistitemaccepted);
            //i.setBackgroundResource(R.drawable.roundcornerlistitemaccepted);
        }
        else if(s3.equals("Rejected")){
//            v.setBackgroundColor(Color.parseColor("#FF0000"));//RED
            v.setBackgroundResource(R.drawable.roundcornerlistitemrejected);
            //i.setBackgroundResource(R.drawable.roundcornerlistitemrejected);
        }
        else{
//            v.setBackgroundColor(Color.parseColor("#800080"));
            v.setBackgroundResource(R.drawable.roundcornerlistitemreturned);
            //i.setBackgroundResource(R.drawable.roundcornerlistitemreturned);
        }




        textView1.setText(s1.trim());
        textView2.setText(s2.trim());
        textView3.setText(s3.trim());


        return v;
    }
}


