package com.example.richardgonzalez.plataformadesalto.Actividades.Fondo;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.richardgonzalez.plataformadesalto.Actividades.Recycler.RecyclerAdapterHistory;
import com.example.richardgonzalez.plataformadesalto.POJO.Medidas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BackgroundHistory extends AsyncTask<String,Medidas,String> {
    boolean complete = false;
    String vuelo, altura, reaccion, TipoAltura;
    int size = 0;
    Context cxt;
    Activity activity;
    int position;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Medidas> arrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
    int j = 0, vacio=0;
    private String TiempoApoyo;
    ArrayList<String> Fecha = new ArrayList<>();
    int i = 0;
    int y = 0;

    @Override
    protected void onPreExecute() {

        //recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView2);
        layoutManager = new LinearLayoutManager(cxt);
        //GridLayoutManager glm = new GridLayoutManager(cxt, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapterHistory(arrayList);
        recyclerView.setAdapter(adapter);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
        progressDialog = new ProgressDialog(cxt);
        progressDialog.setTitle("Espere un momento");
        progressDialog.setMessage("Cargando deportistas");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Constant.Altura2.clear();
        Constant.TV2.clear();
        Constant.TR2.clear();
        Constant.Apoyo2.clear();
        Constant.P2.clear();

    }

    @Override
    protected String doInBackground(String... params) {

        Log.d("Deportitas", String.valueOf(Constant.ID));
        Log.d("Tipo de salto", String.valueOf(Constant.TipoSalto));


        ref.child(String.valueOf(Constant.ID)).child(Constant.TipoSalto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                    String TV= dataSnapshot1.child("Tiempo de Vuelo").getValue(String.class);
                    Constant.TV2.add(TV);
                    String H = dataSnapshot1.child("Altura").getValue(String.class);
                    Constant.Altura2.add(H);
                    String TR = dataSnapshot1.child("Tiempo de Reaccion").getValue(String.class);
                    Constant.TR2.add(TR);
                    String Date = dataSnapshot1.child("Fecha").getValue(String.class);

                    String Potencia = dataSnapshot1.child("Potencia").getValue(String.class);
                    Constant.P2.add(Potencia);

                    try {

                         TiempoApoyo = dataSnapshot1.child("TiempoApoyo").getValue(String.class);
                        Constant.Apoyo2.add(TiempoApoyo);
                    }
                    catch (Exception e)
                    {
                        Constant.Apoyo2.add("");
                         TiempoApoyo = "";
                    }

                    if (Constant.MES ==  Integer.parseInt(Date.substring(3,5))){
                        vacio = 1;
                        Medidas medidas = new Medidas(TV,H,Date,TR,TiempoApoyo,Potencia);
                        publishProgress(medidas);

                    }

                    if (Constant.MES ==  13){
                        vacio = 1;
                        Medidas medidas = new Medidas(TV,H,Date,TR,TiempoApoyo,Potencia);
                        publishProgress(medidas);

                    }


                }

                if (vacio==0){
                    progressDialog.dismiss();
                }




            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });



        return null;

    }



    @Override
    public void onProgressUpdate(Medidas... values) {
        arrayList.add(values[0]);
        adapter.notifyDataSetChanged();
        try {
            progressDialog.dismiss();
        } catch (Exception e){}
    }

    public BackgroundHistory(Context cxt) {
        this.cxt=cxt;
        activity= (Activity)cxt;

    }



}
