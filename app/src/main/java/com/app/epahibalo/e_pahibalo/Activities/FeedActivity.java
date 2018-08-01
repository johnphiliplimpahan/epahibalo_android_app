package com.app.epahibalo.e_pahibalo.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.epahibalo.e_pahibalo.PostIncidentFragment;
import com.app.epahibalo.e_pahibalo.R;
import com.app.epahibalo.e_pahibalo.Sections.AboutFragment;
import com.app.epahibalo.e_pahibalo.Sections.NewsFeedFragment;
import com.app.epahibalo.e_pahibalo.Sections.PinnedPostFragment;
import com.app.epahibalo.e_pahibalo.Sections.ProfileFragment;
import com.app.epahibalo.e_pahibalo.SectionsPageAdapter;
import com.app.epahibalo.e_pahibalo.SocketIntentService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private static String username;
    Intent intent;
    FloatingActionButton postIncidentButton;
    private static final int CAMERA_REQUEST_CODE = 101;
    public static Handler handler;
    String mCurrentPhotoPath,imageFileName;
    public static Uri incidentImageUri;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

//        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        //postIncidentButton = findViewById(R.id.postIncidentBtn);
        setupViewPager(viewPager);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        Bundle extras = getIntent().getExtras();


        username = extras.getString("username");

        intent = new Intent(FeedActivity.this,SocketIntentService.class);
        intent.putExtra("username",username);
        startService(intent);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_news_feed:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.action_profile:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.action_pinned_post:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.action_post_incident:
                        dispatchTakePictureIntent();
                        return true;
                    case R.id.action_about:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });

//        postIncidentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent();
//            }
//        });


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj instanceof String){
                    String uri = (String)msg.obj;
                    if (uri.length() > 0 ){
                        Bundle img = new Bundle();
                        img.putString("imageUri",uri);
                        img.putString("imageFileName",imageFileName);

                        PostIncidentFragment postFragment = new PostIncidentFragment();
                        postFragment.setArguments(img);


                        postFragment.show(getSupportFragmentManager(),"PostIncident");

                    }
                }
            }
        };

        //TabLayout tabLayout = (TabLayout)findViewById(R.id.action_tabs);
        //tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
//        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
//        adapter.initializeFragment(new NewsFeedFragment(),"News Feed");
//        adapter.initializeFragment(new ProfileFragment(),"Profile");
//        viewPager.setAdapter(adapter);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.initializeFragment(new NewsFeedFragment(),"News Feed");
        adapter.initializeFragment(new ProfileFragment(),"My Profile");
        adapter.initializeFragment(new PinnedPostFragment(),"Pinned Post");
        adapter.initializeFragment(new AboutFragment(),"About");
        viewPager.setAdapter(adapter);
    }

    public void alertLogout(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(FeedActivity.this);
        builderSingle.setTitle("Exit");
        builderSingle.setMessage("Do you want to log out?");
        builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent i = new Intent(FeedActivity.this,LoginActivity.class);
                startActivity(i);
                finish();


            }
        });

        builderSingle.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    @Override
    public void onBackPressed() {
        alertLogout();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "INCIDENT_IMAGE_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {


            if(incidentImageUri != null){

                Message msg = new Message();
                msg.setTarget(handler);
                msg.obj = incidentImageUri.toString();
                msg.sendToTarget();
            }else {
                Toast.makeText(getApplicationContext(),"Cannot grab result from Camera",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File incidentImageFile = null;

            try {
                incidentImageFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(incidentImageFile != null){
                 incidentImageUri = FileProvider.getUriForFile(this,
                        "com.app.epahibalo.e_pahibalo.fileprovider",incidentImageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,incidentImageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }

        }
    }

}
