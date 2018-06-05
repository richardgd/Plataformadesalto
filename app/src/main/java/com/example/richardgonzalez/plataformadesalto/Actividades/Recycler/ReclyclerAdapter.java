package com.example.richardgonzalez.plataformadesalto.Actividades.Recycler;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richardgonzalez.plataformadesalto.Actividades.DisplayList;
import com.example.richardgonzalez.plataformadesalto.Actividades.TestActivity;
import com.example.richardgonzalez.plataformadesalto.POJO.Deportistas;
import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReclyclerAdapter extends RecyclerView.Adapter <ReclyclerAdapter.RecyclerViewHolder>{
    ArrayList<Deportistas> arrayList = new ArrayList<>();
    protected DisplayList context;
    int pass=0;


    public  ReclyclerAdapter(ArrayList<Deportistas> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = (DisplayList) context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
            return recyclerViewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

            final Deportistas deportistas = arrayList.get(position);
            Picasso.with(context).load(deportistas.getFoto()).placeholder(R.drawable.mujer2).into(holder.imgFoto);
            holder.nombre.setText(deportistas.getNombre());
            holder.id.setText("ID: " + String.valueOf(deportistas.getId()));
            holder.peso.setText("Peso: " + String.valueOf(deportistas.getPeso()));
            holder.imgFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TestActivity.class);
                    intent.putExtra("Nombre", deportistas.getNombre());
                    intent.putExtra("Peso",deportistas.getPeso());
                    Constant.peso = (float) deportistas.getPeso();
                    Constant.ID = deportistas.getId();
                    intent.putExtra("ID",deportistas.getId());
                    intent.putExtra("Conectividad",Constant.conectividad);
                    v.getContext().startActivity(intent);

                }
            });
            holder.Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    final PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {

                            switch (item.getItemId()){

                                case R.id.mEdit:
                                    final LinearLayout linearLayout = (LinearLayout) context.findViewById(R.id.layoutEdit);
                                    if (linearLayout != null) {
                                        linearLayout.setVisibility(View.VISIBLE);
                                    }
                                    final FloatingActionButton add = (FloatingActionButton)context.findViewById(R.id.fab);
                                    if (add != null) {
                                        add.setVisibility(View.GONE);
                                    }
                                    FloatingActionButton addUser = (FloatingActionButton)context.findViewById(R.id.fabAñadir);
                                    if (addUser != null) {
                                        addUser.setVisibility(View.GONE);
                                    }
                                    final FloatingActionButton Edit = (FloatingActionButton)context.findViewById(R.id.fabEditar);
                                    Edit.setVisibility(View.VISIBLE);

                                    Edit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            EditText editNombre = (EditText) context.findViewById(R.id.editNombre);
                                            editNombre.setVisibility(View.GONE);
                                            EditText editPeso = (EditText) context.findViewById(R.id.editPeso);

                                           int id = 0;
                                            String nombre = editNombre.getText().toString();
                                            float peso =0;
                                            try {
                                                 peso = Float.parseFloat(editPeso.getText().toString());

                                                pass=1;

                                            }
                                            catch(Exception e)
                                            {
                                                Toast.makeText(context, "Ingresar peso y ID en numeros", Toast.LENGTH_SHORT).show();}

                                            if (pass == 1) {
                                                if (linearLayout != null) {
                                                    linearLayout.setVisibility(View.GONE);
                                                }
                                                if (add != null) {
                                                    add.setVisibility(View.VISIBLE);
                                                }

                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference storageReference = storage.getReferenceFromUrl(deportistas.getFoto());
                                                storageReference.delete();

                                               // myFirebaseRef.child(String.valueOf(deportistas.getId())).child("nombre").setValue(nombre);
                                                ref.child(String.valueOf(deportistas.getId())).child("peso").setValue(String.valueOf(peso));
                                                deportistas.setNombre(nombre);
                                                pass = 0;
                                                deportistas.setId(id);
                                                deportistas.setPeso(peso);
                                                notifyItemChanged(holder.getAdapterPosition());
                                                Edit.setVisibility(View.GONE);


                                            }
                                        }
                                    });


                                    break;
                                case R.id.mBorrar:
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageReference = storage.getReferenceFromUrl(deportistas.getFoto());
                                    storageReference.delete();
                                        ref.child(String.valueOf(deportistas.getId())).removeValue();
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

        private TextView nombre, id, peso;
        private ImageButton Option;
        private ImageView imgFoto;

        public RecyclerViewHolder (View view){

                super (view);
                nombre = (TextView)view.findViewById(R.id.tvNombre);
                id=(TextView)view.findViewById(R.id.tvID);
                peso = (TextView) view.findViewById(R.id.tvPeso);
                imgFoto = (ImageView) view.findViewById(R.id.ImageFoto);
                Option= (ImageButton) view.findViewById(R.id.imageOption);



        }

    }


}
