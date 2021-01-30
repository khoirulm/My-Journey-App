package com.kmproject.myjourney;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.myViewHolder> {

    Context context;
    ArrayList<MyTicket> myTicket;

    public TicketAdapter(Context c, ArrayList<MyTicket> p){
        context = c;
        myTicket = p;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i ){
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myticket, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
            myViewHolder.namaWisata.setText(myTicket.get(i).getNama_wisata());
            myViewHolder.lokasi.setText(myTicket.get(i).getLokasi());
            myViewHolder.jumlahTicket.setText(myTicket.get(i).getJumlah_tiket() + " Ticket");

            final String getNamaWisata = myTicket.get(i).getNama_wisata();

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToMyTicketDetails = new Intent(context, MyTicketDetailAct.class);
                    goToMyTicketDetails.putExtra("nama_wisata", getNamaWisata);
                    context.startActivity(goToMyTicketDetails);
                }
            });
    }

    @Override
    public int getItemCount() { return myTicket.size();}

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView namaWisata,lokasi,jumlahTicket;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            namaWisata = itemView.findViewById(R.id.nama_wisata);
            lokasi = itemView.findViewById(R.id.lokasi);
            jumlahTicket = itemView.findViewById(R.id.jumlah_tiket);
        }
    }
}
