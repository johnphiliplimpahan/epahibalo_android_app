package com.app.epahibalo.e_pahibalo.NetworkRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.epahibalo.e_pahibalo.Activities.FeedActivity;
import com.app.epahibalo.e_pahibalo.Activities.LoginActivity;
import com.app.epahibalo.e_pahibalo.Activities.RegisterActivity;
import com.app.epahibalo.e_pahibalo.Helpers.NetworkRequest;
import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends NetworkRequest {

    Context context;
    RequestQueue requestQueue;
    HashMap<String,String> userCredentials;
    Intent intent;
    private final String loginClass = "class com.app.epahibalo.e_pahibalo.Activities.LoginActivity";
    private final String registerClass = "class com.app.epahibalo.e_pahibalo.Activities.RegisterActivity";
    public LoginRequest(Context context,HashMap<String,String> userCredentials) {
        this.context = context;
        this.userCredentials = userCredentials;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void request() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL.REQUEST_LOGIN_USER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("LOGIN_OK")) {
                            if(context.getClass().toString().equals(loginClass)){
                                Message msg = new Message();
                                msg.obj = response;
                                LoginActivity.handler.sendMessage(msg);
                            }else if (context.getClass().toString().equals(registerClass)){
                                Message msg = new Message();
                                msg.obj = response;
                                RegisterActivity.handler.sendMessage(msg);
                            }else{
                                Toast.makeText(context,"Unauthorized Login Attempt",Toast.LENGTH_LONG).show();
                            }
                        } else if (response.equals("LOGIN_NOT_OK")){
                            if(context.getClass().toString().equals(loginClass)){
                                Message msg = new Message();
                                msg.obj = response;
                                LoginActivity.handler.sendMessage(msg);
                            }else if (context.getClass().toString().equals(registerClass)){
                                Message msg = new Message();
                                msg.obj = response;
                                RegisterActivity.handler.sendMessage(msg);
                            }else{
                                Toast.makeText(context,"Unauthorized Login Attempt",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(context,"LOGIN ERROR: CHECK CONNECTION",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(context.getClass().toString().equals(loginClass)){
                            Message msg = new Message();
                            msg.obj = "LOGIN_ERROR";
                            LoginActivity.handler.sendMessage(msg);
                        }else if (context.getClass().toString().equals(registerClass)){
                            Message msg = new Message();
                            msg.obj = "LOGIN_ERROR";
                            RegisterActivity.handler.sendMessage(msg);
                        }else{
                            Toast.makeText(context,"Unauthorized Login Attempt",Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return userCredentials;
            }
        };
        requestQueue.add(postRequest);
    }
}
