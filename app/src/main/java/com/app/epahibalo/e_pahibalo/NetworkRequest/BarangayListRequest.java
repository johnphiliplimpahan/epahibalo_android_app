package com.app.epahibalo.e_pahibalo.NetworkRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.epahibalo.e_pahibalo.Helpers.NetworkRequest;
import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.app.epahibalo.e_pahibalo.Model.Barangay;

import org.json.JSONArray;

public class BarangayListRequest extends NetworkRequest {

    Context context;
    RequestQueue requestQueue;

    public BarangayListRequest(Context context) {
        this.context = context;
    }


    @Override
    public void request() {

        requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest requestBarangayList = new JsonArrayRequest(Request.Method.GET, URL.REQUEST_BARANGAY_LIST,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                new Barangay(context).saveAll(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("WAW: ",error.toString());
            }
        });

        requestQueue.add(requestBarangayList);
    }
}
