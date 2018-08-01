package com.app.epahibalo.e_pahibalo.Migrations;

import com.app.epahibalo.e_pahibalo.Helpers.Migration;

import java.util.ArrayList;
import java.util.List;

public class BarangayTable extends Migration{


    @Override
    public String tableName() {
        return "barangayinozamiz";
    }

    @Override
    public List<String> columns() {
        List<String> columns = new ArrayList<>();
        columns.add("barangay_id");
        columns.add("barangay");
        return columns;
    }

}
