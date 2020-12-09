package com.abhijeet14.cetbus;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportMenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
private DatabaseReference fromPath,toPath;
private DatabaseReference runningRef;
private List<String> routeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromPath = FirebaseDatabase.getInstance().getReference().child("route");
        toPath = FirebaseDatabase.getInstance().getReference().child("running");
        runningRef = FirebaseDatabase.getInstance().getReference();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0.8f);
        actionBar.setTitle("Start Journey");
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void startJourney(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Time")
                    .setMessage("Select the journey time")
                    .setPositiveButton("Morning", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            copyNode(true);
                        }
                    }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Evening", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        copyNode(false);
                    }
                });
        dialog.show();
    }
    public void copyNode(final boolean isReverse){
        final android.app.AlertDialog pd = new SpotsDialog.Builder().setTheme(R.style.Custom).setContext(this).build();
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        fromPath.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                routeList = new ArrayList<>();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    routeList.add(d.child("route").getValue(String.class));
                }
                if(isReverse)
                    Collections.reverse(routeList);
                for(int i=0;i<routeList.size();i++){
                    final int finalI = i;
                    toPath.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().child("route").setValue(routeList.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(finalI == routeList.size()-1){
                                pd.dismiss();
                                startActivity(new Intent(MainActivity.this,RunningActivity.class));
                                finish();
                                Toast.makeText(MainActivity.this, "Routes Added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


                            /*Intent i = new Intent(MainActivity.this,RunningActivity.class);
                            i.putExtra("status","morning");
                            startActivity(i);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        final android.app.AlertDialog pd = new SpotsDialog.Builder().setTheme(R.style.Custom).setContext(this).build();
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("running").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    startActivity(new Intent(MainActivity.this,RunningActivity.class));
                    pd.dismiss();
                }else{
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == android.R.id.home) {
                this.finish();
        }if(item.getItemId() == R.id.action_logout){
             AlertDialog.Builder ad = new AlertDialog.Builder(this)
                     .setTitle("Logout")
                     .setMessage("Are you sure you want to logout ?")
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             FirebaseAuth.getInstance().signOut();
                             startActivity(new Intent(MainActivity.this,LoginActivity.class));
                             finish();
                         }
                     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                         }
                     });
             ad.show();
        }
             return super.onOptionsItemSelected(item);
    }
}
