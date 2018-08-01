package com.app.epahibalo.e_pahibalo.NetworkRequest;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.epahibalo.e_pahibalo.Activities.RegisterActivity;
import com.app.epahibalo.e_pahibalo.Helpers.NetworkRequest;
import com.app.epahibalo.e_pahibalo.Helpers.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationRequest extends NetworkRequest {

    Context context;
    RequestQueue requestQueue;
    JSONObject data;
    int result = 0;
    public RegistrationRequest(Context context, JSONObject data) {
        this.context = context;
        this.data = data;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void request() {

        JsonObjectRequest requestBarangayList = new JsonObjectRequest(Request.Method.POST, URL.REQUEST_REGISTER_USER,data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    result = response.getInt("affectedRows");
                    if (result == 1){
                        Message msg = new Message();
                        msg.obj = "REGISTER_OK";
                        RegisterActivity.handler.sendMessage(msg);
                        Log.d("HANDLER: ","REG "+"REGOK");
                    }else{
                        Message msg = new Message();
                        msg.obj = "REGISTER_NOT_OK";
                        RegisterActivity.handler.sendMessage(msg);
                        Log.d("HANDLER: ","REG "+"RENOTGOK");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Message msg = new Message();
                msg.obj = "REGISTER_ERROR";
                RegisterActivity.handler.sendMessage(msg);
            }
        });

        requestQueue.add(requestBarangayList);
    }


}
