package com.app.epahibalo.e_pahibalo.NetworkRequest;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.epahibalo.e_pahibalo.Activities.RegisterActivity;
import com.app.epahibalo.e_pahibalo.Helpers.NetworkRequest;
import com.app.epahibalo.e_pahibalo.Helpers.URL;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckUsernameRequest extends NetworkRequest{

    Context context;
    RequestQueue requestQueue;
    String username;

    public CheckUsernameRequest(Context context, String username) {
        this.context = context;
        this.username = username;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void request() {
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL.REQUEST_CHECK_USERNAME,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Message msg = new Message();
                            msg.obj = response;
                            RegisterActivity.handler.sendMessage(msg);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            // error
                            //Log.d("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("username", username);
                    return params;
                }
            };
            requestQueue.add(postRequest);
    }
}
