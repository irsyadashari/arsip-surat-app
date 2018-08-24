package com.blogspot.irsyadashari.hmifapp.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.irsyadashari.hmifapp.Activities.InputArsip;
import com.blogspot.irsyadashari.hmifapp.R;
import com.blogspot.irsyadashari.hmifapp.model.Surat;

import org.w3c.dom.Text;

import java.util.List;

public class SuratRecyclerAdapter extends RecyclerView.Adapter<SuratRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Surat> suratList;

    public SuratRecyclerAdapter(Context context, List<Surat> suratList) {
        this.context = context;
        this.suratList = suratList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Surat suratObj = suratList.get(position);

        // set surat.getVariables into holder.description;
        String descriptionTexts = "Jenis Surat :\n"+suratObj.getJenisSurat()+"\n\n"+
                                  "Perihal Surat :\n"+suratObj.getPerihalSurat()+"\n\n"+
                                  "Keterangan Surat :\n"+suratObj.getKeteranganSurat()+"\n\n"+
                                  "Filename :\n"+suratObj.getDisplayName()+"\n\n"+
                                  "Filesize :"+suratObj.getFileSizeValue()+" bytes";

        holder.description.setText(descriptionTexts);

        String kalimatTglFormat = "Date Created : " + suratObj.getTanggalSurat();
        holder.timestamp.setText(kalimatTglFormat);
    }

    @Override
    public int getItemCount() {
        return suratList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public TextView timestamp;

        public ViewHolder(View view,Context ctx) {
            super(view);

            context = ctx;

            //Binding all widgets into the RecyclerView
            description = view.findViewById(R.id.descriptionTextView);
            timestamp = view.findViewById(R.id.timestampList);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //jika view di klik
                }
            });
        }
    }

}
