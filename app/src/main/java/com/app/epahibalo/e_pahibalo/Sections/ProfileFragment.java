package com.app.epahibalo.e_pahibalo.Sections;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.epahibalo.e_pahibalo.Activities.FeedActivity;
import com.app.epahibalo.e_pahibalo.Activities.RegisterActivity;
import com.app.epahibalo.e_pahibalo.Helpers.DateParser;
import com.app.epahibalo.e_pahibalo.Helpers.GlideApp;
import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.app.epahibalo.e_pahibalo.Helpers.UserSession;
import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemAdapters.FeedItemAdapter;
import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemModels.FeedItem;
import com.app.epahibalo.e_pahibalo.Model.Barangay;
import com.app.epahibalo.e_pahibalo.NetworkRequest.CheckMobileNumberRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.CheckUsernameRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.LoginRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.RegistrationRequest;
import com.app.epahibalo.e_pahibalo.NetworkRequest.UpdateProfileRequest;
import com.app.epahibalo.e_pahibalo.R;
import com.app.epahibalo.e_pahibalo.SocketIntentService;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    ImageView profilePicture;
    Button editProfilePictureBtn,updateProfileBtn;
    TextView profileCitizenID,profileDobLabel,profileGenderLabel,profileContactLabel,profileAddressLabel,profileCaretDown;
    TextView profileFullName,profileContactNo,profileAddress,profileDob,profileGender;

    ConstraintLayout my_profile_layout;
    DateParser parser;
    private FeedItemAdapter feedItemAdapter;
    private List<FeedItem> feedItemsList = new ArrayList<>();
    RecyclerView recyclerProfile;
    boolean isShowing = false;

    DatePickerDialog.OnDateSetListener dateSetListener;

    ArrayAdapter<String> barangayList,genderList;
    JSONObject profileData;
    JSONObject data = new JSONObject();
    private static final int CAMERA_REQUEST_CODE = 101;
    protected static final int INCIDENT_SUBMITTED = 3;
    public static Handler handler;
    public static Bundle profee;
    ProgressDialog dialog;
    MaterialEditText firstname, middlename,lastname,dob,gender,contact,streetpurok,brgy,city,zipcode;
    Button editBtn;

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_profile,container,false);

        parser = new DateParser();
        profileData = new JSONObject();
        my_profile_layout = view.findViewById(R.id.my_profile_layout);
        profilePicture = (ImageView)view.findViewById(R.id.profilePicture);
        editProfilePictureBtn = (Button)view.findViewById(R.id.editProfilePictureBtn);

        updateProfileBtn = view.findViewById(R.id.profile_updateBtn);

        profileFullName = view.findViewById(R.id.profile_fullname);
        profileCitizenID = view.findViewById(R.id.profileCitizenID);
        profileContactNo = view.findViewById(R.id.profile_contact_no);
        profileAddress = view.findViewById(R.id.profile_address);
        profileAddressLabel = view.findViewById(R.id.profileAddressLabel);
        profileContactLabel = view.findViewById(R.id.profileContactLabel);
        profileCaretDown = view.findViewById(R.id.profileCaretDown);

        profileDobLabel = view.findViewById(R.id.profile_dob_label);
        profileDob = view.findViewById(R.id.profile_dob);

        profileGenderLabel = view.findViewById(R.id.profile_gender_label);
        profileGender = view.findViewById(R.id.profile_gender);
        dialog = new ProgressDialog(getContext());

        recyclerProfile = view.findViewById(R.id.recyclerProfile);
        feedItemAdapter = new FeedItemAdapter(feedItemsList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerProfile.setLayoutManager(layoutManager);
        recyclerProfile.setItemAnimator(new DefaultItemAnimator());
        recyclerProfile.setAdapter(feedItemAdapter);



        my_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowing){

//                    final float scale = getResources().getDisplayMetrics().density;
//                    int dpWidthInPx  = (int) (100 * scale);
//                    int dpHeightInPx = (int) (100 * scale);
//                    profilePicture.getLayoutParams().width = dpWidthInPx;
//                    profilePicture.getLayoutParams().height = dpHeightInPx;

                    showDetails(profileContactNo);
                    showDetails(profileAddress);
                    showDetails(profileDob);
                    showDetails(profileDobLabel);
                    showDetails(profileGender);
                    showDetails(profileGenderLabel);
                    showDetails(profileAddressLabel);
                    showDetails(profileContactLabel);
                    hideDetails(profileCaretDown);
                    //showDetails(editProfilePictureBtn);
                    showDetails(updateProfileBtn);
                    isShowing = true;
                }else{
//                    final float scale = getResources().getDisplayMetrics().density;
//                    int dpWidthInPx  = (int) (40 * scale);
//                    int dpHeightInPx = (int) (40 * scale);
//                    profilePicture.getLayoutParams().width = dpWidthInPx;
//                    profilePicture.getLayoutParams().height = dpHeightInPx;

                    hideDetails(profileContactNo);
                    hideDetails(profileAddress);
                    hideDetails(profileDob);
                    hideDetails(profileDobLabel);
                    hideDetails(profileGender);
                    hideDetails(profileGenderLabel);
                    hideDetails(profileAddressLabel);
                    hideDetails(profileContactLabel);
                    showDetails(profileCaretDown);
                    //hideDetails(editProfilePictureBtn);
                    hideDetails(updateProfileBtn);
                    isShowing = false;
                }


            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.update_profile_dialog, null);
                dialogBuilder.setView(dialogView);

                 firstname = dialogView.findViewById(R.id.update_profile_firstname);
                 middlename = dialogView.findViewById(R.id.update_profile_middlename);
                 lastname = dialogView.findViewById(R.id.update_profile_lastname);
                 dob = dialogView.findViewById(R.id.update_profile_bdate);
                 gender = dialogView.findViewById(R.id.update_profile_gender);
                 contact = dialogView.findViewById(R.id.update_profile_contactno);
                 streetpurok = dialogView.findViewById(R.id.update_profile_street);
                 brgy = dialogView.findViewById(R.id.update_profile_brgy);
                 city = dialogView.findViewById(R.id.update_profile_city);
                 zipcode = dialogView.findViewById(R.id.update_profile_zipcode);
                 editBtn = dialogView.findViewById(R.id.update_profile_updateBtn);

                try {
                    firstname.setText(profileData.getString("firstname"));
                    middlename.setText(profileData.getString("middlename"));
                    lastname.setText(profileData.getString("lastname"));
                    dob.setText(parser.convertDate(profileData.getString("dob")));
                    gender.setText(profileData.getString("gender").equals("M") ? "Male" : "Female");
                    contact.setText(profileData.getString("mobile"));
                    streetpurok.setText(profileData.getString("streetpurok"));
                    brgy.setText(profileData.getString("brgy"));
                    city.setText(profileData.getString("city"));
                    zipcode.setText(profileData.getString("zipcode"));

                    dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                            if(hasFocus){
                                Calendar cal = Calendar.getInstance();
                                int year = cal.get(Calendar.YEAR);
                                int month = cal.get(Calendar.MONTH);
                                int day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialog = new DatePickerDialog(
                                        getContext(),
                                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                        dateSetListener,year,month,day
                                );

                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                            }

                        }

                    });

                    dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            String date = year+"-"+month+"-"+dayOfMonth;
                            dob.setText(date);
                        }
                    };

                    gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                                builderSingle.setTitle("Select Gender");

                                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                genderList = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_singlechoice);
                                genderList.add("Male");
                                genderList.add("Female");

                                builderSingle.setAdapter(genderList, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String strName = genderList.getItem(which);
                                        gender.setText(strName);
                                    }
                                });
                                builderSingle.show();
                            }

                        }
                    });


                    brgy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                                builderSingle.setTitle("Select barangay");

                                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                barangayList = new Barangay(getContext()).barangayListViewAdapter();

                                builderSingle.setAdapter(barangayList, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String strName = barangayList.getItem(which);
                                        brgy.setText(strName);
                                    }
                                });

                                builderSingle.show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.show();

                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }

                        dialog.setMessage("Checking for invalid inputs");
                        dialog.show();
                        try {


                            if(lastname.getText().toString().trim().length() == 0){
                                lastname.setError("Last Name cannot be empty");
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }else{
                                data.put("lastname",lastname.getText().toString().trim());
                                if(firstname.getText().toString().trim().length() == 0){
                                    firstname.setError("First Name cannot be empty");
                                    if(dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                }else{
                                    data.put("firstname",firstname.getText().toString().trim());
                                    data.put("middlename",middlename.getText().toString().trim());
                                    if(dob.getText().toString().trim().length() == 0){
                                        dob.setError("Please choose your birth date");
                                        if(dialog.isShowing()){
                                            dialog.dismiss();
                                        }
                                    }else{
                                        data.put("dateofbirth",dob.getText().toString().trim());
                                        if(gender.getText().toString().trim().length() == 0){
                                            gender.setError("Please select your gender");
                                            if(dialog.isShowing()){
                                                dialog.dismiss();
                                            }
                                        }else{
                                            //String gender = gender.getText().toString().trim().equals("Male") ? "M" : "F";
                                            data.put("sex",gender.getText().toString().trim());

                                            String contact_no =contact.getText().toString().trim();

                                            if(contact_no.length() != 11){

                                                contact.setError("Contact Number must have 11 digits");

                                                if(dialog.isShowing()){
                                                    dialog.dismiss();
                                                }
                                            }else {
                                                if (contact_no.substring(0,2).equals("09")){
                                                    data.put("mobilenum",contact_no);

                                                    if(streetpurok.getText().toString().trim().length() == 0){
                                                        streetpurok.setError("Please write your street or purok");
                                                        if(dialog.isShowing()){
                                                            dialog.dismiss();
                                                        }
                                                    }else{
                                                        data.put("streetpurok",streetpurok.getText().toString().trim());
                                                        if(brgy.getText().toString().trim().length() == 0){
                                                            brgy.setError("Please select your barangay");
                                                            if(dialog.isShowing()){
                                                                dialog.dismiss();
                                                            }
                                                        }else{
                                                            data.put("citizen_id",profileCitizenID.getText().toString());
                                                            data.put("brgy",brgy.getText().toString().trim());
                                                            data.put("city",city.getText().toString().trim());
                                                            data.put("zipcode",zipcode.getText().toString().trim());
                                                            data.put("address",
                                                                    streetpurok.getText().toString().trim()+", "+
                                                                            brgy.getText().toString().trim()+" "+
                                                                            city.getText().toString().trim()

                                                            );
                                                            data.put("fullname",
                                                                    firstname.getText().toString().trim()+" "+
                                                                            middlename.getText().toString().trim()+" "+
                                                                            lastname.getText().toString().trim()
                                                            );
                                                            SocketIntentService.checkMobileNumber(contact_no,profileCitizenID.getText().toString());

                                                            dialog.setMessage("Verifying Updated Citizen Profile");

                                                        }
                                                    }
                                                }else {
                                                    contact.setError("Contact number must begin with '09'");
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

                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });


                handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                switch (msg.arg1){
                    case INCIDENT_SUBMITTED :
                        FeedItem newItem = (FeedItem)msg.obj;

                        feedItemsList.add(0,newItem);
                        feedItemAdapter.notifyDataSetChanged();
                        break;
                    default:
                        if(msg.obj instanceof Bitmap){
                            Bitmap image = (Bitmap)msg.obj;

                            if(image != null){
                                GlideApp.with(getContext())
                                        .clear(profilePicture);

                                GlideApp.with(getContext())
                                        .load(image)
                                        .into(profilePicture);
                            }
                        }else if(msg.obj instanceof JSONObject){
                            JSONObject data = (JSONObject)msg.obj;

                            try {
                                String citizen_id = data.getString("citizen_id");
                                String firstname = data.getString("firstname");
                                String middlename = data.getString("middlename");
                                String lastname = data.getString("lastname");
                                String dob = data.getString("dateofbirth");
                                String gender = data.getString("sex");
                                String profilePic = data.getString("profilepic");
                                String streetpurok = data.getString("streetpurok");
                                String brgy = data.getString("brgy");
                                String city = data.getString("city");
                                String mobile = data.getString("mobilenum");
                                String zipcode = data.getString("zipcode");

                                profileData.put("citizen_id",citizen_id);
                                profileData.put("profilepic",profilePic);
                                profileData.put("firstname",firstname);
                                profileData.put("middlename",middlename);
                                profileData.put("lastname",lastname);
                                profileData.put("dob",dob);
                                profileData.put("gender",gender);
                                profileData.put("mobile",mobile);
                                profileData.put("streetpurok",streetpurok);
                                profileData.put("brgy",brgy);
                                profileData.put("city",city);
                                profileData.put("zipcode",zipcode);


                                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
                                circularProgressDrawable.setStrokeWidth(5f);
                                circularProgressDrawable.setCenterRadius( 30f);
                                circularProgressDrawable.start();


                                GlideApp.with(getContext())
                                        .load(URL.IMAGE_PATH+profilePic)
                                        .placeholder(circularProgressDrawable)
                                        .error(R.drawable.placeholder_image)
                                        .into(profilePicture);

                                profileCitizenID.setText(citizen_id);
                                profileFullName.setText(firstname+" "+middlename+" "+lastname);
                                profileContactNo.setText(mobile);
                                profileAddress.setText(streetpurok+", "+brgy+" "+city);
                                profileDob.setText(parser.convertDate(dob));
                                profileGender.setText(gender.equals("M") ? "Male" : "Female");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(msg.obj instanceof FeedItem){
                            FeedItem item = (FeedItem) msg.obj;

                            feedItemsList.add(item);
                            feedItemAdapter.notifyDataSetChanged();
                        }else if (msg.obj instanceof String){
                            String update = (String)msg.obj;

                            if(update.equals("UPDATE_OK")){
                                if (dialog.isShowing()){
                                    dialog.dismiss();
                                    Toast.makeText(getContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                                    try {
                                        profileFullName.setText(data.getString("fullname"));
                                        profileContactNo.setText(data.getString("mobilenum"));
                                        profileGender.setText(data.getString("sex"));
                                        profileDob.setText(new DateParser().convertDate(data.getString("dateofbirth")));
                                        profileAddress.setText(data.getString("address"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else if(update.equals("UPDATE_NOT_OK")){
                                if (dialog.isShowing()){
                                    dialog.dismiss();
                                    Toast.makeText(getContext(),"Profile Update Failed",Toast.LENGTH_SHORT).show();
                                }
                            }else if (update.equals("UPDATE_MOBILENUM_OK")){
                                if (dialog.isShowing()){
                                    dialog.setMessage("Submitting Profile Information");
                                    if(data != null){
                                        new UpdateProfileRequest(getContext(),data).request();
                                    }
                                }
                            }else if (update.equals("UPDATE_MOBILENUM_NOT_OK")){
                                if (dialog.isShowing()){
                                    dialog.dismiss();
                                    contact.setError("Mobile number already used");
                                }
                            }
                        }
                        break;
                }




            }
        };



        editProfilePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return view;
    }



    public void showDetails(final View view){

        view.clearAnimation();

        view.animate()
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                }).start();

    }

    public void hideDetails(final View view){

        view.clearAnimation();

        view.animate()
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                }).start();

    }

    public void editProfilePicture(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                        getContext(), Manifest.permission.CAMERA)) {

                    Toast.makeText(getContext(),"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions((Activity) getContext(),
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQUEST_CODE);
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMERA_REQUEST_CODE);
                }

            }
        } else{
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_REQUEST_CODE);
            //Toast.makeText(getContext(),"PERMISSION NOT NEEDED",Toast.LENGTH_SHORT).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Message msg = new Message();
            msg.obj = imageBitmap;
            handler.sendMessage(msg);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }





}
