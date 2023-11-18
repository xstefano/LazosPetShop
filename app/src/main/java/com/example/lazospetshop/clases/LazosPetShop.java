package com.example.lazospetshop.clases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LazosPetShop extends SQLiteOpenHelper{
    private static final String nombreBD = "LazosPetShop.db";
    private static final int versionBD = 1;
    private static final String createTableUsuario = "CREATE TABLE IF NOT EXISTS USUARIO (ID INTEGER, CORREO VARCHAR(255), CLAVE VARCHAR(255))";
    private static final String dropTableUsuario = "DROP TABLE IF EXISTS USUARIO";
    //Constructor
    public LazosPetShop(@Nullable Context context) {
        super(context, nombreBD, null, versionBD);
    }

    //Se ejecuta cuando se llama al constructor
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableUsuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropTableUsuario);
        sqLiteDatabase.execSQL(createTableUsuario);
    }

    public boolean agregarUsuario(int id, String correo, String clave){
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            db.execSQL("INSERT INTO USUARIO VALUES("+id+", '"+correo+"', '"+clave+"')");
            db.close();
            ret = true;
        }
        return ret;
    }

    public boolean recordoSesion(){
        boolean ret = false;
        SQLiteDatabase db = getReadableDatabase();
        if(db != null){
            Cursor cursor = db.rawQuery("SELECT * FROM USUARIO", null);
            if(cursor.moveToNext())
                ret = true;
        }
        return ret;
    }

    public String extraerDatoUsuario(String campo){
        String data = "";
        SQLiteDatabase db = getReadableDatabase();
        String consulta = String.format("SELECT %s FROM USUARIO", campo);
        if(db != null){
            Cursor cursor = db.rawQuery(consulta, null);
            if(cursor.moveToNext())
                data = cursor.getString(0);
        }
        return data;
    }

    public boolean eliminarUsuario(int id){
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            db.execSQL("DELETE FROM USUARIO WHERE ID = "+id);
            db.close();
            ret = true;
        }
        return ret;
    }

    public boolean actualizarDatoUsuario(int id, String campo, String valor){
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            db.execSQL("UPDATE USUARIO SET "+campo+" = '"+valor+"' WHERE ID = "+id);
            db.close();
            ret = true;
        }
        return ret;
    }
}
