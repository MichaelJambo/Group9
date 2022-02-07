package com.example.nchikumbeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nchikumbeapp.databinding.ActivityHomeBinding;
import com.example.nchikumbeapp.activities.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView image;
    private DatabaseReference ref;
    ArrayList<Modelling> messagesArrayList;
    private RecyclerAdapter recyclerAdapter;
    private Context context;
    FirebaseAuth fAuth;

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fAuth = FirebaseAuth.getInstance();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_add_product, R.id.nav_my_products, R.id.nav_about_us, R.id.nav_contact_us, R.id.nav_view)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

       recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ref = FirebaseDatabase.getInstance().getReference();

        messagesArrayList = new ArrayList<>();
      clearAll();
     getDataFromFirebase();
     return;
    }

    private  void getDataFromFirebase(){
        Query query = ref.child("image");
        query.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    System.out.println(dataSnapshot.child("imageUrl").getValue());


                    System.out.println(dataSnapshot.child("ProductPrice").getValue());

                    Modelling modelling = new Modelling();
                    modelling.setImageUrl(dataSnapshot.child("imageUrl").getValue().toString());
                    modelling.setProductNAme(dataSnapshot.child("productName").getValue().toString());
                    modelling.setDescription(dataSnapshot.child("productDescription").getValue().toString());
                    modelling.setPrice(dataSnapshot.child("productPrice").getValue().toString());
                    messagesArrayList.add(modelling);

                    /*for(DataSnapshot d : dataSnapshot.getChildren()) {
                        Modelling modelling = new Modelling();
                        modelling.setImageUrl(d.child("imageUrl").getValue().toString());
                        System.out.println(d.child("targetProduct").getValue());
                        Toast.makeText(HomeActivity.this,""+d.child("targetProduct").getValue(),Toast.LENGTH_LONG).show();
                        messagesArrayList.add(modelling);

                    }*/
                }

                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),messagesArrayList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void clearAll(){
        if(messagesArrayList != null){
            messagesArrayList.clear();
            if (recyclerAdapter != null)
                recyclerAdapter.notifyDataSetChanged();
        }else
            messagesArrayList = new ArrayList<>();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
         FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}