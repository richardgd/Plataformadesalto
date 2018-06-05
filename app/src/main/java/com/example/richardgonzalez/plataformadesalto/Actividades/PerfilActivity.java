package com.example.richardgonzalez.plataformadesalto.Actividades;


/**
 * Created by Richard Gonzalez on 22/05/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.richardgonzalez.plataformadesalto.R;
import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.BackgroundHistory;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class PerfilActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private int position;
    private String datoNombre;
    private TextView Perfil;
    private FloatingActionButton graph;
    private String TipoSalto;
    private double H[];
    private int j=0;

    private String[] list = {"Filtrar por Mes (Todos)","Enero", "Febrero","Marzo", "Abril", "Mayo","Junio",
                             "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    private Spinner spinner;
    BackgroundHistory background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
        Perfil = (TextView) findViewById(R.id.tittlePerfil);
        spinner = (Spinner) findViewById(R.id.spinner2);
        graph = (FloatingActionButton) findViewById(R.id.fabGraph);
        createSpinner();
        //Limpiando rastros
        Constant.Altura.clear();
        Constant.TR.clear();
        Constant.P.clear();
        Constant.TV.clear();


        //Constant.Altura = new ArrayList<String>();
        //Constant.TV = new ArrayList<String>();
        //Constant.TR = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {//ver si contiene datos
            position =  extras.getInt("ID");//Obtengo el nombre
            datoNombre = extras.getString("Nombre");//Obtengo el nombre
            TipoSalto = extras.getString("Tipo");
            String variable = Constant.TipoSalto + " de " + datoNombre;
            Perfil.setText(variable);

        }

        //Constant.TipoSalto = TipoSalto;
        //Constant.MES = 13;
        background = new BackgroundHistory(PerfilActivity.this);
        background.execute();


        //background = new BackgroundHistory(PerfilActivity.this);
        //background.execute();

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this,GraphActivity.class);
                intent.putExtra("Nombre",datoNombre);
                intent.putExtra("ID",position);
                intent.putExtra("Tipo",TipoSalto);
                String[] stringArray = Constant.Altura.toArray(new String[Constant.Altura.size()]);
                String[] stringsArray2 = Constant.TR.toArray(new String[Constant.TR.size()]);
                String[] stringsArray3 = Constant.TV.toArray(new String[Constant.TV.size()]);
                intent.putExtra("VectorH",stringArray);
                intent.putExtra("VectorTR",stringsArray2);
                intent.putExtra("VectorTV",stringsArray3);
                startActivity(intent);
            }
        });


    }


    private void createSpinner() {
        ArrayAdapter<String> adaptador= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 1:
                        Constant.MES = 1;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 2:
                        Constant.MES = 2;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 3:
                        Constant.MES = 3;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();

                        break;
                    case 4:
                        Constant.MES = 4;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 5:
                        Constant.MES = 5;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 6:
                        Constant.MES = 6;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 7:
                        Constant.MES = 7;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 8:
                        Constant.MES = 8;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 9:
                        Constant.MES = 9;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 10:
                        Constant.MES = 10;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 11:
                        Constant.MES = 11;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    case 12:
                        Constant.MES = 12;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;
                    default:
                        Constant.MES = 13;
                        background = new BackgroundHistory(PerfilActivity.this);
                        background.execute();
                        break;



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




}
