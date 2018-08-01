package com.app.epahibalo.e_pahibalo.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.epahibalo.e_pahibalo.Model.Barangay;
import com.app.epahibalo.e_pahibalo.NetworkRequest.BarangayListRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.CheckMobileNumberRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.CheckUsernameRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.LoginRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.RegistrationRequest;
import com.app.epahibalo.e_pahibalo.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText ca_firstname,ca_middlename,ca_lastname,
            ca_bday,
            ca_gender,ca_contact_no,ca_street,ca_barangay,ca_zipcode,ca_city,
            ca_username,ca_password,ca_conf_password;
    Button ca_create_account_btn;

    DatePickerDialog.OnDateSetListener dateSetListener;
    ProgressDialog dialog;
    public static Handler handler;
    Thread barangayThread;
    ArrayAdapter<String> barangayList,genderList;
    JSONObject data;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ca_firstname = findViewById(R.id.ca_firstname);
        ca_middlename = findViewById(R.id.ca_middlename);
        ca_lastname = findViewById(R.id.ca_lastname);

        ca_bday = findViewById(R.id.ca_bdate);
        ca_barangay = findViewById(R.id.ca_barangay);
        ca_gender = findViewById(R.id.ca_gender);
        ca_create_account_btn = findViewById(R.id.ca_registerBtn);

        ca_contact_no = findViewById(R.id.ca_contactno);
        ca_street = findViewById(R.id.ca_streetpurok);
        ca_city = findViewById(R.id.ca_city);
        ca_zipcode = findViewById(R.id.ca_zipcode);

        ca_username = findViewById(R.id.ca_username);
        ca_password = findViewById(R.id.ca_password);
        ca_conf_password = findViewById(R.id.ca_conf_password);
        data = new JSONObject();



        dialog = new ProgressDialog(RegisterActivity.this);

        dialog.setMessage("Loading please wait");
        dialog.show();
        barangayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                new BarangayListRequest(RegisterActivity.this).request();
            }
        });

        barangayThread.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String resp = (String)msg.obj;

                if(resp.equals("DONE")){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }

                    if(barangayThread.isAlive()){
                        barangayThread.stop();
                    }

                }


            }
        };


        ca_barangay.setKeyListener(null);
        ca_gender.setKeyListener(null);
        ca_bday.setKeyListener(null);
        ca_city.setKeyListener(null);

        ca_bday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            RegisterActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            dateSetListener,year,month,day
                    );

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }

            }

        });

        ca_gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showGenderList();
                }
            }
        });

        ca_barangay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showBarangayList();
                }
            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year+"-"+month+"-"+dayOfMonth;
                ca_bday.setText(date);
            }
        };

        ca_create_account_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validate();
            }

        });
    }

    public void showGenderList(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterActivity.this);
        builderSingle.setTitle("Select Gender");

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        genderList = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.select_dialog_singlechoice);
        genderList.add("Male");
        genderList.add("Female");

        builderSingle.setAdapter(genderList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = genderList.getItem(which);
                ca_gender.setText(strName);
            }
        });
        builderSingle.show();
    }

    public void showBarangayList(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterActivity.this);
        builderSingle.setTitle("Select barangay");

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        barangayList = new Barangay(RegisterActivity.this).barangayListViewAdapter();

        builderSingle.setAdapter(barangayList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = barangayList.getItem(which);
                ca_barangay.setText(strName);
            }
        });

        builderSingle.show();
    }



    @SuppressLint("HandlerLeak")
    public void validate(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }

        dialog.setMessage("Checking for invalid inputs");
        dialog.show();
        try {


            if(ca_lastname.getText().toString().trim().length() == 0){
                ca_lastname.setError("Last Name cannot be empty");
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }else{
                data.put("lastname",ca_lastname.getText().toString().trim());
                if(ca_firstname.getText().toString().trim().length() == 0){
                    ca_firstname.setError("First Name cannot be empty");
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }else{
                    data.put("firstname",ca_firstname.getText().toString().trim());
                    data.put("middlename",ca_middlename.getText().toString().trim());
                    if(ca_bday.getText().toString().trim().length() == 0){
                        ca_bday.setError("Please choose your birth date");
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }else{
                        data.put("dateofbirth",ca_bday.getText().toString().trim());
                        if(ca_gender.getText().toString().trim().length() == 0){
                            ca_gender.setError("Please select your gender");
                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }else{
                            String gender = ca_gender.getText().toString().trim().equals("Male") ? "M" : "F";
                            data.put("sex",gender);

                            String contact_no =ca_contact_no.getText().toString().trim();

                            if(contact_no.length() != 11){

                                ca_contact_no.setError("Contact Number must have 11 digits");
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }else {
                                if (contact_no.substring(0,2).equals("09")){

                                    if(ca_street.getText().toString().trim().length() == 0){
                                        ca_street.setError("Please write your street or purok");
                                        if(dialog.isShowing()){
                                            dialog.dismiss();
                                        }
                                    }else{
                                        data.put("streetpurok",ca_street.getText().toString().trim());
                                        if(ca_barangay.getText().toString().trim().length() == 0){
                                            ca_barangay.setError("Please select your barangay");
                                            if(dialog.isShowing()){
                                                dialog.dismiss();
                                            }
                                        }else{
                                            data.put("brgy",ca_barangay.getText().toString().trim());
                                            data.put("city",ca_city.getText().toString().trim());
                                            data.put("zipcode",ca_zipcode.getText().toString().trim());
                                            if(ca_username.getText().toString().trim().length() >= 6){
                                                if(ca_password.getText().toString().trim().length() >= 8){
                                                    String pass = ca_password.getText().toString().trim();
                                                    String conf = ca_conf_password.getText().toString().trim();

                                                    if(conf.equals(pass)){
                                                        data.put("password",pass);
                                                        dialog.setMessage("Validating information");
                                                        new CheckUsernameRequest(RegisterActivity.this,ca_username.getText().toString().trim()).request();
                                                        new CheckMobileNumberRequest(RegisterActivity.this,ca_contact_no.getText().toString().trim()).request();

                                                        final int[] count = {0};
                                                        handler = new Handler(){
                                                            @Override
                                                            public void handleMessage(Message msg) {
                                                                String status = (String)msg.obj;


                                                                if(status.equals("USERNAME_OK")){
                                                                    Log.d("HANDLER: ","WAW "+status);
                                                                    if(ca_username.getError() != null){
                                                                        ca_username.setErrorColor(Color.GREEN);
                                                                        ca_username.setError("Username is not used");
                                                                    }
                                                                    try {
                                                                        data.put("username",ca_username.getText().toString().trim());
                                                                        count[0]++;
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                if (status.equals("USERNAME_NOT_OK")){
                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                        ca_username.setErrorColor(Color.RED);
                                                                        ca_username.setError("Username already used");
                                                                    }
                                                                }

                                                                if(status.equals("MOBILENUM_OK")){
                                                                    Log.d("HANDLER: ","WAW "+status);
                                                                    if(ca_contact_no.getError() != null){
                                                                        ca_contact_no.setErrorColor(Color.GREEN);
                                                                        ca_contact_no.setError("Mobile number is not used");
                                                                    }
                                                                    try {
                                                                        data.put("mobilenum",ca_contact_no.getText().toString().trim());
                                                                        count[0]++;
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }


                                                                }
                                                                if (status.equals("MOBILENUM_NOT_OK")){
                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                        ca_contact_no.setErrorColor(Color.RED);
                                                                        ca_contact_no.setError("Mobile number already used");
                                                                    }
                                                                }


                                                                if(count[0] == 2){
                                                                    dialog.setMessage("Submitting information to the server");

                                                                    if(data.length() == 12){
                                                                        new RegistrationRequest(RegisterActivity.this,data).request();
                                                                        count[0] = 0;
                                                                        data = null;
                                                                    }else{
                                                                        if(dialog.isShowing()){
                                                                            dialog.dismiss();
                                                                            Toast.makeText(RegisterActivity.this,"There are some missing data",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                }

                                                                if(status.equals("OK")){
                                                                    Log.d("HANDLER: ","WAW "+status);
                                                                    if(ca_username.getError() != null){
                                                                        ca_username.setError(null);
                                                                    }
                                                                }
                                                                if (status.equals("NOT_OK")){
                                                                    Log.d("HANDLER: ","WAW "+status);
                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                        ca_username.setError("Username is not available");
                                                                    }
                                                                }

                                                                if (status.equals("REGISTER_OK")){

                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                    }
                                                                    HashMap<String,String> userCredentials = new HashMap<>();
                                                                    userCredentials.put("username",ca_username.getText().toString().trim());
                                                                    userCredentials.put("password",ca_password.getText().toString().trim());
                                                                    new LoginRequest(RegisterActivity.this,userCredentials).request();
                                                                    Log.d("HANDLER: ","MA "+ca_username.getText().toString()+" B"+ca_password.getText().toString().trim());
                                                                }

                                                                if (status.equals("REGISTER_NOT_OK")){

                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                    }
                                                                    Toast.makeText(RegisterActivity.this,"Registration Failed: An error occurred during data submission",Toast.LENGTH_LONG).show();
                                                                }

                                                                if (status.equals("REGISTER_ERROR")){

                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                    }
                                                                    Toast.makeText(RegisterActivity.this,"Registration Failed: Connection Timed Out",Toast.LENGTH_LONG).show();
                                                                }

                                                                if (status.equals("LOGIN_OK")){
                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                    }
                                                                    Intent i = new Intent(RegisterActivity.this,FeedActivity.class);
                                                                    i.putExtra("username",ca_username.getText().toString().trim());
                                                                    startActivity(i);
                                                                    finish();
                                                                }

                                                                if (status.equals("LOGIN_NOT_OK")){
                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                    }
                                                                    Toast.makeText(RegisterActivity.this,"Registration Failed: Information was not submitted to the server",Toast.LENGTH_LONG).show();
                                                                }

                                                                if (status.equals("LOGIN_ERROR")){
                                                                    if(dialog.isShowing()){
                                                                        dialog.dismiss();
                                                                    }
                                                                    Toast.makeText(RegisterActivity.this,"There was an error connecting to the server",Toast.LENGTH_LONG).show();
                                                                }

                                                            }
                                                        };
                                                    }else{
                                                        ca_conf_password.setError("Passwords do not match");
                                                        if(dialog.isShowing()){
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                }else{
                                                    ca_password.setError("Password must be at least 8 characters");
                                                    if(dialog.isShowing()){
                                                        dialog.dismiss();
                                                    }
                                                }
                                            }else{
                                                ca_username.setError("Username must be at least 6 characters");
                                                if(dialog.isShowing()){
                                                    dialog.dismiss();
                                                }
                                            }

                                        }
                                    }
                                }else {
                                    ca_contact_no.setError("Contact number must begin with '09'");
                                    if(dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("HANDLER: ","RUNNING MAN");
    }




}
