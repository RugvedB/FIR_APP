package com.example.letstry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class showUserImgHere extends AppCompatActivity {
    ImageView showUserImage;
    TextView location,fir,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_img_here);

        showUserImage=findViewById(R.id.showUserImage);
        location=findViewById(R.id.location);
        fir=findViewById(R.id.fir);
        status=findViewById(R.id.status);

        String imgurl=getIntent().getStringExtra("imgurl");
        String location1=getIntent().getStringExtra("location");
        String fir1=getIntent().getStringExtra("fir");
        String status1=getIntent().getStringExtra("status");

        Log.d("imgurlz","inside show  "+imgurl);
        Glide.with(getApplicationContext()).load(imgurl).into(showUserImage);
        location.setText(location1);
        fir.setText(fir1);
        status.setText(status1);
    }
}
