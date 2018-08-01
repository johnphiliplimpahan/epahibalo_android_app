package com.app.epahibalo.e_pahibalo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.app.epahibalo.e_pahibalo.Helpers.DateParser;
import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.app.epahibalo.e_pahibalo.Helpers.UserSession;
import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemModels.FeedItem;
import com.app.epahibalo.e_pahibalo.Sections.NewsFeedFragment;
import com.app.epahibalo.e_pahibalo.Sections.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SocketIntentService extends IntentService {

    public static Socket socket;

    public DateParser parse = new DateParser();
    FeedItem feedItem;
    Message msg;
    protected static final int INCIDENT_SUBMITTED = 3;

    public SocketIntentService() {
        super("SocketIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            try {
                IO.Options opts = new IO.Options();
                opts.forceNew = false;
                opts.reconnection = true;
                socket = IO.socket(URL.BASE_URL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


                String username = intent.getStringExtra("username");

                if(!socket.connected()){
                    socket.connect();
                }

                socket.emit("login",username);

                socket.on("my news feed",loadNewsFeed);
                socket.on("user profile",loadUserProfile);
                socket.on("my own posts",loadOwnPosts);



        }
    }



    private Emitter.Listener loadNewsFeed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject)args[0];

            try {
                JSONArray posts = new JSONArray(obj.getString("posts"));
                for(int x =0; x<posts.length();x++){
                    JSONObject post = posts.getJSONObject(x);

                    String incident_id = post.getString("incedent_report_id");
                    String profilePicPath = URL.IMAGE_PATH+post.getString("profilepic");
                    String fullname = post.getString("fullname");
                    String headLine = post.getString("incedent_report");
                    String location = post.getString("streetpurok").equals("") ? post.getString("brgy")+" "+post.getString("city") : post.getString("streetpurok")+", "+post.getString("brgy")+" "+post.getString("city");
                    String datePosted = parse.convertDate(post.getString("datereportreceived"))+" at "+parse.convertTime(post.getString("timereportreceived"));
                    String type = post.getString("disaster");
                    String incidentImage = URL.IMAGE_PATH+post.getString("image");


                    feedItem = new FeedItem(
                            incident_id,
                            profilePicPath,
                            fullname,
                            headLine,
                            datePosted,
                            type,
                            location,
                            incidentImage,
                            "");


                    msg = new Message();
                    msg.setTarget(NewsFeedFragment.handler);
                    msg.obj = feedItem;
                    msg.sendToTarget();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener loadUserProfile = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject)args[0];

            try {
                JSONArray profile = new JSONArray(obj.getString("profile"));
                JSONObject data = profile.getJSONObject(0);


                msg = new Message();
                msg.setTarget(ProfileFragment.handler);
                msg.obj = data;
                msg.sendToTarget();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    private Emitter.Listener loadOwnPosts = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject)args[0];

            try {
                JSONArray posts = new JSONArray(obj.getString("own_posts"));
                for(int x =0; x<posts.length();x++){
                    JSONObject post = posts.getJSONObject(x);

                    String incident_id = post.getString("incedent_report_id");
                    String profilePicPath = URL.IMAGE_PATH+post.getString("profilepic");
                    String fullname = post.getString("fullname");
                    String headLine = post.getString("incedent_report");
                    String location = post.getString("streetpurok").equals("") ? post.getString("brgy")+" "+post.getString("city") : post.getString("streetpurok")+", "+post.getString("brgy")+" "+post.getString("city");
                    String datePosted = parse.convertDate(post.getString("datereportreceived"))+" at "+parse.convertTime(post.getString("timereportreceived"));
                    String type = post.getString("disaster");
                    String incidentImage = URL.IMAGE_PATH+post.getString("image");
                    String validated = post.getString("validated");

                    Log.d("HAHAHA: ","DD "+validated);
                    feedItem = new FeedItem(
                            incident_id,
                            profilePicPath,
                            fullname,
                            headLine,
                            datePosted,
                            type,
                            location,
                            incidentImage,
                            validated);

                    UserSession.profilePicPath = profilePicPath;
                    UserSession.fullname = fullname;

                    msg = new Message();
                    msg.setTarget(ProfileFragment.handler);
                    msg.obj = feedItem;
                    msg.sendToTarget();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public static void checkMobileNumber(String mobilenum,String citizen_id){
        JSONObject zsmke = new JSONObject();
        try {
            zsmke.put("mobilenum",mobilenum);
            zsmke.put("citizen_id",citizen_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("checkMobile",zsmke);

        socket.on("checked mobile", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String obj = (String) args[0];
                if(obj.length() == 2){
                    Message msg = new Message();
                    msg.setTarget(ProfileFragment.handler);
                    msg.obj = "UPDATE_MOBILENUM_OK";
                    msg.sendToTarget();
                }else{
                    Message msg = new Message();
                    msg.setTarget(ProfileFragment.handler);
                    msg.obj = "UPDATE_MOBILENUM_NOT_OK";
                    msg.sendToTarget();
                }

        }
        });
    }


    public static void uploadIncident(JSONObject incidentData){
        socket.emit("post incident",incidentData);

        socket.on("incident submitted", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject wholeData = (JSONObject)args[0];
                DateParser parser = new DateParser();
                try {
                    int status = wholeData.getInt("status");
                    Log.d("HANDA: ","JAJ "+status);
                    switch (status){
                        case INCIDENT_SUBMITTED :
                            JSONArray theSubmittedPost = new JSONArray(wholeData.getString("the_incident"));
                            for (int x=0;x<theSubmittedPost.length();x++){
                                JSONObject thePostItself = theSubmittedPost.getJSONObject(x);
                                String incident_id = thePostItself.getString("incedent_report_id");
                                String profilePicPath = URL.IMAGE_PATH+thePostItself.getString("profilepic");
                                String fullname = thePostItself.getString("fullname");
                                String headLine = thePostItself.getString("incedent_report");
                                String location = thePostItself.getString("streetpurok").equals("") ? thePostItself.getString("brgy")+" "+thePostItself.getString("city") : thePostItself.getString("streetpurok")+", "+thePostItself.getString("brgy")+" "+thePostItself.getString("city");
                                String datePosted = parser.convertDate(thePostItself.getString("datereportreceived"))+" at "+parser.convertTime(thePostItself.getString("timereportreceived"));
                                String type = thePostItself.getString("disaster");
                                String incidentImage = URL.IMAGE_PATH+thePostItself.getString("image");
                                String validated = thePostItself.getString("validated");

                                FeedItem feedItem = new FeedItem(
                                        incident_id,
                                        profilePicPath,
                                        fullname,
                                        headLine,
                                        datePosted,
                                        type,
                                        location,
                                        incidentImage,
                                        validated);

                                Message msg = new Message();
                                msg.setTarget(PostIncidentFragment.handler);
                                msg.arg1 = INCIDENT_SUBMITTED;
                                msg.obj = feedItem;
                                msg.sendToTarget();
                            }
                            break;
                        default:
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }




}
