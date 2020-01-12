package com.example.letstry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class zAdminViewAllFir extends AppCompatActivity {

    ListView zAdminLstView;
    private ArrayList<FirModel> allfirmodelsGlobal;
    private ArrayList<String> trackKeyGlobal;
    int sizeGlobal;
    private ImageView imgView;


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("letscheckk","instart");


        FirebaseDatabase.getInstance().getReference("Table3").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allfirmodelsGlobal.clear();
                trackKeyGlobal.clear();
                sizeGlobal=0;


                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d("letscheckk","key lets look at numbers: "+snapshot.getChildrenCount());


                    UserModel userModel=snapshot.getValue(UserModel.class);
                    ArrayList<FirModel> firModelArrayListofCurrentUser=userModel.getArrayListOfFir();



//                    sizeGlobal=sizeGlobal+firModelArrayListofCurrentUser.size();

//                    Log.d("letscheckk","key : "+String.valueOf(firModelArrayListofCurrentUser.size()));

                    for(FirModel x:firModelArrayListofCurrentUser){
//                        Log.d("letscheckk","key inside for loop: "+x.getLocation());
                        if(x.getLocation().equals("Default-l") && x.getSingleFir().equals("Default-f")){//i.e. it containg default initial value

                        }
                        else{
                            sizeGlobal++;
                            allfirmodelsGlobal.add(x);
                            trackKeyGlobal.add(snapshot.getKey());
                        }


                    }
                    //Log.d("letscheckk","key outsideee: ");

                }
                zAdminLstView=findViewById(R.id.zAdminLstView);
                CustomAdapterZadmin customAdapterZadmin=new CustomAdapterZadmin(allfirmodelsGlobal,trackKeyGlobal,zAdminViewAllFir.this,sizeGlobal);
                zAdminLstView.setAdapter(customAdapterZadmin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_admin_view_all_fir);
        Log.d("letscheckk","in create");

        //
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


        zAdminLstView=findViewById(R.id.zAdminLstView);
        allfirmodelsGlobal=new ArrayList<>();
        trackKeyGlobal=new ArrayList<>();

        Log.d("letscheckk","After all declarations");

        zAdminLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUpdateDialog(position);

            }
        });
        zAdminLstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//                final ImageView i=(ImageView) findViewById(R.id.i);

                String imgurl=allfirmodelsGlobal.get(position).getImgfirebaseuri();
                String loctn=allfirmodelsGlobal.get(position).getLocation();
                String sf=allfirmodelsGlobal.get(position).getSingleFir();
                String sta=allfirmodelsGlobal.get(position).getStatus();
//                String u=allfirmodelsGlobal.get(position).getImgfirebaseuri();

//                Glide.with(getApplicationContext()).load(u).into(i);
                Log.d("imgurlz",imgurl);


                Intent intent=new Intent(getApplicationContext(),showUserImgHere.class);
                intent.putExtra("imgurl",imgurl);
                intent.putExtra("location",loctn);
                intent.putExtra("fir",sf);
                intent.putExtra("status",sta);
                getApplicationContext().startActivity(intent);
                return true;

            }
        });

        Log.d("letscheckk","ending oncreate");

    }


    private void showUpdateDialog(final int position) {
        String loctn=allfirmodelsGlobal.get(position).getLocation();
        String fir=allfirmodelsGlobal.get(position).getSingleFir();
        String stat=allfirmodelsGlobal.get(position).getStatus();
        final String imgfirebaseuri=allfirmodelsGlobal.get(position).getImgfirebaseuri();

        Log.d("showd","String at "+position+"  :   "+loctn);




        ////////////////
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);

        LayoutInflater inflater=getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.zadmin_dialogbox_layout,null);

        dialogBuilder.setView(dialogView);

        final TextView textView1=(TextView)dialogView.findViewById(R.id.textView1);
        final TextView textView2=(TextView)dialogView.findViewById(R.id.textView2);
        final EditText editTextStatus=(EditText)dialogView.findViewById(R.id.editTextStatus);

