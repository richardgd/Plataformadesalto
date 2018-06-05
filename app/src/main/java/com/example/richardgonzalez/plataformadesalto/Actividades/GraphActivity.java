package com.example.richardgonzalez.plataformadesalto.Actividades;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.richardgonzalez.plataformadesalto.Actividades.Fondo.Constant;
import com.example.richardgonzalez.plataformadesalto.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private TextView Perfil;
    private String datoNombre;
    private String[] H, TiempoV, TiempoR;

    int i, position;
    //
    CheckBox checkTV, checkTR, checkH;
    LineDataSet dataSet, dataSet2, dataSet3;
    LineData lineData;
    LineChart chart;
    private LineDataSet dataSet4;
    private LineDataSet dataSet5;
    private CheckBox checkAP, checkP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Perfil = (TextView)   findViewById(R.id.tittlePerfil);
        checkTR = (CheckBox) findViewById(R.id.checkReaccion);
        checkH = (CheckBox) findViewById(R.id.checkAltura);
        checkTV = (CheckBox) findViewById(R.id.checkVuelo);
        checkP = (CheckBox) findViewById(R.id.checkBoxPotencia);
        checkAP = (CheckBox) findViewById(R.id.checkBoxApoyo);


        // in this example, a LineChart is initialized from xml
        chart = (LineChart) findViewById(R.id.chart);

        if (Constant.TipoSalto != "Dromp Jump"){
            checkAP.setVisibility(View.INVISIBLE);
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {//ver si contiene datos
            position =  extras.getInt("ID");//Obtengo el nombre
            datoNombre = extras.getString("Nombre");//Obtengo el nombre
            H=extras.getStringArray("VectorH");
            TiempoR=extras.getStringArray("VectorTR");
            TiempoV=extras.getStringArray("VectorTV");
            String variable = Constant.TipoSalto + "de" + datoNombre;
            Perfil.setText(variable);


        }





        List<Entry> TV = new ArrayList<>();
        List<Entry> TR = new ArrayList<>();
        List<Entry> Hh = new ArrayList<>();
        List<Entry> P = new ArrayList<>();
        List<Entry> Ap = new ArrayList<>();
        if(Constant.TipoSalto=="Dromp Jump"){

            for (i = 0; i <Constant.Altura2.size(); i++){
                TV.add(new Entry(i, Float.valueOf(Constant.TV2.get(i))));
                TR.add(new Entry(i, Float.valueOf(Constant.TR2.get(i))));
                Hh.add(new Entry(i, Float.valueOf(Constant.Altura2.get(i))));
                P.add(new Entry(i, Float.valueOf(Constant.P2.get(i))));
                Ap.add(new Entry(i, Float.valueOf(Constant.Apoyo2.get(i))));


            }

        }

        else {

            for (i = 0; i < Constant.Altura2.size(); i++) {

                TV.add(new Entry(i, Float.valueOf(Constant.TV2.get(i))));
                TR.add(new Entry(i, Float.valueOf(Constant.TR2.get(i))));
                Hh.add(new Entry(i, Float.valueOf(Constant.Altura2.get(i))));
                P.add(new Entry(i, Float.valueOf(Constant.P2.get(i))));


            }
        }

        if (Constant.TipoSalto=="Drop Jump"){

            dataSet = new LineDataSet(TV, "TV"); // add entries to dataset
            dataSet.setDrawCircles(true);
            dataSet.setColor(Color.RED);
            dataSet.setValueTextColor(Color.RED); // styling, ...
            dataSet.setDrawValues(true);

            dataSet2 = new LineDataSet(TR, "TR"); // add entries to dataset
            dataSet2.setDrawCircles(true);
            dataSet2.setColor(Color.BLUE);
            dataSet2.setValueTextColor(Color.BLUE); // styling, ...
            dataSet2.setDrawValues(true);

            dataSet3 = new LineDataSet(Hh, "H"); // add entries to dataset
            dataSet3.setDrawCircles(true);
            dataSet3.setColor(Color.GREEN);
            dataSet3.setValueTextColor(Color.GREEN); // styling, ...
            dataSet3.setDrawValues(true);

            dataSet4 = new LineDataSet(P, "P"); // add entries to dataset
            dataSet4.setDrawCircles(true);
            dataSet4.setColor(Color.YELLOW);
            dataSet4.setValueTextColor(Color.YELLOW); // styling, ...
            dataSet4.setDrawValues(true);

            dataSet5 = new LineDataSet(Ap, "A"); // add entries to dataset
            dataSet5.setDrawCircles(true);
            dataSet5.setColor(Color.GRAY);
            dataSet5.setValueTextColor(Color.GRAY); // styling, ...
            dataSet5.setDrawValues(true);

            lineData = new LineData();


            if (chart != null) {
                chart.setData(lineData);

            }


        }

        else {

            dataSet = new LineDataSet(TV, "TV"); // add entries to dataset
            dataSet.setDrawCircles(true);
            dataSet.setColor(Color.RED);
            dataSet.setValueTextColor(Color.RED); // styling, ...
            dataSet.setDrawValues(true);

            dataSet2 = new LineDataSet(TR, "TR"); // add entries to dataset
            dataSet2.setDrawCircles(true);
            dataSet2.setColor(Color.BLUE);
            dataSet2.setValueTextColor(Color.BLUE); // styling, ...
            dataSet2.setDrawValues(true);

            dataSet3 = new LineDataSet(Hh, "H"); // add entries to dataset
            dataSet3.setDrawCircles(true);
            dataSet3.setColor(Color.GREEN);
            dataSet3.setValueTextColor(Color.GREEN); // styling, ...
            dataSet3.setDrawValues(true);

            dataSet4 = new LineDataSet(P, "P"); // add entries to dataset
            dataSet4.setDrawCircles(true);
            dataSet4.setColor(Color.YELLOW);
            dataSet4.setValueTextColor(Color.YELLOW); // styling, ...
            dataSet4.setDrawValues(true);

            lineData = new LineData();


            if (chart != null) {
                chart.setData(lineData);

            }

        }


    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkAltura:
                if (checked){
                    lineData.addDataSet(dataSet3);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // Put some meat on the sandwich
                else{

                    lineData.removeDataSet(dataSet3);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // Remove the meat
                break;
            case R.id.checkReaccion:
                if (checked){
                    lineData.addDataSet(dataSet2);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // Cheese me
                else {
                    lineData.removeDataSet(dataSet2);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // I'm lactose intolerant
                break;
            case R.id.checkVuelo:
                if (checked){
                    lineData.addDataSet(dataSet);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // Cheese me
                else {
                    lineData.removeDataSet(dataSet);


                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);

                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                break;
            // TODO: Veggie sandwich
            case R.id.checkBoxPotencia:
                if (checked){
                    lineData.addDataSet(dataSet4);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // Cheese me
                else {
                    lineData.removeDataSet(dataSet4);


                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);

                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                break;
            case R.id.checkBoxApoyo:
                if (checked){
                    lineData.addDataSet(dataSet5);

                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);
                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                // Cheese me
                else {
                    lineData.removeDataSet(dataSet5);


                    // LineData lineData2 = new LineData(dataSet2);
                    if (chart != null) {
                        chart.setData(lineData);

                        // chart.setData(lineData2);
                        chart.invalidate(); // refresh
                    }
                }
                break;
        }
    }









}
