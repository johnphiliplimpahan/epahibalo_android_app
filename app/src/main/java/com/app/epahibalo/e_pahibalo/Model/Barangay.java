package com.app.epahibalo.e_pahibalo.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.app.epahibalo.e_pahibalo.Activities.RegisterActivity;
import com.app.epahibalo.e_pahibalo.Helpers.LocalStorage;
import com.app.epahibalo.e_pahibalo.Helpers.Model;
import com.app.epahibalo.e_pahibalo.Migrations.BarangayTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Iterator;

public class Barangay extends Model {

    LocalStorage db;
    Context context;
    public Barangay(Context context) {
        this.db = new LocalStorage(context);
        this.context = context;
    }



    @Override
    public void saveAll(JSONArray data) {
        Message msg = new Message();

        if(data.length() != count()) {

            ContentValues values = new ContentValues();
            for (int x = 0; x < data.length(); x++) {
                try {

                    JSONObject store = data.getJSONObject(x);

                    Iterator<String> fields = store.keys();
                    while (fields.hasNext()) {
                        String field = fields.next();
                        String value = store.getString(field);
                        Log.d("WAW: ","FIELD: "+field+" VALUE: "+value);
                        values.put(field, value);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                write().insert(table(), null, values);
                msg.obj = "DONE";
            }
        }else{
            msg.obj = "DONE";
            Log.d("WAW: ","JSON: "+data.length()+" COUNT: "+count());
        }
        RegisterActivity.handler.sendMessage(msg);
        close();
    }

    @Override
    public String table() {
        return new BarangayTable().tableName();
    }

    @Override
    public boolean exists() {
        Cursor cursor = read().rawQuery("SELECT name FROM sqlite_master WHERE type='table' and name='"+table()+"'",null);
        if(cursor.getCount() == 1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SQLiteDatabase write() {
        return this.db.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase read() {
        return this.db.getReadableDatabase();
    }

    @Override
    public void close() {
        this.db.close();
    }

    @Override
    public int count() {
        Cursor cursor = read().rawQuery("SELECT * FROM "+table(),null);

        return cursor.getCount();
    }

    public ArrayAdapter<String> barangayListViewAdapter(){

        ArrayAdapter<String> list = new ArrayAdapter<String>(context,android.R.layout.select_dialog_singlechoice);
        Cursor cursor = read().rawQuery("SELECT barangay FROM "+table(),null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        close();
        return list;

    }

    public ArrayAdapter<String> barangaySpinnerAdapter(){
        ArrayAdapter<String> list = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item);
        Cursor cursor = read().rawQuery("SELECT barangay FROM "+table(),null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        close();
        return list;
    }
}
