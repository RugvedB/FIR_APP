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

public class CustomAdapter extends BaseAdapter {

    private ArrayList<FirModel> country_name;
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomAdapter(ArrayList<FirModel> country_name, Context context) {
        this.country_name = country_name;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return country_name.size();
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
        TextView textView1 = v.findViewById(R.id.content);
        TextView textView2 = v.findViewById(R.id.location1);
        TextView textView3 = v.findViewById(R.id.status);



            String s1= country_name.get(position).getLocation();
            String s2 = country_name.get(position).getSingleFir();
            String s3 = country_name.get(position).getStatus();
        String u=country_name.get(position).getImgfirebaseuri();

        ImageView i=v.findViewById(R.id.i);
        Glide.with(v.getContext()).load(u).into(i);

        if(s3.equals("pending")){
            v.setBackgroundResource(R.drawable.roundcornerlistitempending);
        }
        else if(s3.equals("Approved")){
            v.setBackgroundResource(R.drawable.roundcornerlistitemaccepted);
        }
        else if(s3.equals("Rejected")){
            v.setBackgroundResource(R.drawable.roundcornerlistitemrejected);
        }
        else{
            v.setBackgroundResource(R.drawable.roundcornerlistitemreturned);
        }




        textView1.setText(s1.trim());
        textView2.setText(s2.trim());
        textView3.setText(s3.trim());


        return v;
    }
}





