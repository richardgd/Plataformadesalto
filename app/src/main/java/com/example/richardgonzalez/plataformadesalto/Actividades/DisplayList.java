package com.example.richardgonzalez.plataformadesalto.Actividades;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Background;
import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.POJO.Deportistas;
import com.example.richardgonzalez.plataformadesalto.Bluetooth.BluetoothService;
import com.example.richardgonzalez.plataformadesalto.Bluetooth.DeviceListActivity;
import com.example.richardgonzalez.plataformadesalto.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class DisplayList extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 2;
    private static final int RC_TAKE_PICTURE = 101;
    private String nombre = "";
    private float peso;
    private int id, pass;
    private  FloatingActionButton add, addmember, edit;
    private LinearLayout linearLayout;
    private EditText editNombre, editPeso;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
    private String[] list = {"Ingresa el sexo","hombre", "mujer"};
    private boolean fotoupload;
    int sexo;
    boolean conectividad = false;
    private ImageView bluetoohON, Option;
    private BluetoothAdapter mBluetoothAdapter;
    public static final int  REQUEST_CONNECT_DEVICE_SECURE = 1,
            REQUEST_CONNECT_DEVICE_INSECURE = 2, REQUEST_ENABLE_BT = 3;
    public static BluetoothService BlueService;
    private String TAG = "DisplayActivity";
    private Button buttonLoadImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        storage = FirebaseStorage.getInstance();
        bluetoohON = (ImageView) findViewById(R.id.tvBluetooth);
        Option = (ImageView) findViewById(R.id.tvOption);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("https://plataforma-de-salto.firebaseio.com");
        add = (FloatingActionButton) findViewById(R.id.fab);
        addmember = (FloatingActionButton) findViewById(R.id.fabAñadir);
        linearLayout = (LinearLayout) findViewById(R.id.layoutEdit);
        edit = (FloatingActionButton) findViewById(R.id.fabEditar);
        buttonLoadImage = (Button) findViewById(R.id.buttonFoto);

        editNombre = (EditText) findViewById(R.id.editNombre);
        editPeso = (EditText) findViewById(R.id.editPeso);
        buttonLoadImage.setOnClickListener(this);

        final Background background = new Background(DisplayList.this);
        Constant.conectividad = isOnline();



        final int CODE = 5; // app defined constant used for onRequestPermissionsResult

        String[] permissionsToRequest =
                {
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

        boolean allPermissionsGranted = true;

        for(String permission : permissionsToRequest)
        {
            allPermissionsGranted = allPermissionsGranted && (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
        }

        if(!allPermissionsGranted)
        {
            ActivityCompat.requestPermissions(this, permissionsToRequest, CODE);
        }

        mBluetoothAdapter.startDiscovery();



        storageReference = storage.getReferenceFromUrl("gs://deportesuac-a1819.appspot.com/");



        if (Constant.conectividad) {
            background.execute();
            bluetoohON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
            });

            Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_bluetooth, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.mConexion : {
                                    // Launch the DeviceListActivity to see devices and do scan
                                    Intent serverIntent = new Intent(DisplayList.this, DeviceListActivity.class);
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


        else {
            Toast.makeText(getApplicationContext(),"Modo Offline", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, TestActivity.class);
            intent.putExtra("Conectividad",Constant.conectividad);
            startActivity(intent);


        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);





            }
        });

        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    nombre = editNombre.getText().toString();
                    try {
                        peso = Float.parseFloat(editPeso.getText().toString());
                        Random r = new Random();
                        id = r.nextInt(1000 - 1) + 1;
                        pass = 1;

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ingresar peso y ID en numeros", Toast.LENGTH_SHORT).show();
                    }

                    if (pass == 1) {
                        linearLayout.setVisibility(View.GONE);
                        Deportistas deportista = new Deportistas(nombre, id, peso, Constant.Foto);
                        add.setVisibility(View.VISIBLE);
                        ref.child(String.valueOf(id)).setValue(deportista);
                        pass = 0;
                        background.onProgressUpdate(new Deportistas(nombre, Integer.parseInt(String.valueOf(id)), peso, Constant.Foto));
                    }


            }
    });

}


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    Constant.Adress = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    //startService(new Intent(this, BluetoothService.class));
                    //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                    BlueService = new BluetoothService();
                    BlueService.onCreate();


                }
                break;

            case RC_TAKE_PICTURE:
                Log.d("Entro","TAKE Picture");
                if (resultCode == Activity.RESULT_OK && null != data) {


                        Toast.makeText(getApplicationContext(), "Espere mientras la foto sube...", Toast.LENGTH_SHORT).show();
                        mFileUri = data.getData();

                        if (mFileUri != null) {
                            uploadFromUri(mFileUri);
                            Log.d("Entro","Upload Picture");
                        } else {
                            Log.w(TAG, "File URI is null");
                        }







                }
                break;

        }
    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;
        StorageReference photoRef = storageReference.child(editNombre.getText().toString());
        Log.d("Entro","Upload Picture");

        photoRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri:onSuccess");

                        // Get the public download URL

                        Constant.Foto=taskSnapshot.getMetadata().getDownloadUrl().toString();
                        Toast.makeText(getApplicationContext(),"Foto subida", Toast.LENGTH_SHORT).show();

                        // [START_EXCLUDE]

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                    }
                });
    }





    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonFoto:
                
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RC_TAKE_PICTURE);
                
                break;
        }
    }
}




