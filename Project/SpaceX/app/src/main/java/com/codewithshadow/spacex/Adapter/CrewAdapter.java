package com.codewithshadow.spacex.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.codewithshadow.spacex.Model.CrewModel;
import com.codewithshadow.spacex.R;
import com.codewithshadow.spacex.RoomDatabase.RoomDB;
import com.codewithshadow.spacex.Activity.WikipediaActivity;
import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.MyViewHolder> {
    private Context aCtx;
    private List<CrewModel> list;
    private RoomDB database;


    public CrewAdapter(Context aCtx, List<CrewModel> list) {
        this.aCtx = aCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public CrewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(aCtx).inflate(R.layout.card_useritem, parent, false);
        return new CrewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewAdapter.MyViewHolder holder, int position) {
        CrewModel model = list.get(position);

        database = RoomDB.getInstance(aCtx);

        holder.name.setText(model.getName());
        holder.agency.setText(model.getAgency());
        holder.url.setText(model.getWikipedia());
        holder.active.setText(model.getStatus());
        RequestOptions myOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(aCtx).load(model.getImage())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(myOptions)
                .into(holder.userImage);

        if (model.getStatus().equals("active"))
        {
            holder.card_img.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(aCtx, WikipediaActivity.class);
                intent.putExtra("url",model.getWikipedia());
                aCtx.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,agency,url,active;
        ImageView userImage;
        CardView card_img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_text);
            userImage = itemView.findViewById(R.id.item_image);
            card_img = itemView.findViewById(R.id.item_badge_card_parent);
            agency = itemView.findViewById(R.id.item_label);
            url = itemView.findViewById(R.id.item_label_link);
            active = itemView.findViewById(R.id.item_active);

        }
    }
}

