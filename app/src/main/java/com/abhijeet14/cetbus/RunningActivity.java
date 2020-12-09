package com.abhijeet14.cetbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.abhijeet14.cetbus.model.Running;
import com.abhijeet14.cetbus.viewholder.RunningViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RunningActivity extends AppCompatActivity {
    private RecyclerView listRoutes;
    private DatabaseReference runningRef;
    private FirebaseRecyclerAdapter<Running,RunningViewHolder> f;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0.8f);
        actionBar.setTitle("Upcoming Routes");
        actionBar.setDisplayHomeAsUpEnabled(true);

        runningRef = FirebaseDatabase.getInstance().getReference().child("running").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        listRoutes = findViewById(R.id.running_routes_container);
        listRoutes.hasFixedSize();
        listRoutes.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));


        FirebaseRecyclerOptions<Running> options = new FirebaseRecyclerOptions.Builder<Running>().setQuery(runningRef,Running.class).build();
        f = new FirebaseRecyclerAdapter<Running, RunningViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RunningViewHolder holder, final int position, @NonNull Running model) {
                runningRef.child(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        final DatabaseReference d=getRef(position);
                        runningRef.child(getRef(position).getKey()).removeEventListener(this);
                        holder.setRouteName(dataSnapshot.child("route").getValue(String.class));
                        holder.routeCard().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RunningActivity.this);
                                dialog.setTitle("Delete Route ?");
                                dialog.setMessage("Are you sure the bus has crossed this stoppage ?");
                                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        d.removeValue();
                                        checkNode();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dialog.show();
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RunningActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public RunningViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(RunningActivity.this).inflate(R.layout.running_layout,viewGroup,false);
                return new RunningViewHolder(view);
            }
        };
        listRoutes.setAdapter(f);
    }
    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        f.stopListening();
    }
    public void checkNode(){
        DatabaseReference runningCheckRef = FirebaseDatabase.getInstance().getReference();
        runningCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("running").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                }else{
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