//

        textView1.setText(allfirmodelsGlobal.get(position).getLocation());
        textView2.setText(allfirmodelsGlobal.get(position).getSingleFir());
        editTextStatus.setText(allfirmodelsGlobal.get(position).getStatus());

        final Button buttonUpdate=(Button)dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete=(Button)dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Update Dialog Box");

        final AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();
        /////////////////
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedStatus=editTextStatus.getText().toString().trim();



                if(updatedStatus.equals("")){
                    editTextStatus.setError("Name Required");
                    return;
                }

                updateArtist(position,trackKeyGlobal.get(position),textView1.getText().toString().trim(),textView2.getText().toString().trim(),updatedStatus,imgfirebaseuri);

                alertDialog.dismiss();

            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(position,trackKeyGlobal.get(position));
                alertDialog.dismiss();
            }
        });

    }
    private void deleteArtist(final int position,final String currId) {
        Log.d("letscheck",String.valueOf(position)+"  --  "+currId);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Table3").child(currId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel userModel = dataSnapshot.getValue(UserModel.class);


                ArrayList<FirModel> f = userModel.getArrayListOfFir();
                String p = userModel.getPhone_number();
                String token = userModel.getToken();


//                f.remove(position);//wrong
                int pzz=0;
                for(FirModel x:f){

                    if((x.getLocation().equals(allfirmodelsGlobal.get(position).getLocation()))  &&  (x.getSingleFir().equals(allfirmodelsGlobal.get(position).getSingleFir()))){
                        Log.d("letscheck",x.getSingleFir()+"   inside   "+allfirmodelsGlobal.get(position).getSingleFir());
                        if(f.size()==1 && f.get(0).getLocation().equals("Default-l") && f.get(0).getSingleFir().equals("Default-f")){//i.e. it containg default initial value
                            Toast.makeText(getApplicationContext(), "Cannot remove default initial entry unless you add your first fir", Toast.LENGTH_SHORT).show();
                        }
                        else if(f.size()==1){
                            //i.e only one element is present and it is valid...but we cant let the list be empty
                            f.clear();
                            f.add(new FirModel("Default-l","Default-f","pending","https://firebasestorage.googleapis.com/v0/b/letstry-cd943.appspot.com/o/default.jpg?alt=media&token=8a6b91eb-9e9d-474b-acdd-36f98dbd0e51"));
                        }
                        else{
                            f.remove(pzz);
                        }
                   


                        break;
                    }
                    pzz++;
                }

                UserModel temp = new UserModel(p, f,token);
                FirebaseDatabase.getInstance().getReference("Table3").child(currId).setValue(temp);

                zAdminLstView = findViewById(R.id.zAdminLstView);
                CustomAdapterZadmin customAdapterZadmin=new CustomAdapterZadmin(allfirmodelsGlobal,trackKeyGlobal,zAdminViewAllFir.this,sizeGlobal);
                zAdminLstView.setAdapter(customAdapterZadmin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


        private void updateArtist ( final int position, final String currId, final String location, final String fir,final String updatedStatus,final String imgfirebaseuri){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Table3").child(currId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    ArrayList<FirModel> f = userModel.getArrayListOfFir();
                    String p = userModel.getPhone_number();
                    String token = userModel.getToken();


//                    f.set(position, firModel);//wrong

                    int pzz=0;
                    for(FirModel x:f){

                        if((x.getLocation().equals(allfirmodelsGlobal.get(position).getLocation()))  &&  (x.getSingleFir().equals(allfirmodelsGlobal.get(position).getSingleFir()))){
                            Log.d("letscheck",x.getSingleFir()+"   inside   "+allfirmodelsGlobal.get(position).getSingleFir());
//                            f.remove(pzz);
                            f.set(pzz,new FirModel(location,fir,updatedStatus,imgfirebaseuri));


                            break;
                        }
                        pzz++;
                    }

                    UserModel temp = new UserModel(p, f,token);
                    FirebaseDatabase.getInstance().getReference("Table3").child(currId).setValue(temp);
                    //
                    String CHANNEL_ID="simplified";
                    String CHANNEL_NAME="FIRe";
                    String CHANNEL_DESC="my project notifications";

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

                        NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                        notificationChannel.setDescription(CHANNEL_DESC);
                        NotificationManager manager=getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(notificationChannel);

                    }
                    displayNotification(CHANNEL_ID);
                    //

                    zAdminLstView = findViewById(R.id.zAdminLstView);
                    CustomAdapterZadmin customAdapterZadmin=new CustomAdapterZadmin(allfirmodelsGlobal,trackKeyGlobal,zAdminViewAllFir.this,sizeGlobal);
                    zAdminLstView.setAdapter(customAdapterZadmin);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            Log.d("showd", "Inside updateArtist");

        }

        //
        private void displayNotification(String p){

            NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(getApplicationContext(),p)
                    .setSmallIcon(R.drawable.police)
                    .setContentTitle("this is the titleee")
                    .setContentText("this is content from notification...long contexttttttttttttttttttt")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);

            notificationManagerCompat.notify(1,mBuilder.build());

        }
        //

    }








