package com.example.nchikumbeapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nchikumbeapp.Modelling;
import com.example.nchikumbeapp.R;
import com.example.nchikumbeapp.RecyclerAdapter;
import com.example.nchikumbeapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView image;
    private DatabaseReference ref;
    ArrayList<Modelling> messagesArrayList;
    private RecyclerAdapter recyclerAdapter;
    private Context context;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ref = FirebaseDatabase.getInstance().getReference();

        messagesArrayList = new ArrayList<>();
        clearAll();
        getDataFromFirebase();
        return root;
    }
    private  void getDataFromFirebase(){
        Query query = ref.child("image");
        query.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    System.out.println("This has a values of "+dataSnapshot.child("productName").getValue());


                       System.out.println(dataSnapshot.child("ProductPrice").getValue());

                        Modelling modelling = new Modelling();
                        modelling.setImageUrl(dataSnapshot.child("imageUrl").getValue().toString());
                        modelling.setProductNAme(dataSnapshot.child("productName").getValue().toString());
                        modelling.setDescription(dataSnapshot.child("productDescription").getValue().toString());
                        modelling.setPrice(dataSnapshot.child("productPrice").getValue().toString());
                        messagesArrayList.add(modelling);

                }

                recyclerAdapter = new RecyclerAdapter(getContext(),messagesArrayList);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}