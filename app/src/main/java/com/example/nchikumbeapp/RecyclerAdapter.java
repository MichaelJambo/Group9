package com.example.nchikumbeapp;

import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ImageView imageView;
    TextView textView1, textView2,textView3;
    private Context mContext;
    ArrayList<Modelling> messageLists;


    public RecyclerAdapter(Context mContext,ArrayList<Modelling> messageLists) {

        this.mContext = mContext;
        this.messageLists = messageLists;
    }




    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        textView1.setText(messageLists.get(position).getProductNAme());
        textView2.setText(messageLists.get(position).getPrice());
        textView3.setText(messageLists.get(position).getDescription());
        Glide.with(mContext).load(messageLists.get(position).getImageUrl()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }

}
