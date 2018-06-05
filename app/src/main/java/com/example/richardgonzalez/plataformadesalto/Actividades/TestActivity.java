package com.example.richardgonzalez.plataformadesalto.Actividades;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.Bluetooth.DeviceListActivity;
import com.example.richardgonzalez.plataformadesalto.Bluetooth.BluetoothService;
import com.example.richardgonzalez.plataformadesalto.POJO.BaseDatos;
import com.example.richardgonzalez.plataformadesalto.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * Created by Richard Gonzalez on 22/05/2018.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {


    //Contastes
    private static final String TAG = "BluetoohActivity";
    public static final int  REQUEST_CONNECT_DEVICE_SECURE = 1,
             REQUEST_ENABLE_BT = 3;
    public static Context context;

    //View

    private ImageView bluetoohON, Option;
    public static TextView txtString, text1, text2, textTitle, SaltoRepetivo, textPotencia;
    private Button savedate, salto, buttonLoadImage;
    private Spinner spinner;

    //Variables
    private String[] list = {"Ingresa el salto", "Salto Simple", "Squat Jump", "Countermovement Jump",
            "Squat Jump con carga", "Abalakov", "Drop Jump", "Salto Repetivo por Tiempo", "Salto Repetivo por Repetición"};
    private String datoPeso, datoNombre, fecha;

    public static Float altura;
    private EditText editRepetido;
    public static List<String> TiempoV, TiempoR;
    int position = 0, j = 0;
    //Float altura;
    DateFormat df;
    private boolean conectividad;


    //Bluetooth
    private BluetoothAdapter mBluetoothAdapter = null;
    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
    FirebaseStorage storage;
    StorageReference storageReference;
    //private BluetoothService DisplayList.BlueService;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BluetoothService BlueService2;
    private BaseDatos baseDatos;
    private Button photoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        storage = FirebaseStorage.getInstance();
        //Views

        context = getApplicationContext();
        // bluetoohON = (ImageView) findViewById(R.id.tvBluetooth);
        txtString = (TextView) findViewById(R.id.txtString);
        textPotencia = (TextView) findViewById(R.id.textViewPotencia);
        Option = (ImageView) findViewById(R.id.tvOption);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        textTitle = (TextView) findViewById(R.id.tittleNombre);
        SaltoRepetivo = (TextView) findViewById(R.id.textRepetivo);
        editRepetido = (EditText) findViewById(R.id.editRepetivo);

        salto = (Button) findViewById(R.id.buttonSalto);


        savedate = (Button) findViewById(R.id.buttonSave);
        spinner = (Spinner) findViewById(R.id.spinnerSalto);
        createSpinner();



        //TiempoR = new ArrayList<>();
        //TiempoV = new ArrayList<>();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {//ver si contiene datos
            datoNombre = extras.getString("Nombre");//Obtengo el nombre
            datoPeso = String.valueOf(extras.getString("Peso"));//Obtengo la edad
            position = extras.getInt("ID");
            textTitle.setText(datoNombre);
            conectividad = extras.getBoolean("Conectividad");
            textTitle.setText(datoNombre);


        }


        savedate.setOnClickListener(this);
        salto.setOnClickListener(this);


        if (conectividad) {

            Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_history, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {


                                case R.id.mHistory: {
                                    Intent intent = new Intent(TestActivity.this, PerfilActivity.class);
                                    intent.putExtra("ID", position);
                                    intent.putExtra("Tipo", Constant.TipoSalto);
                                    intent.putExtra("Nombre", datoNombre);
                                    startActivity(intent);
                                    return true;
                                }
                            }

                            return true;
                        }
                    });
                    popupMenu.show();


                }

            });

        }

        else{
            baseDatos = new BaseDatos(getApplicationContext());


            Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_bluetooth, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {



                                case R.id.mConexion: {

                                    Intent serverIntent = new Intent(TestActivity.this, DeviceListActivity.class);
                                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                                    return true;

                                }
                            }

                            return true;
                        }
                    });
                    popupMenu.show();


                }

            });




        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    Constant.Adress = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    //startService(new Intent(this, BluetoothService.class));
                    //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                    BlueService2 = new BluetoothService();
                    BlueService2.onCreate();


                }

                break;


        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(conectividad) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session
            } else if (DisplayList.BlueService == null) {
                //startService(new Intent(this, BluetoothService.class));
                DisplayList.BlueService = new BluetoothService();
                DisplayList.BlueService.onCreate();


            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:

                if (((Constant.TipoSalto == "Salto Simple" || Constant.TipoSalto == "Squad Jump" || Constant.TipoSalto == "Countermovement Jump" || Constant.TipoSalto ==
                        "Squat Jump con carga" || Constant.TipoSalto == "Abalakov")) && (Constant.TiempoVuelo != null && Constant.TiempoReaccion != null && Constant.AlturaMedida != null && conectividad)) {
                    df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
                    fecha = df.format(Calendar.getInstance().getTime());

                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Tiempo de Vuelo").setValue(Constant.TiempoVuelo);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Tiempo de Reaccion").setValue(Constant.TiempoReaccion);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Altura").setValue(Constant.AlturaMedida);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Fecha").setValue(fecha);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Potencia").setValue(Constant.Potencia);
                    Constant.TiempoVuelo = null;
                    Constant.AlturaMedida = null;
                    Constant.TiempoReaccion = null;
                    Constant.Potencia = null;
                    Toast.makeText(getApplicationContext(), "Guardado con exito", Toast.LENGTH_LONG).show();
                }



                if (((Constant.TipoSalto == "Drop Jump")) && (Constant.TiempoVuelo != null && Constant.TiempoReaccion != null && Constant.AlturaMedida != null && conectividad)) {
                    df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
                    fecha = df.format(Calendar.getInstance().getTime());

                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Tiempo de Vuelo").setValue(Constant.TiempoVuelo);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Tiempo de Reaccion").setValue(Constant.TiempoReaccion);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Altura").setValue(Constant.AlturaMedida);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Fecha").setValue(fecha);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Potencia").setValue(Constant.Potencia);
                    ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("TiempoApoyo").setValue(Constant.TiempoApoyo);

                    Constant.TiempoVuelo = null;
                    Constant.AlturaMedida = null;
                    Constant.TiempoReaccion = null;
                    Constant.Potencia = null;
                    Constant.TiempoApoyo = null;
                    Toast.makeText(getApplicationContext(), "Guardado con exito", Toast.LENGTH_LONG).show();
                }

                /*

                if (((Constant.TipoSalto == "Salto Simple" || Constant.TipoSalto == "Squad Jump" || Constant.TipoSalto == "Countermovement Jump" || Constant.TipoSalto ==
                        "Squat Jump con carga" || Constant.TipoSalto == "Abalakov")) && Constant.TiempoVuelo != null && Constant.TiempoReaccion != null && Constant.AlturaMedida != null && !conectividad) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_ID, Constant.ID);
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TV, Constant.TiempoVuelo);
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TR, Constant.TiempoReaccion);
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_H, Constant.AlturaMedida);
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_P, position);
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TIPO_SALTO, Constant.TipoSalto);
                    contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TA, Constant.TiempoApoyo);
                    baseDatos.InsertarMiembros(contentValues);
                }
                */
                if ((Constant.TipoSalto == "Salto Repetivo" || Constant.TipoSalto == "Salto Repetivo2") && (Constant.TV != null && Constant.TR != null && Constant.Altura != null && conectividad)) {
                    int i = 0;


                    Log.d(TAG, String.valueOf(Constant.Altura.size()));
                    Random r = new Random();
                    int a = r.nextInt(10000 - 1);

                    while (i < Constant.Altura.size()) {
                        df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");

                        fecha = df.format(Calendar.getInstance().getTime()) + " " + String.valueOf(i) + "Prueba: " + String.valueOf(a);
                        ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Tiempo de Vuelo").setValue(Constant.TV.get(i));
                        ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Tiempo de Reaccion").setValue(Constant.TR.get(i));
                        ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Altura").setValue(Constant.Altura.get(i));
                        ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Fecha").setValue(fecha);
                        ref.child(String.valueOf(position)).child(Constant.TipoSalto).child(String.valueOf(fecha)).child("Potencia").setValue(Constant.P.get(i));
                        i++;
                    }
                    Toast.makeText(getApplicationContext(), "Guardado con Exito", Toast.LENGTH_SHORT).show();
                    Constant.P.clear();
                    Constant.Altura.clear();
                    Constant.TR.clear();
                    Constant.TV.clear();


                }

                if (Constant.TipoSalto == "Salto Repetivo" && Constant.TiempoVuelo != null && Constant.TiempoReaccion != null && Constant.AlturaMedida != null && !conectividad) {
                   /*
                    int i = 0;
                    Log.d("TV", String.valueOf(TiempoV.size()));
                    BaseDatos baseDatos = new BaseDatos(this);
                    ContentValues contentValues = new ContentValues();

                    while (i < TiempoV.size()) {
                        Log.d("TV", TiempoV.get(i));
                        Log.d("TR", TiempoR.get(i));
                        i++;
                        // Log.d("H", AlturaH.get(i));
                    }
                    try {
                        i = 0;
                        while (i < TiempoV.size()) {

                            altura = (float) (((altura * altura) * 9.84) / 8);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_ID, Constant.ID);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TV, Constant.TiempoVuelo);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TR, Constant.TiempoReaccion);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_H, Constant.AlturaMedida);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_P, position);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TIPO_SALTO, Constant.TipoSalto);
                            contentValues.put(ConstanteBaseDatos.TABLE_MEDIDA_TA, Constant.TiempoApoyo);
                            baseDatos.InsertarMiembros(contentValues);
                            i++;
                        }
                        Toast.makeText(getApplicationContext(), "Guardado con Exito", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.d("SaveError", e.toString());
                    }
                }

                */
                }

                break;
            case R.id.buttonSalto: {
                if (conectividad) {
                    if (Constant.TipoSalto == "Salto Repetivo") {
                        try {
                            DisplayList.BlueService.write('2' + editRepetido.getText().toString());
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                        }

                    } else if (Constant.TipoSalto == "Salto Repetivo2") {
                        try {
                            DisplayList.BlueService.write('3' + editRepetido.getText().toString());
                        }  catch (Exception e){
                        Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                    }
                    } else if (Constant.TipoSalto == "Drop Jump") {
                        try{
                        DisplayList.BlueService.write('4' + editRepetido.getText().toString());
                        }
                     catch (Exception e){
                        Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                    }
                    } else {
                        try {
                            DisplayList.BlueService.write('1' + editRepetido.getText().toString());
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                if (!conectividad) {

                    if (Constant.TipoSalto == "Salto Repetivo") {
                        try {
                        BlueService2.write('2' + editRepetido.getText().toString());
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                        }

                    } else if (Constant.TipoSalto == "Salto Repetivo2") {
                        try{
                        BlueService2.write('3' + editRepetido.getText().toString());
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                        }
                    } else if (Constant.TipoSalto == "Drop Jump") {
                        try{
                        BlueService2.write('4' + editRepetido.getText().toString());
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try{
                        BlueService2.write('1' + editRepetido.getText().toString());
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No se ha conectado a la Alfombra", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


            }
            break;

        }

    }




    private void createSpinner() {
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 1:
                        Constant.TipoSalto = "Salto Simple";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setVisibility(View.GONE);
                        editRepetido.setVisibility(View.GONE);
                        break;
                    case 2:
                        Constant.TipoSalto = "Squad Jump";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setVisibility(View.GONE);
                        editRepetido.setVisibility(View.GONE);
                        break;
                    case 3:
                        Constant.TipoSalto = "Countermovement Jump";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setVisibility(View.GONE);
                        editRepetido.setVisibility(View.GONE);
                        break;
                    case 4:
                        Constant.TipoSalto = "Squad Jump con carga";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setVisibility(View.GONE);
                        editRepetido.setVisibility(View.GONE);
                        break;
                    case 5:
                        Constant.TipoSalto = "Abalakov";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setVisibility(View.GONE);
                        editRepetido.setVisibility(View.GONE);
                        break;
                    case 6:
                        Constant.TipoSalto = "Drop Jump";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setText("Tiempo de Apoyo:");
                        SaltoRepetivo.setVisibility(View.VISIBLE);
                        editRepetido.setVisibility(View.GONE);

                        break;
                    case 7:
                        Constant.TipoSalto = "Salto Repetivo";
                        text1.setVisibility(View.GONE);
                        text2.setVisibility(View.GONE);
                        txtString.setVisibility(View.GONE);
                        textPotencia.setVisibility(View.GONE);
                        SaltoRepetivo.setVisibility(View.VISIBLE);
                        editRepetido.setVisibility(View.VISIBLE);

                        break;
                    case 8:
                        Constant.TipoSalto = "Salto Repetivo2";
                        text1.setVisibility(View.GONE);
                        text2.setVisibility(View.GONE);
                        txtString.setVisibility(View.GONE);
                        textPotencia.setVisibility(View.GONE);
                        SaltoRepetivo.setVisibility(View.VISIBLE);
                        editRepetido.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Constant.TipoSalto = "Salto Simple";
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        txtString.setVisibility(View.VISIBLE);
                        textPotencia.setVisibility(View.VISIBLE);
                        SaltoRepetivo.setVisibility(View.GONE);
                        editRepetido.setVisibility(View.GONE);
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

    }
}



