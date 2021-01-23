package com.example.myolx;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import model.PostModel;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class SeLLFragment extends Fragment {
    android.widget.Spinner spinner;
  //  ArrayList<Categoryy> Spinner;
  //  ArrayList<Products> Products;
//    Categoryy Model;
    SpinnerAdapter spinnerAdapter;
    private double latitude,longitude;
    ImageView imageView;
    Button next;
    RadioButton rbtnnew,rbtnused;
    private static final int PICK_IMAGE  = 1;
    private static final int PICK_MULTIPLE_IMAGE  = 2;
    LocationManager locationManager;
    TextInputLayout productname, location;
    RadioGroup radioGroup;
    Bitmap bitmaps;
    int Postion;
    String ne,us,price,number,nam,n,p;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("data");
    private Uri imageUri;

    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sell");
        View view = connectViews(inflater, container);
        final RadioGroup radio = (RadioGroup) getActivity().findViewById(R.id.radioG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button
                        ne = rbtnnew.getText().toString();
                        Toast.makeText(getActivity(), ""+ne,Toast.LENGTH_LONG).show();

                        break;
                    case 1: // secondbutton
                        us = rbtnused.getText().toString();
                        Toast.makeText(getActivity(), ""+us , Toast.LENGTH_LONG).show();

                        break;
                }
            }
        });

     return view;

    }

    private View connectViews(LayoutInflater inflater, ViewGroup container) {
        storageReference = FirebaseStorage.getInstance().getReference();
         firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_se_l_l, container, false);
        spinner = view.findViewById(R.id.spinner);
        location = view.findViewById(R.id.location);
        imageView = view.findViewById(R.id.PimageView);
        next = view.findViewById(R.id.btnnext);
        productname = view.findViewById(R.id.productname);
        rbtnnew = view.findViewById(R.id.rbtnnew);
        rbtnused = view.findViewById(R.id.rbtnused);
        radioGroup = view.findViewById(R.id.radioG);
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        authStateListener= new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user!=null){

                }else {

                }
            }
        };

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            //upload single image
            public void onClick(View v) {
                Intent Gallery = new Intent(Intent.ACTION_PICK);
                Gallery.setType("image/*");
                Gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(Gallery, "Select Image"), PICK_IMAGE);

            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.addnumberlayout);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width,height);
                dialog.show();
                final EditText numbe = dialog.findViewById(R.id.NumberDialog);
                final EditText pric  = dialog.findViewById(R.id.PriceDialog);
                number = numbe.getText().toString();
                price = pric.getText().toString();
                Toast.makeText(getActivity(), "ttt" + number, Toast.LENGTH_SHORT).show();
                Button post = dialog.findViewById(R.id.post);
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   Toast.makeText(getActivity(), "fucker hamza  " + numbe.getText().toString(), Toast.LENGTH_SHORT).show();
                         postdatatosale(pric.getText().toString(),numbe.getText().toString());
                         dialog.dismiss();
                    }
                });
            }
        });



        return view;
    }

    private void postdatatosale(final String pricee, final String numberr) {

            nam = productname.getEditText().getText().toString();


        Log.d("number",numberr);
        Log.d("price",pricee);
        Toast.makeText(getActivity(), "Phone"+numberr, Toast.LENGTH_SHORT).show();
        final String cm = ne;
        final String um =us;
        if (!TextUtils.isEmpty(nam)&&imageUri!=null){
            final StorageReference filepath = storageReference
                    .child("Item_Images")
                    .child("myimage"+ Timestamp.now().getSeconds());
        filepath.putFile(imageUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
         @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                 String imageUri =  uri.toString();
                final PostModel mod = new PostModel();
                mod.setName(nam);
                if (rbtnnew.isChecked()){
                    mod.setCondition(ne);
                }else{
                    mod.setCondition(us);
                }
                mod.setNumber(numberr);
             //   mod.setPrice(Integer.parseInt(price));
                try{
                    mod.setPrice(pricee);
                } catch(NumberFormatException ex){ // handle your exception
                    Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
                }
                mod.setImageURi(imageUri);
                mod.setTimeAdd(new Timestamp(new Date()));
                collectionReference.add(mod).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_SHORT).show();
                        Log.d("dor",documentReference.toString());
                        Toast.makeText(getActivity(), "name " + number + price ,  Toast.LENGTH_SHORT).show();
                        Home home = new Home();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentc,home);
                        transaction.commit();

                           // Intent inten = new Intent(getActivity(),Home.class);
                         //startActivity(inten);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("hamza",e.toString());

                    }
                });
            }
        });
        }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Errorr",e.toString());

    }

});

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  final Uri uri = data.getData();
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            if (data!=null){
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    user =firebaseAuth.getCurrentUser();
    firebaseAuth.addAuthStateListener(authStateListener);
//        Log.d("user",user.toString());

    }

    @Override
    public void onStop() {
        super.onStop();
    if (firebaseAuth!=null){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    }

}
