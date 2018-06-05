package com.example.richardgonzalez.plataformadesalto.Bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.Actividades.TestActivity;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */
public class BluetoothService extends Service {
    private StringBuilder recDataString = new StringBuilder();
    private static final int SALTO_SIMPLE =  1;
    private BluetoothSocket btSocket;
    private BluetoothAdapter mBluetoothAdapter;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean isBtConnected = false;


    private boolean ConnectSuccess = true;



    private String TAG = "ServiceClas";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
        if (btSocket == null || !isBtConnected)
        {
            mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice dispositivo = mBluetoothAdapter.getRemoteDevice(Constant.Adress);//conectamos al dispositivo y chequeamos si esta disponible
            try {
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            try {
                btSocket.connect();
                Read read = new Read();
                read.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");
        if (!ConnectSuccess) {
            Toast.makeText(TestActivity.context, "Conexion FALLIDA", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(TestActivity.context,"Conectado", Toast.LENGTH_SHORT).show();
            isBtConnected = true;


        }


        return START_NOT_STICKY;
    }


    public void write(String input) {

        try {

                btSocket.getOutputStream().write(input.getBytes(Charset.forName("UTF-8")));//write bytes over BT connection via outstream
                 Toast.makeText(TestActivity.context,"Preparese para el salto", Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            //if you cannot write, close the application
            Toast.makeText(TestActivity.context,"Error, apague y prenda el Bluetooth", Toast.LENGTH_SHORT).show();

        }
    }



    public class Read extends Thread {
        @Override
        public void run() {
            super.run();
            byte[] buffer = new byte[256];
            int bytes;
            while (true) {
                try {
                    // Read from the InputStream
                    //Read is synchronous call which keeps on waiting until data is available
                    bytes = btSocket.getInputStream().read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    mHandler.obtainMessage(SALTO_SIMPLE, bytes, -1, readMessage).sendToTarget();


                } catch (IOException e) {
                    break;
                }
            }
        }
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {


            // Atendemos al tipo de mensaje
            switch (msg.what) {
                // Mensaje de lectura: se mostrara en un TextView
                case SALTO_SIMPLE:


                    if (Constant.TipoSalto == "Salto Simple" || Constant.TipoSalto == "Squad Jump" || Constant.TipoSalto == "Countermovement Jump" || Constant.TipoSalto ==
                            "Squat Jump con carga" || Constant.TipoSalto == "Abalakov") {
                        String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage); //keep appending to string until ~
                        Log.d("x", readMessage);
                        int endOfLineIndex = recDataString.indexOf("~"); // determine the end-of-line
                        if (endOfLineIndex > 0) { // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex); // extract string
                           // TestActivity.txtString.setText("Data Received = " + dataInPrint);
                            int dataLength = dataInPrint.length(); //get length of data received
                            Log.d("Lenght", String.valueOf(dataLength));
                            if (recDataString.charAt(0) == '#') //if it starts with # we know it is what we are looking for
                            {
                                //TV = recDataString.substring(1, 6); //get sensor value from string between indices 1-5
                                //TR = recDataString.substring(6, 11); //same again...
                                try {

                                    Constant.TiempoVuelo = recDataString.substring(1, 6); //get sensor value from string between indices 1-5
                                    Constant.TiempoReaccion = recDataString.substring(6, 11); //same again..
                                    Constant.AlturaMedida = String.valueOf(((Float.valueOf(recDataString.substring(1, 6))) * (Float.valueOf(recDataString.substring(1, 6))) * 9.8) / 8);
                                    Constant.AlturaMedida=Constant.AlturaMedida.substring(0,5);
                                    try {
                                        if (Constant.TipoSalto != "Countermovement Jump") {
                                            Constant.Potencia = String.valueOf(String.valueOf(((60.7 * (Float.valueOf(Constant.AlturaMedida) * 100)) + ((45.3) * Constant.peso)) - 2055));
                                        } else {
                                            Constant.Potencia = String.valueOf(String.valueOf((((51.9 * (Float.valueOf(Constant.AlturaMedida) * 100)) + ((48.9) * Constant.peso)) - 2007)));
                                        }
                                    } catch (Exception e){
                                        Log.d("error", e.toString());
                                        Toast.makeText(TestActivity.context,"Lo siento, algo salio mal...", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (Exception e) {

                                    Log.d("error", e.toString());
                                    Toast.makeText(TestActivity.context,"Lo siento, algo salio mal...", Toast.LENGTH_SHORT).show();

                                }

                                String cambio1 = "El tiempo de Vuelo fue :" + recDataString.substring(1, 6) + "ms";
                                String cambio2 = "El tiempo de Reaccion fue :" + recDataString.substring(6, 11) + "ms";
                                String cambio3 = "La altura fue: " + Constant.AlturaMedida.substring(0,5) + "m";
                                String cambio4 = "La potencia fue: " + Constant.Potencia + "Wat/kg";
                                TestActivity.text1.setText(cambio1);
                                TestActivity.text2.setText(cambio2);
                                TestActivity.txtString.setText(cambio3);
                                TestActivity.textPotencia.setText(cambio4);


                            }
                            recDataString.delete(0, recDataString.length()); //clear all string data

                        }
                    }

                    if (Constant.TipoSalto == "Drop Jump") {
                        String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage); //keep appending to string until ~
                        Log.d("x", readMessage);
                        int endOfLineIndex = recDataString.indexOf("~"); // determine the end-of-line
                        if (endOfLineIndex > 0) { // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex); // extract string
                            int dataLength = dataInPrint.length(); //get length of data received
                            Log.d("Lenght", String.valueOf(dataLength));
                            if (recDataString.charAt(0) == '#') //if it starts with # we know it is what we are looking for
                            {
                                //TV = recDataString.substring(1, 6); //get sensor value from string between indices 1-5
                                //TR = recDataString.substring(6, 11); //same again...
                                try {
                                    Log.d("Lenght", String.valueOf(recDataString.substring(1, 16)));
                                    Constant.TiempoVuelo = recDataString.substring(1, 6); //get sensor value from string between indices 1-5
                                    Constant.TiempoReaccion = recDataString.substring(6, 11); //same again..
                                    Constant.TiempoApoyo = recDataString.substring(11, 16);
                                    Constant.AlturaMedida = String.valueOf(((Float.valueOf(recDataString.substring(1, 6))) * (Float.valueOf(recDataString.substring(1, 6))) * 9.8) / 8);
                                    Constant.AlturaMedida=Constant.AlturaMedida.substring(0,5);
                                    Constant.Potencia = String.valueOf(String.valueOf(((60.7 * (Float.valueOf(Constant.AlturaMedida) * 100) ) + ((45.3)* Constant.peso))-2055));
                                    Constant.Potencia=Constant.Potencia.substring(0,5);

                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                    Toast.makeText(TestActivity.context,"Lo siento, algo salio mal...", Toast.LENGTH_SHORT).show();

                                }

                                String cambio1 = "El tiempo de Vuelo fue :" + recDataString.substring(1, 6) + "ms";
                                String cambio2 = "El tiempo de Reaccion fue :" + recDataString.substring(6, 11) + "ms";
                                String cambio3= "";
                                try {
                                     cambio3 = "El tiempo de Apoyo fue " + recDataString.substring(11, 16) + "ms";
                                }

                                catch (Exception e){
                                    Log.d("exception",e.toString());
                                }
                                String cambio5 = "La altura fue: " + Constant.AlturaMedida + "m";
                                String cambio4 = "La potencia fue: " + Constant.Potencia + "Wat/kg";
                                TestActivity.text1.setText(cambio1);
                                TestActivity.text2.setText(cambio2);
                                TestActivity.txtString.setText(cambio4);
                                TestActivity.textPotencia.setText(cambio3);
                                TestActivity.SaltoRepetivo.setText(cambio5);


                            }

                            recDataString.delete(0, recDataString.length()); //clear all string data

                        }
                    }


                    if (Constant.TipoSalto == "Salto Repetivo" || Constant.TipoSalto == "Salto Repetivo2") {
                        String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage); //keep appending to string until ~
                        Log.d("x", readMessage);
                        int endOfLineIndex = recDataString.indexOf("~"); // determine the end-of-line
                        if (endOfLineIndex > 0) { // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex); // extract string

                            int dataLength = dataInPrint.length(); //get length of data received
                            Log.d("Lenght", String.valueOf(dataLength));
                            if (dataLength > 8) {
                                if (recDataString.charAt(0) == '#') //if it starts with # we know it is what we are looking for
                                {
                                    try {
                                        Constant.TV.add(recDataString.substring(1, 6)); //get sensor value from string between indices 1-5
                                        Constant.TR.add(recDataString.substring(6, 11)); //same again..
                                        float h = (float) (((Float.valueOf(recDataString.substring(1, 6))) * (Float.valueOf(recDataString.substring(1, 6))) * 9.8) / 8);
                                        Constant.Altura.add(String.valueOf(h));
                                        Constant.P.add(String.valueOf(((60.7 * (h * 100) ) + ((45.3)* Constant.peso))-2055));

                                    } catch (Exception e) {

                                        Log.d("error", e.toString());
                                        Toast.makeText(TestActivity.context,"Lo siento, algo salio mal...", Toast.LENGTH_SHORT).show();

                                    }

                                    String cambio1 = "El tiempo de Vuelo fue :" + recDataString.substring(1, 6) + "ms ";
                                    String cambio2 = "el tiempo de Reaccion fue :" + recDataString.substring(6, 11) + "ms ";
                                    String cambio3 = "la altura fue: " + String.valueOf(((Float.valueOf(recDataString.substring(1, 6))) * (Float.valueOf(recDataString.substring(1, 6))) * 9.8) / 8) + "m ";
                                    String cambio4 = "la potencia fue: " + String.valueOf(((Float.valueOf(recDataString.substring(1, 6)) * Float.valueOf(recDataString.substring(1, 6))) * ((9.8) * (9.8))) / (4 * Float.valueOf(recDataString.substring(1, 6)))) + "Wat/kg";
                                    String cambio5 = cambio1 + '\n' + '\r' + cambio2 + '\n' + '\r' + cambio3 + '\n' + '\r' + cambio4 + '\n' + '\r';
                                    TestActivity.SaltoRepetivo.setText(cambio5);


                                }
                            }
                            if (dataLength < 8 && dataLength>2) //if it starts with # we know it is what we are looking for
                            {
                                Toast.makeText(TestActivity.context, "Termino el salto", Toast.LENGTH_SHORT).show();

                            }

                            recDataString.delete(0, recDataString.length()); //clear all string data
                        }


                    }

                    break;

                default:
                    break;
            }
        }

    };








    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruido...");
    }
}
