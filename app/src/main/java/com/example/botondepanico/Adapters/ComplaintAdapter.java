package com.example.botondepanico.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.botondepanico.Activities.MainActivity;
import com.example.botondepanico.Pojos.ComplaintModel;
import com.example.botondepanico.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter <ComplaintAdapter.ViewHolder> implements View.OnClickListener{

    private int resource;
    private ArrayList<ComplaintModel> NameList;
    private View.OnClickListener listener;


    public ComplaintAdapter (ArrayList<ComplaintModel> NameList, int resource){
        this.NameList= NameList;
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

        ComplaintModel complaintModel = NameList.get(position);
        holder.textViewTitle.setText(complaintModel.getTitle());
        holder.textViewId.setText("Id: "+complaintModel.getIdSosAlert());
        holder.Location.setText("Ubicacion: "+complaintModel.getLocation());
        holder.Hour.setText(complaintModel.getHour());

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

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewTitle;
        private TextView Location;
        private TextView Hour;
        private ImageView imageViewStatus;
        public View view;

        public ViewHolder(View view) {

            super(view);
            this.view = view;

            this.textViewTitle = (TextView) view.findViewById(R.id.titleComplaint);
            this.textViewId = (TextView) view.findViewById(R.id.idComplaint);
            this.Location = (TextView)view.findViewById(R.id.textLocationComplaint);
            this.imageViewStatus = (ImageView) view.findViewById(R.id.imageViewStatus);
            this.Hour=(TextView) view.findViewById(R.id.hourComplaint);

        }

    }
}
