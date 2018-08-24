package com.blogspot.irsyadashari.hmifapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.blogspot.irsyadashari.hmifapp.Data.SuratRecyclerAdapter;
import com.blogspot.irsyadashari.hmifapp.R;
import com.blogspot.irsyadashari.hmifapp.model.Surat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LihatArsip extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabaseReference;
    private RecyclerView recyclerView;
    private SuratRecyclerAdapter suratRecyclerAdapter;
    private List<Surat> suratList;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Spinner spinnerJenisSurat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_arsip);

        //Firebase variables init
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MSurat");
        mDatabaseReference.keepSynced(true);

        suratList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Spinner sebagai pemilih jenis surat
        spinnerJenisSurat = findViewById(R.id.spinnerJenisSuratLihatActivity);
        ArrayAdapter<CharSequence> adapterJenisSurat = ArrayAdapter.createFromResource(this,
                R.array.jenis_surat_search,
                android.R.layout.simple_spinner_item);
        adapterJenisSurat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisSurat.setAdapter(adapterJenisSurat);
        spinnerJenisSurat.setOnItemSelectedListener(this);

    }

    //Spinner Method 1
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO:saringlah konten di recyclerview sesuai dengan jenis surat yang dipilih
    }

    //Spinner Method 2
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //me CREATE objek recycler view untuk setiap objek surat.java
                Surat surat = dataSnapshot.getValue(Surat.class);
                suratList.add(surat);

                suratRecyclerAdapter = new SuratRecyclerAdapter(LihatArsip.this,suratList);
                recyclerView.setAdapter(suratRecyclerAdapter);
                suratRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
