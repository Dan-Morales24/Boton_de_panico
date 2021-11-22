package com.example.botondepanico.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botondepanico.Pojos.ComplaintModel;
import com.example.botondepanico.R;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter <ComplaintAdapter.ViewHolder> {

    private int resource;
    private ArrayList<ComplaintModel> NameList;

    public ComplaintAdapter (ArrayList<ComplaintModel> NameList, int resource){
        this.NameList= NameList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ComplaintModel complaintModel = NameList.get(position);
         holder.textViewId.setText(complaintModel.getIdSosAlert());
        holder.textViewName.setText(complaintModel.getName());
        holder.textViewTitle.setText(complaintModel.getTitle());
        holder.textViewStatus.setText(complaintModel.getStatus());
        holder.Location.setText(complaintModel.getLocation());

        String status = complaintModel.getStatus();


        if(status.equals("Pendiente")){
            holder.imageViewStatus.setImageResource(R.drawable.ic_status_pending);
        }
        else if (status.equals("Visto")){
            holder.imageViewStatus.setImageResource(R.drawable.ic_status_viewed);

        }
        else if (status.equals("Atendido")){
            holder.imageViewStatus.setImageResource(R.drawable.ic_status_attended);

        }



    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewTitle;
        private TextView textViewName;
        private TextView textViewStatus;
        private TextView Location;
        private ImageView imageViewStatus;
        public View view;

        public ViewHolder(View view) {

            super(view);
            this.view = view;

            this.textViewTitle = (TextView) view.findViewById(R.id.titleComplaint);
            this.textViewId = (TextView) view.findViewById(R.id.idComplaint);
            this.textViewName = (TextView) view.findViewById(R.id.nameComplaint);
            this.textViewStatus = (TextView) view.findViewById(R.id.statusComplaint);
            this.Location = (TextView)view.findViewById(R.id.textLocationComplaint);
            this.imageViewStatus = (ImageView) view.findViewById(R.id.imageViewStatus);

        }

    }
}
