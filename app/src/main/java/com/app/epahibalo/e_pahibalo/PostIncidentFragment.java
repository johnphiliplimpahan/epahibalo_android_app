package com.app.epahibalo.e_pahibalo;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.epahibalo.e_pahibalo.Helpers.GlideApp;
import com.app.epahibalo.e_pahibalo.Helpers.UserSession;
import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemModels.FeedItem;
import com.app.epahibalo.e_pahibalo.Model.Barangay;
import com.app.epahibalo.e_pahibalo.Sections.ProfileFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostIncidentFragment extends BottomSheetDialogFragment {

    ImageView postIncidentImage;
    MaterialEditText postIncidentHeadline,postIncidentStreetPurok;
    Spinner postIncidentDisasterType,postIncidentBarangayList;
    Button postIncidentButton;
    public static String imageUri,imageFileName;
    public static Handler handler;
    ProgressDialog dialog;
    Thread waw;
    JSONObject incidentData;
    protected final int INCIDENT_DATA_PREPARED = 1;
    protected static final int INCIDENT_SUBMITTED = 3;

    @Override
    public void setArguments(Bundle args) {
        imageUri = args.getString("imageUri");
        imageFileName = args.getString("imageFileName");
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        View view = inflater.inflate(R.layout.fragment_post_incident, container, false);
        //Log.d("HANDA: ","HEIGHT: "+view.getLayoutParams().height);
        //view.getLayoutParams().height = container.getLayoutParams().height;
        postIncidentImage = view.findViewById(R.id.disasterImage);
        postIncidentHeadline = view.findViewById(R.id.disasterHeadline);
        postIncidentStreetPurok = view.findViewById(R.id.disasterStreetPurok);
        postIncidentDisasterType = view.findViewById(R.id.disasterTypeSpinner);
        postIncidentBarangayList = view.findViewById(R.id.disasterBarangaySpinner);
        postIncidentButton = view.findViewById(R.id.disasterPostIncidentButton);
        incidentData = new JSONObject();


        GlideApp.with(getContext())
                .load(Uri.parse(imageUri))
                .dontTransform()
                .into(postIncidentImage);

        ArrayAdapter<String> disasterType = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item);
        disasterType.add("Earthquake");
        disasterType.add("Typhoon");
        disasterType.add("Flood");
        disasterType.add("Tornado");
        disasterType.add("Landslide");
        disasterType.add("Fire");
        disasterType.add("Tsunami");

        postIncidentDisasterType.setAdapter(disasterType);
        postIncidentBarangayList.setAdapter(new Barangay(getContext()).barangaySpinnerAdapter());



        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                    switch (msg.arg1){
                        case INCIDENT_DATA_PREPARED :
                            if(waw.isAlive()){
                                waw.stop();
                            }
                            SocketIntentService.uploadIncident(incidentData);
                            break;
                        case INCIDENT_SUBMITTED :
                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }

                            Message toProfile = new Message();
                            toProfile.arg1 = INCIDENT_SUBMITTED;
                            toProfile.setTarget(ProfileFragment.handler);
                            toProfile.obj = msg.obj;
                            toProfile.sendToTarget();

                            Toast.makeText(getContext(),"Incident Submitted: Please wait for validation",Toast.LENGTH_LONG).show();
                            dismiss();
                            break;
                        default:
                            Toast.makeText(getContext(),"Failed to prepare data",Toast.LENGTH_SHORT).show();
                            break;
                    }




            }
        };


        waw = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    InputStream is = getContext().getContentResolver().openInputStream(Uri.parse(imageUri));

                    byte[] imageByteArray = new byte[is.available()];
                    is.read(imageByteArray);
                    is.close();

                    String imageString = Base64.encodeToString(imageByteArray,Base64.DEFAULT);

                    incidentData.put("imageString",imageString);
                    incidentData.put("fileName",imageFileName);
                    incidentData.put("headLine",postIncidentHeadline.getText().toString());
                    incidentData.put("disasterType",postIncidentDisasterType.getSelectedItem().toString());
                    incidentData.put("barangay",postIncidentBarangayList.getSelectedItem().toString());
                    incidentData.put("streetpurok",postIncidentStreetPurok.getText().toString());
                    incidentData.put("username", UserSession.username);

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat time = new SimpleDateFormat("H:mm:ss");
                    String dateReported = date.format(c);
                    String timeReported = time.format(c);

                    incidentData.put("datereported",dateReported);
                    incidentData.put("timereported",timeReported);

                    Message msg = new Message();
                    msg.setTarget(handler);
                    msg.arg1 = INCIDENT_DATA_PREPARED;
                    msg.sendToTarget();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        postIncidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(postIncidentHeadline.getText().toString().length() < 10){
                    postIncidentHeadline.setError("Headline must have at least 10 characters");
                }else if (postIncidentStreetPurok.getText().toString().length() <= 0){
                    postIncidentStreetPurok.setError("Street or Purok must not be empty");
                }else{
                    dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Submitting Incident");
                    dialog.show();
                    waw.start();
                }

            }
        });



        return view;
    }


}
