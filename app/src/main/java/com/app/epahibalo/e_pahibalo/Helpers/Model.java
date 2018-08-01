package com.app.epahibalo.e_pahibalo.Helpers;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

public abstract class Model {



    public abstract void saveAll(JSONArray data);

    public abstract String table();
    public abstract boolean exists();
    public abstract SQLiteDatabase read();
    public abstract SQLiteDatabase write();
    public abstract void close();
    public abstract int count();
}
