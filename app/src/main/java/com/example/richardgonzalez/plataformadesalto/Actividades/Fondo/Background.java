package com.example.richardgonzalez.plataformadesalto.Actividades.Fondo;



/**
 * Created by Richard Gonzalez on 22/05/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.richardgonzalez.plataformadesalto.POJO.Deportistas;
import com.example.richardgonzalez.plataformadesalto.Actividades.Recycler.ReclyclerAdapter;
import com.example.richardgonzalez.plataformadesalto.R

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class Background extends AsyncTask<String,Deportistas,String> {

   private Context cxt;
    private Activity activity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Deportistas> arrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    // private FirebaseDatabase myFirebaseRef;


    private int vacio = 0;


    @Override
    protected void onPreExecute() {


        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(cxt);
        GridLayoutManager glm = new GridLayoutManager(cxt, 2);
        recyclerView.setLayoutManager(glm);
        recyclerView.setHasFixedSize(true);
        adapter = new ReclyclerAdapter(arrayList,cxt);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
       //progressDialog = new ProgressDialog(cxt);
        //progressDialog.setTitle("Espere un momento");
        //progressDialog.setMessage("Cargando deportistas");
        //progressDialog.setIndeterminate(true);
        //progressDialog.setCancelable(false);
        //progressDialog.show();



    }

    @Override
    protected String doInBackground(String... params) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");


        ref.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    String nombre = dataSnapshot1.child("nombre").getValue(String.class);
                    int id = dataSnapshot1.child("id").getValue(Integer.class);
                    double peso = dataSnapshot1.child("peso").getValue(Double.class);
                    String foto = dataSnapshot1.child("foto").getValue(String.class);
                    Deportistas deportista = new Deportistas(nombre,id,peso,foto);

                    publishProgress(deportista);
                    vacio = 1;
                }






            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        return null;

    }


    @Override
    public void onProgressUpdate(Deportistas... values) {
        arrayList.add(values[0]);

        adapter.notifyDataSetChanged();
       /* try {
            progressDialog.dismiss();
        } catch (Exception e){}
*/
    }



    public Background(Context cxt) {
        this.cxt=cxt;
        activity= (Activity)cxt;


    }



}