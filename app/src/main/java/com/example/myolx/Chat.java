package com.example.myolx;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Chat extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    UserAdapter adapter;
    RecyclerView recycleruser;

    private ArrayList<ModelRegister> UserList;
   private SearchView searchView;


    final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Register");


//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate( R.menu.search, menu);
//        System.out.println( "inflating menu");
//
//    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search,menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchh)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getActivity().getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                menu.findItem(R.id.searchh).collapseActionView();
                Log.e("queryText",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search goes here !!
               // adapter.getFilter().filter(query);
                //adapter.getFilter
                Log.e("queryText",newText);
                return false;
            }


        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //SearchView searchView;
        switch (item.getItemId()) {

            case R.id.searchh:



                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Chat");


        View vie = inflater.inflate(R.layout.fragment_profile,container,false);
        recycleruser = vie.findViewById(R.id.userRecycler);

        UserList = new ArrayList<>();
        recycleruser.setLayoutManager(new LinearLayoutManager(vie.getContext()));
        firebaseAuth  = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        adapter=new UserAdapter(UserList,getActivity());
        getuser();
        return vie;
    }
    private void getuser() {
         nm.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                     for (DataSnapshot npsnapshot : snapshot.getChildren()) {

                         ModelRegister l = npsnapshot.getValue(ModelRegister.class);
                          String name = l.getName();
                         Log.d("nam", "onDataChange: "+name);
                         if(!user.getUid().equals(l.getId())){
                             UserList.add(l);
                         }
                     }


                     adapter=new UserAdapter(UserList,getActivity());
                     recycleruser.setAdapter(adapter);
                     LayoutAnimationController animationController =
                             AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_slid_right);
                     recycleruser.setLayoutAnimation(animationController);
                     recycleruser.getAdapter().notifyDataSetChanged();
                     recycleruser.scheduleLayoutAnimation();


                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(getActivity(), "Error"+error.toString(), Toast.LENGTH_LONG).show();
             }
         });
    }
}