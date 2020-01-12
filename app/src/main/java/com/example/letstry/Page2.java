package com.example.letstry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Page2 extends AppCompatActivity {

    EditText location;
    EditText singleFir;
    Button fileFir;
    String CurrImgUrl;
    ImageView tryimg;


//IMG
private Button btnChoose;
    private ImageView imageView;

    private Uri mImageUri;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 2;

    FirebaseStorage storage;
    StorageReference storageReference;

    String currId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();

        currId=getIntent().getStringExtra("currId");


        btnChoose = (Button) findViewById(R.id.btnChoose);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternet()){
                    chooseImage();
                }
                else{
                    Toast.makeText(Page2.this, "Cannot upload photo if internet is off", Toast.LENGTH_SHORT).show();
                }

            }
        });




        location=findViewById(R.id.location1);
        singleFir=findViewById(R.id.singleFir);
        fileFir=findViewById(R.id.fileFir);
        tryimg=findViewById(R.id.tryimg);


        fileFir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternet()){
                    uploadImage();
                }
                else{
                    Toast.makeText(Page2.this, "Cannot file FIR if internet is off", Toast.LENGTH_SHORT).show();
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


    private void impfun(final String mImageUriInImpFuntn){
        String l=location.getText().toString().trim();
        String f=singleFir.getText().toString().trim();
        final String id=getIntent().getStringExtra("currId");

        FirebaseDatabase.getInstance().getReference().child("Table3").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);
                ArrayList<FirModel> arrayList1=user.getArrayListOfFir();

//                FirModel firModel=new FirModel(location.getText().toString().trim(),singleFir.getText().toString().trim(),"pending");
                FirModel firModel=new FirModel(location.getText().toString().trim(),singleFir.getText().toString().trim(),"pending",mImageUriInImpFuntn.toString());//Just checking

                if(arrayList1.size()==1 && arrayList1.get(0).getLocation().equals("Default-l") && arrayList1.get(0).getSingleFir().equals("Default-f")){//i.e. it containg default initial value
                    arrayList1.clear();//removing default values
                }
                arrayList1.add(firModel);

                UserModel temp=new UserModel(user.getPhone_number(),arrayList1,user.getToken());
//                        uploadImage();
                Log.d("completion?","yes");
                FirebaseDatabase.getInstance().getReference("Table3").child(id).setValue(temp);
                location.setText("");
                singleFir.setText("");
                tryimg.setImageResource(android.R.color.transparent);


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


    //IMG
//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            mImageUri = data.getData();//imgUri


            Log.d("imgstuff",mImageUri.toString());

            final ImageView tryimg=(ImageView) findViewById(R.id.tryimg);
            CurrImgUrl=mImageUri.toString();
            Glide.with(getApplicationContext()).load(CurrImgUrl).into(tryimg);



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                //imageView.setImageBitmap(bitmap);
                btnChoose=findViewById(R.id.btnChoose);
                btnChoose.setText("Successfully Uploaded");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {


        if(mImageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String l=location.getText().toString().trim();
            String f=singleFir.getText().toString().trim();
            Log.d("imgg",l.concat(f));


            final StorageReference ref = storageReference.child("images/"+currId+"/"+ UUID.randomUUID().toString());
            ref.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                          @Override
                                                                          public void onSuccess(Uri uri) {

                                                                              String imageUrl = uri.toString();
                                                                              progressDialog.dismiss();
                                                                              impfun(imageUrl);
                                                                              showSuccess("Success");
                                                                              btnChoose.setText("CHOOSE");
                                                                              //Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      });
                            //




                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showSuccess("Success");
                            //Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else if(location.getText().toString().trim().equals("")){
            Toast.makeText(this, "Location field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(singleFir.getText().toString().trim().equals("")){
            Toast.makeText(this, "FIR field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Upload atleast single image", Toast.LENGTH_SHORT).show();
        }
    }


}
