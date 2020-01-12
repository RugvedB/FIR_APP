package com.example.letstry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class feedbackActivity extends AppCompatActivity {
    Button sendFeedback;
    EditText writeFeedback;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        phoneNumber=getIntent().getStringExtra("pn");

        writeFeedback=findViewById(R.id.writeFeedback);
        sendFeedback=findViewById(R.id.sendFeedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkInternet()){
                    showSuccess("Requires Internet");
                }else {

                    String key = FirebaseDatabase.getInstance().getReference("Feedback").push().getKey();
                    if (writeFeedback.getText().toString().trim().equals("")) {
//                    Toast.makeText(feedbackActivity.this, "Cannot submit empty feedback", Toast.LENGTH_SHORT).show();
                        showSuccess("Feedback Empty");
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Feedback").child(key).setValue(writeFeedback.getText().toString().trim());
//                    Toast.makeText(feedbackActivity.this, "Thank you for your valuable feedback", Toast.LENGTH_SHORT).show();
                        showSuccess("Success");
                        writeFeedback.setText("");
                    }
                }






            }
        });
    }
    boolean checkInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else
            return false;
    }
    void showSuccess(String msg){
        AlertDialog.Builder d=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.info_success,null);

        TextView textView=dialogView.findViewById(R.id.msg);
        textView.setText(msg);

        d.setView(dialogView);



        final Button ok=(Button)dialogView.findViewById(R.id.ok);




        d.setTitle("Message:");

        final AlertDialog alertDialog=d.create();
        alertDialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
