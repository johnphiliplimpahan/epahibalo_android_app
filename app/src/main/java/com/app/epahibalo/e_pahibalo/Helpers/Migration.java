package com.app.epahibalo.e_pahibalo.Helpers;

import java.util.Iterator;
import java.util.List;

public abstract class Migration {

    public String create(){
        String createTable = "";
        createTable+="CREATE TABLE IF NOT EXISTS "+tableName()+" (";
        Iterator<String> columns = columns().iterator();
        while(columns.hasNext()){
            createTable+=columns.next()+" TEXT,";
        }
        createTable+=")";
        return finalizeQuery(createTable);
    }

    public abstract String tableName();
    public abstract List<String> columns();

    public String finalizeQuery(String query){
        return new StringBuilder(query).deleteCharAt(query.length() -2).toString();
    }

    public String drop(){
        return "DROP TABLE IF EXISTS "+tableName();
    }


}
