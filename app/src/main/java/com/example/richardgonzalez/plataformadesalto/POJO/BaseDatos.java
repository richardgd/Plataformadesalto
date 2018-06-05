package com.example.richardgonzalez.plataformadesalto.POJO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */
public class BaseDatos extends SQLiteOpenHelper {
    //Base de datos
    private Context context;
    public BaseDatos(Context context) {
        super(context, ConstanteBaseDatos.DATABASE_NAME, null , ConstanteBaseDatos.DATABASE_VERSION);
        this.context=context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creacion de Base de Datos
        String queryCrearTablaMiembros = "CREATE TABLE " + ConstanteBaseDatos.TABLE_MEDIDA + "(" +
         ConstanteBaseDatos.TABLE_MEDIDA_ID + " TEXT PRIMARY KEY AUTOINCREMENT, " +
                ConstanteBaseDatos.TABLE_MEDIDA_TV + " TEXT, " +
                ConstanteBaseDatos.TABLE_MEDIDA_TR + " TEXT, " +
                ConstanteBaseDatos.TABLE_MEDIDA_H + " TEXT, " +
                ConstanteBaseDatos.TABLE_MEDIDA_P + " TEXT, "+
                ConstanteBaseDatos.TABLE_MEDIDA_TA + " TEXT"+
                ConstanteBaseDatos.TABLE_MEDIDA_TIPO_SALTO + " TEXT"+
                ")";



        db.execSQL(queryCrearTablaMiembros);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST" + ConstanteBaseDatos.TABLE_MEDIDA);
        onCreate(db);

    }

    public ArrayList<MedidaOffline> obtenerTodosLosMiembros(){

        ArrayList<MedidaOffline> medidaOfflines= new ArrayList<>();
        String query = "SELECT * FROM " + ConstanteBaseDatos.TABLE_MEDIDA;
        //Se lee la base de datos
        SQLiteDatabase db = this.getReadableDatabase();
        //Modo de lectura, recorrer registros
       Cursor registros=  db.rawQuery(query, null);

        while (registros.moveToNext()){

             MedidaOffline medidaOFF = new MedidaOffline(registros.getString(1), registros.getString(2), registros.getString(3),registros.getString(0),registros.getString(4),registros.getString(5));
            // El numero es la posicion de la tabla que esta arriba
           medidaOfflines.add(medidaOFF);
        }
        db.close();

        return  medidaOfflines;

    }

    public void InsertarMiembros(ContentValues contentValues){
    //Recible la tabla un contenedor de valores
        
        SQLiteDatabase db= this.getWritableDatabase();
        db.insert(ConstanteBaseDatos.TABLE_MEDIDA, null, contentValues);
        db.close();

    }

    public void Delete(){
        //Recible la tabla un contenedor de valores

        SQLiteDatabase db= this.getWritableDatabase();
        String query = "SELECT * FROM " + ConstanteBaseDatos.TABLE_MEDIDA;
        int i = 0;
        Cursor registros=  db.rawQuery(query, null);
        while (registros.moveToNext()) {
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_H , new String[i]);
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_ID , new String[i]);
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_P , new String[i]);
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_TIPO_SALTO , new String[i]);
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_TR , new String[i]);
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_TV , new String[i]);
            db.delete(ConstanteBaseDatos.TABLE_MEDIDA, ConstanteBaseDatos.TABLE_MEDIDA_TR , new String[i]);

            i++;
        }
        db.close();

    }

}
