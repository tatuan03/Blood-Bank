package com.example.bloodbanknaut.Adapters;

import static android.Manifest.permission.CALL_PHONE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbanknaut.DataModels.RequestDataModel;
import com.example.bloodbanknaut.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<RequestDataModel> dataSet;
    private Context context;

    public RequestAdapter(
            List<RequestDataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {
        holder.message.setText(dataSet.get(position).getMessage());
        Glide.with(context).load(dataSet.get(position).getUrl()).into(holder.imgView);
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ti lam
                if(PermissionChecker.checkSelfPermission(context,CALL_PHONE)
                        != PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+dataSet.get(holder.getAdapterPosition()).getPhoneNum()));
                    context.startActivity(intent);
                }
                else {
                    ((Activity) context).requestPermissions(new String[]{CALL_PHONE}, 401);
                }
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        holder.message.getText().toString() + "\n\nContact: "+ dataSet.get(holder.getAdapterPosition()).getPhoneNum());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey, could you help me here");
                context.startActivity(Intent.createChooser(shareIntent,"Share...."));
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView imgView,callButton,shareButton;

        ViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            imgView = itemView.findViewById(R.id.img);
            callButton = itemView.findViewById(R.id.callButton);
            shareButton = itemView.findViewById(R.id.shareButton);
        }

    }
}
