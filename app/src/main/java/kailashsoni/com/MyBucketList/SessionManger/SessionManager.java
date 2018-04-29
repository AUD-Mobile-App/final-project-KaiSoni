package kailashsoni.com.MyBucketList.SessionManger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import kailashsoni.com.MyBucketList.Activity.Login;


/**
 * Created by kailashsoni on 13/4/18
 */

// This class is for the sessions for the bucket list app
public class SessionManager {

    // define variables to be used
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;
    private Context context;

    public static final String PREF_NAME="DriverApp";
    private static final String IS_LOGIN = "Is_login";
    public static final String KEY_UID = "user_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USER_FIRST_NAME = "USER_type";
    public static final String KEY_USER_LAST_NAME = "USER_name";
    public static final String KEY_MOBILE = "USER_name";

    // This is the session constructor
    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();

    }

    // This uses shared preferences for for the log in session
    public void create_BucketLoginSession(String id, String firstname,String lastname,String email,String Mobile)
    {
        //init is_login
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_UID,id);
        editor.putString(KEY_USER_FIRST_NAME,firstname);
        editor.putString(KEY_USER_LAST_NAME,lastname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_MOBILE,Mobile);

        editor.commit();
    }

    // Hasp map to put to the information in a string
    public HashMap<String,String> getUserDetails(){
        HashMap<String,String>user = new HashMap<String, String>();
        user.put(KEY_UID,pref.getString(KEY_UID,null));
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        user.put(KEY_USER_FIRST_NAME,pref.getString(KEY_USER_FIRST_NAME,null));
        user.put(KEY_USER_LAST_NAME,pref.getString(KEY_USER_LAST_NAME,null));
        user.put(KEY_MOBILE,pref.getString(KEY_MOBILE,null));

        return  user;
    }

    //get TeacherAllInfo
    //remaining for certificate
    public boolean checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add ic_new Flag to start ic_new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            context.startActivity(i);
            return true;
        }
        return false;
    }

    // Logouts user
    public void logOutUser(){
        // Clearing all data from Shared Preferences
        Intent intent = new Intent(context,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
