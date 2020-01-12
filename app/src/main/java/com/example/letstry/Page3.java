package com.example.letstry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Page3 extends AppCompatActivity {

    ListView lst;
    private ArrayList<FirModel> stringArrayList;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("pg3","instart");
        FirebaseDatabase.getInstance().getReference("Table3").child(getIntent().getStringExtra("currId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringArrayList.clear();
                UserModel user = dataSnapshot.getValue(UserModel.class);
                stringArrayList =  user.getArrayListOfFir();

                lst=findViewById(R.id.lst);
                CustomAdapter customAdapter=new CustomAdapter(stringArrayList,Page3.this);
                lst.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        //trying to show user about colors
        AlertDialog.Builder d=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.info_layout,null);

        d.setView(dialogView);


        final Button ok=(Button)dialogView.findViewById(R.id.ok);




        d.setTitle("Instructions");

        final AlertDialog alertDialog=d.create();
        alertDialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //

        //lst=findViewById(R.id.lst);
        Log.d("pg3","increadte");

        lst=findViewById(R.id.lst);
        stringArrayList=new ArrayList<>();


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUpdateDialog(position);

            }
        });
        lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                String imgurl=stringArrayList.get(position).getImgfirebaseuri();
                String loctn=stringArrayList.get(position).getLocation();
                String sf=stringArrayList.get(position).getSingleFir();
                String sta=stringArrayList.get(position).getStatus();

                Log.d("imgurlz",imgurl);
                Intent intent=new Intent(getApplicationContext(),showUserImgHere.class);
                intent.putExtra("imgurl",imgurl);
                intent.putExtra("location",loctn);
                intent.putExtra("fir",sf);
                intent.putExtra("status",sta);

                startActivity(intent);
                return true;

            }
        });



    }

    private void showUpdateDialog(final int position) {
        String loctn=stringArrayList.get(position).getLocation();
        String fir=stringArrayList.get(position).getSingleFir();
        final String imgfirebaseuri=stringArrayList.get(position).getImgfirebaseuri();

        Log.d("showd","String at "+position+"  :   "+loctn);
        Log.d("showd","currId : "+getIntent().getStringExtra("currId"));

        ////////////////
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);

        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.update_layout,null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName=(EditText)dialogView.findViewById(R.id.editTextName);
        final EditText editTextName2=(EditText)dialogView.findViewById(R.id.editTextName2);
        final Button buttonUpdate=(Button)dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete=(Button)dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Update Dialog Box");

        final AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();
        /////////////////
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location=editTextName.getText().toString().trim();
                String fir=editTextName2.getText().toString().trim();


                if(TextUtils.isEmpty(location)||TextUtils.isEmpty(fir)){
                    editTextName.setError("Name Required");
                    return;
                }

                updateArtist(position,getIntent().getStringExtra("currId"),location,fir,imgfirebaseuri);

                alertDialog.dismiss();

            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(position,getIntent().getStringExtra("currId"));
                alertDialog.dismiss();
            }
        });

    }
    private void deleteArtist(final int position,final String currId){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Table3").child(currId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel=dataSnapshot.getValue(UserModel.class);
                ArrayList<FirModel> f=userModel.getArrayListOfFir();
                String p=userModel.getPhone_number();

                if(f.size()==1 && f.get(0).getLocation().equals("Default-l") && f.get(0).getSingleFir().equals("Default-f")){//i.e. it containg default initial value
                    Toast.makeText(Page3.this, "Cannot remove default initial entry unless you add your first fir", Toast.LENGTH_SHORT).show();
                }
                else if(f.size()==1){
                    //i.e only one element is present and it is valid...but we cant let the list be empty
                    f.clear();
                    f.add(new FirModel("Default-l","Default-f","pending","https://firebasestorage.googleapis.com/v0/b/letstry-cd943.appspot.com/o/default.jpg?alt=media&token=8a6b91eb-9e9d-474b-acdd-36f98dbd0e51"));
                }
                else{
                    f.remove(position);
                }

                

                UserModel temp=new UserModel(p,f, userModel.getToken());
                FirebaseDatabase.getInstance().getReference("Table3").child(currId).setValue(temp);

                lst=findViewById(R.id.lst);
                CustomAdapter customAdapter=new CustomAdapter(f,Page3.this);
                lst.setAdapter(customAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    private void updateArtist(final int position, final String currId, final String location, final String fir,final String imgfirebaseuri) {
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Table3").child(currId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel userModel=dataSnapshot.getValue(UserModel.class);
                    ArrayList<FirModel> f=userModel.getArrayListOfFir();
                    String p=userModel.getPhone_number();

                    FirModel firModel=new FirModel(location,fir,"pending",imgfirebaseuri);
                    f.set(position,firModel);

                    UserModel temp=new UserModel(p,f,userModel.getToken());
                    FirebaseDatabase.getInstance().getReference("Table3").child(currId).setValue(temp);

                    lst=findViewById(R.id.lst);
                    CustomAdapter customAdapter=new CustomAdapter(f,Page3.this);
                    lst.setAdapter(customAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        Log.d("showd","Inside updateArtist");

    }
}
