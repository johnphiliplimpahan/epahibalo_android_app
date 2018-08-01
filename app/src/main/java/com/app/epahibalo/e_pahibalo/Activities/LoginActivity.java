package com.app.epahibalo.e_pahibalo.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.app.epahibalo.e_pahibalo.Helpers.UserSession;
import com.app.epahibalo.e_pahibalo.NetworkRequest.LoginRequest;
import com.app.epahibalo.e_pahibalo.R;
import com.app.epahibalo.e_pahibalo.SocketIntentService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    // UI references.
    private Button signIn,createAccount;
    private Intent intent;
    MaterialEditText usernameField,passwordField;
    private HashMap<String,String> userCredentials;
    private ProgressDialog dialog;
    public static Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        signIn = findViewById(R.id.sign_in_button);
        createAccount = findViewById(R.id.create_account_button);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);

        userCredentials = new HashMap<>();
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Logging in");


        signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dialog.isShowing()){
                    dialog.dismiss();
                }

                dialog.show();
                userCredentials.put("username",usernameField.getText().toString());
                userCredentials.put("password",passwordField.getText().toString());
                new LoginRequest(LoginActivity.this,userCredentials).request();
            }
        });

        createAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String STATUS = (String)msg.obj;

                if(STATUS.equals("LOGIN_OK")){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    UserSession.username = usernameField.getText().toString().trim();

                    Intent i = new Intent(LoginActivity.this,FeedActivity.class);
                    i.putExtra("username",usernameField.getText().toString().trim());
                    startActivity(i);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2000);

                }

                if (STATUS.equals("LOGIN_NOT_OK")){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this,"No such user",Toast.LENGTH_LONG).show();
                }

                if (STATUS.equals("LOGIN_ERROR")){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this,"There was an error connecting to the server",Toast.LENGTH_LONG).show();
                }
            }
        };
    }



}

