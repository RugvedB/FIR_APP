package com.example.letstry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginPg extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText code;
    private Button send;
    String mVerificationId;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pg_login);

        FirebaseApp.initializeApp(this);

        userIsLoggedIn();

        phoneNumber=findViewById(R.id.phoneNumber);
        code=findViewById(R.id.code);
        send=findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumber.getText().toString().trim().length()<10){
                    showSuccess("Enter Correct Number");
                }
                else if(phoneNumber.getText().toString().trim().length()==10){
                    showSuccess("Country code missing");
                }

                else if(mVerificationId!=null){
                    verifyPhoneNumberWithCode();
                }
                else{
                    startPhoneNumberVerification();
                }


            }


        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId=s;
                send.setText("Verify OTP");
            }
        };
    }

    private void verifyPhoneNumberWithCode(){
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,code.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userIsLoggedIn();
                }
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        final String[] token = new String[1];

        if(user!=null){
            String pn=user.getPhoneNumber();
            Log.d("pn",pn);
            impfun(user.getPhoneNumber());
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("pn",pn);
            intent.putExtra("token", token[0]);
            startActivity(intent);
            finish();
        }
    }
    private void startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private void impfun(final String phoneNumber) {
        final String[] token = new String[1];
        FirebaseMessaging.getInstance().subscribeToTopic("updates");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            token[0] =task.getResult().getToken();

                            Log.d("tokencheck","here1 success");
                        }else{
                            Log.d("tokencheck","here1");
                        }
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Table3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(phoneNumber)){

                    //Toast.makeText(MainActivity.this, "User exists", Toast.LENGTH_SHORT).show();
                    UserModel user=dataSnapshot.child(phoneNumber).getValue(UserModel.class);

                    ArrayList<FirModel> arrayList1=user.getArrayListOfFir();


                    UserModel temp=new UserModel(user.getPhone_number(),arrayList1,user.getToken());
                    FirebaseDatabase.getInstance().getReference("Table3").child(phoneNumber).setValue(temp);

                }
                else{


                    //Toast.makeText(MainActivity.this, "User doesnt exists", Toast.LENGTH_SHORT).show();
                    ArrayList<FirModel> arrayList1=new ArrayList<>();
                    arrayList1.add(new FirModel("Default-l","Default-f","pending","https://firebasestorage.googleapis.com/v0/b/letstry-cd943.appspot.com/o/default.jpg?alt=media&token=8a6b91eb-9e9d-474b-acdd-36f98dbd0e51"));
                    Log.d("tokencheck", token[0]);
                    UserModel temp=new UserModel(phoneNumber,arrayList1, token[0]);
                    FirebaseDatabase.getInstance().getReference("Table3").child(phoneNumber).setValue(temp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void showSuccess(String msg){
        AlertDialog.Builder d=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.info_success,null);

        TextView textView=dialogView.findViewById(R.id.msg);
        textView.setText(msg);

        d.setView(dialogView);



        final Button ok=(Button)dialogView.findViewById(R.id.ok);




        d.setTitle("Messages");

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
