package com.app.epahibalo.e_pahibalo.NetworkRequest;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.epahibalo.e_pahibalo.Activities.RegisterActivity;
import com.app.epahibalo.e_pahibalo.Helpers.NetworkRequest;
import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.app.epahibalo.e_pahibalo.Sections.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfileRequest extends NetworkRequest {

    Context context;
    RequestQueue requestQueue;
    JSONObject data;
    int result = 0;

    public UpdateProfileRequest(Context context, JSONObject data) {
        this.context = context;
        this.data = data;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void request() {
        JsonObjectRequest requestUpdateProfile = new JsonObjectRequest(Request.Method.POST, URL.REQUEST_UPDATE_PROFILE,data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    result = response.getInt("affectedRows");
                    if (result == 1){

                       Message msg = new Message();
                        msg.setTarget(ProfileFragment.handler);
                        msg.obj = "UPDATE_OK";
                        msg.sendToTarget();
                    }else{
                        Message msg = new Message();
                        msg.setTarget(ProfileFragment.handler);
                        msg.obj = "UPDATE_NOT_OK";
                        msg.sendToTarget();
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
                msg.setTarget(ProfileFragment.handler);
                msg.obj = "UPDATE_NOT_OK";
                msg.sendToTarget();
            }
        });

        requestQueue.add(requestUpdateProfile);
    }
}
