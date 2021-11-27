package com.example.botondepanico.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.botondepanico.Pojos.NotificationsModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter <NotificationsAdapter.ViewHolder>implements View.OnClickListener {

    private int resource;
    private ArrayList<NotificationsModel> NameList;
    private View.OnClickListener listener;

    public  NotificationsAdapter(ArrayList<NotificationsModel> NameList,int resource){
        this.NameList = NameList;
        this.resource = resource;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);


        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {

        NotificationsModel notificationsModel = NameList.get(position);

        holder.textViewId.setText(notificationsModel.getId());
        holder.textViewTitle.setText(notificationsModel.getTitle());
        holder.textViewDescription.setText(notificationsModel.getBriefDescription());
        holder.textViewHour.setText(notificationsModel.getHourPublished());

        Glide.with(holder.view) .load(notificationsModel.getImageNotification())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.icono_denuncias)
                .into(holder.ImageAvatar);



    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {

        if(listener!=null){
            listener.onClick(v);
        }


    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewTitle;
        private TextView textViewHour;
        private TextView textViewDescription;
        private TextView textViewId;
        private ImageView ImageAvatar;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            this.textViewTitle= (TextView) view.findViewById(R.id.TitleNotification);
            this.textViewDescription = (TextView) view.findViewById(R.id.DescriptionNotification);
            this.ImageAvatar = (ImageView) view.findViewById(R.id.item_image_notification);
            this.textViewHour = (TextView) view.findViewById(R.id.hourPostNotification);
            this.textViewId = (TextView) view.findViewById(R.id.IdPostNotification);

        }


    }


}
