package com.example.richardgonzalez.plataformadesalto.Actividades.Recycler;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.POJO.Medidas;
import com.example.richardgonzalez.plataformadesalto.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class RecyclerAdapterHistory extends RecyclerView.Adapter<RecyclerAdapterHistory.RecyclerViewHolder> {

    ArrayList<Medidas> arrayList = new ArrayList<>();
    public RecyclerAdapterHistory(ArrayList<Medidas> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final Medidas medidas = arrayList.get(position);

        String cambio1="T.Vuelo: "+medidas.getVuelo();
        holder.vuelo.setText(cambio1);
        String cambio2= medidas.getFecha();
        holder.fecha.setText(cambio2);
        String cambio3 = "Altura: "+medidas.getAltura();
        holder.altura.setText(cambio3);
        String cambio4 = "T.Reacción: "+ medidas.getReaccion();
        holder.reaccion.setText(cambio4);
        String cambio5 = "Potencia: "+ medidas.getPotencia();
        holder.potencia.setText(cambio5);
        String cambio6 = "T.Apoyo: "+ medidas.getApoyo();
        holder.apoyo.setText(cambio6);


        holder.graficar.setTag(arrayList.get(position));

        holder.graficar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
               if ( cb.isChecked() ) {
                    Constant.Altura2.add(medidas.getAltura());
                    Constant.TV2.add(medidas.getVuelo());
                    Constant.TR2.add(medidas.getReaccion());
                    Constant.P2.add(medidas.getPotencia());
                    Constant.Apoyo2.add(medidas.getApoyo());
                   Log.d("selecciono", "true");
                }

                if (! cb.isChecked() ) {
                    Constant.Altura2.remove(position);
                    Constant.TV2.remove(position);
                    Constant.TR2.remove(position);
                    Constant.P2.remove(position);
                    Constant.Apoyo2.remove(position);
                    Log.d("selecciono", "false");
                }


            }
        });






        holder.Option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.delete, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.mBorrar:
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
                                ref.child(String.valueOf(Constant.ID)).child(Constant.TipoSalto).child(String.valueOf(medidas.getFecha())).removeValue();
                                arrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());

                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();


            }

        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView vuelo, altura, reaccion, fecha, potencia, apoyo;
        private CheckBox graficar;
        private ImageButton Option;

        public RecyclerViewHolder (View view){

            super (view);
            fecha = (TextView) view.findViewById(R.id.tvFecha);
            vuelo = (TextView)view.findViewById(R.id.tvVuelo);
            reaccion=(TextView)view.findViewById(R.id.tvReaccion);
            altura = (TextView) view.findViewById(R.id.tvAltura);
            apoyo = (TextView) view.findViewById(R.id.tvApoyo);
            potencia = (TextView) view.findViewById(R.id.tvPotencia);
            graficar = (CheckBox) view.findViewById(R.id.checkBoxGraph);
            Option= (ImageButton) view.findViewById(R.id.imageOption2);



        }

    }
}
