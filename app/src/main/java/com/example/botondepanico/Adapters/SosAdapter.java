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
import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;

import java.util.ArrayList;

public class SosAdapter extends RecyclerView.Adapter <SosAdapter.ViewHolder>implements View.OnClickListener {

    private int resource;
    private ArrayList<IncidentSosModel> NameList;
    private View.OnClickListener listener;

    public SosAdapter(ArrayList<IncidentSosModel> NameList, int resource){
        this.NameList = NameList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);


        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        IncidentSosModel incidentSosModel = NameList.get(position);
        holder.textViewInformation.setText(incidentSosModel.getName());
        holder.textViewLocation.setText(incidentSosModel.getLocation());
        holder.textViewStatus.setText("Estado de la alerta: "+incidentSosModel.getStatus());
        holder.textViewIdSos.setText("Id: "+incidentSosModel.getIdSosAlert());

        String status = incidentSosModel.getStatus();


        Glide.with(holder.view) .load(Data_Reference.currentClient.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.ImageAvatar);


        if(incidentSosModel.getStatus().equals("Pendiente")){
            holder.imageViewStatus.setImageResource(R.drawable.ic_status_pending);
        }
            else if (incidentSosModel.getStatus().equals("Visto")){
            holder.imageViewStatus.setImageResource(R.drawable.ic_status_viewed);

        }
            else if (incidentSosModel.getStatus().equals("Atendido")){
            holder.imageViewStatus.setImageResource(R.drawable.ic_status_attended);

        }


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

        private TextView textViewIdSos;
        private TextView textViewInformation;
        private TextView textViewLocation;
        private TextView textViewStatus;
        private ImageView imageViewStatus,ImageAvatar;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            this.textViewIdSos = (TextView) view.findViewById(R.id.textViewIdSosIncident);
            this.textViewInformation = (TextView) view.findViewById(R.id.textViewInformation);
            this.textViewLocation = (TextView) view.findViewById(R.id.textLocation);
            this.textViewStatus = (TextView) view.findViewById(R.id.textStatus);
            this.imageViewStatus = (ImageView) view.findViewById(R.id.imageViewStatus);
            this.ImageAvatar = (ImageView) view.findViewById(R.id.item_image_panic);

        }


    }

}
