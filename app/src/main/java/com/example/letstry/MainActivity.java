package com.example.letstry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;


import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    public final static String CHANNEL_ID="simplified";
    private final String CHANNEL_NAME="FIRe";
    private final String CHANNEL_DESC="my project notifications";
    //private EditText phoneNumber;
    private String phoneNumber,token;
    String adminPhoneNumber="+91XXXXXXXXXX";

    private ImageButton sendNumber,nearByPoliceStation,feedbackButton,ViewAllMyFir,zAdmin,logout;;



    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        phoneNumber=getIntent().getStringExtra("pn");


        sendNumber=findViewById(R.id.sendNumber);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),LoginPg.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });




        sendNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Page2.class);
                myIntent.putExtra("currId", phoneNumber); //Optional parameters

                MainActivity.this.startActivity(myIntent);


            }
        });



        zAdmin=findViewById(R.id.zAdmin);
        zAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumber.equals(adminPhoneNumber)){//i.e. it is admin account
                    zAdmin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("posi","admin click");
                            Intent intent=new Intent(MainActivity.this,zAdminViewAllFir.class);
                            MainActivity.this.startActivity(intent);
                        }
                    });
                }
                else{
//            zAdmin.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "You do not have admin access", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nearByPoliceStation=findViewById(R.id.nearByPoliceStation);
        nearByPoliceStation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.google.com/maps/search/nearby+police+station"));
                startActivity(viewIntent);
            }
        });
        feedbackButton=findViewById(R.id.feedbackButton);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),feedbackActivity.class);
                intent.putExtra("pn",phoneNumber);
                startActivity(intent);
            }
        });

        ViewAllMyFir=findViewById(R.id.ViewAllMyFir);
        ViewAllMyFir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, Page3.class);
                myIntent.putExtra("currId",phoneNumber);
                MainActivity.this.startActivity(myIntent);


            }
        });

    }
}

