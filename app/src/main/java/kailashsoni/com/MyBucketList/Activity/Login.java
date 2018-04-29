package kailashsoni.com.MyBucketList.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kailashsoni.com.MyBucketList.R;
import kailashsoni.com.MyBucketList.SessionManger.SessionManager;
import kailashsoni.com.MyBucketList.Utility.Common;
import kailashsoni.com.MyBucketList.Utility.NetConnection;
import kailashsoni.com.MyBucketList.Utility.VolleySingleton;


// This is to login the user for the app
public class Login extends AppCompatActivity implements View.OnClickListener{

    // define variables to be used
    private TextView SignupBtn;
    private  final String TAG = "LoginActivity";
    private EditText emailEdit,passwordEdit;
    private Button loginButton;
    private String string_email = null,string_password;
    private EditText email,password;
    //layout
    private ConstraintLayout layout_login;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        //the session and progress
        pDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        //find views
        initView();

    }

    private void initView() {
        //layout
        layout_login =findViewById(R.id.constrainLayout);
        //edit text
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.pwdET);
        //text view
        SignupBtn = findViewById(R.id.signupNowTxt);
        loginButton = findViewById(R.id.loginBtn);
        //layout
        layout_login = findViewById(R.id.constrainLayout);

        //set button and text to the onclick listener
        loginButton.setOnClickListener(this);
        SignupBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupNowTxt:
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
                break;
                case R.id.loginBtn:
                    string_email = email.getText().toString();
                    string_password = password.getText().toString();
                    if (NetConnection.IsNetPresent(Login.this)) {
                        if (!string_email.equals("")&& !string_password.equals(""))
                        {
                            if (!Patterns.EMAIL_ADDRESS.matcher(string_email).matches()) {
                                Snackbar.make(layout_login, "Invalid Email Field", Snackbar.LENGTH_SHORT).show();
                            }else if (string_password.equals("")) {
                                Snackbar.make(layout_login, "Password Field Required", Snackbar.LENGTH_SHORT).show();
                            } else {
                                    doLogin_JSON(string_email,string_password);
                            }
                        }else {
                            Snackbar.make(layout_login, "All Field Required", Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        Snackbar.make(layout_login, "Check Net connection !!!", Snackbar.LENGTH_SHORT).show();
                    }
                break;
        }
    }


    // The json method for login
    private void doLogin_JSON(final String string_email, final String string_password) {
        pDialog.setTitle("Login...");
        pDialog.show();
        String URL = Common.ROOT_URL + Common.getLoginAPI;


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"response is "+response);
                pDialog.dismiss();
                try {

                        JSONObject jsonObj = new JSONObject(response);

                        String status =  jsonObj.getString("status");
                        if(status.equalsIgnoreCase("true")){
                            String user_Id =  jsonObj.getString("id");
                            String user_firstname =  jsonObj.getString("firstname");
                            String user_lastname=  jsonObj.getString("lastname");
                            String user_email =  jsonObj.getString("email");
                            String user_mobileno =  jsonObj.getString("mobilenumber");

                            sessionManager.create_BucketLoginSession(user_Id,user_firstname,user_lastname,user_email,user_mobileno);
                            Intent intent1 = new Intent(Login.this,NavigationActivity.class);
                            startActivity(intent1);
                            finish();
                        }else if(status.equalsIgnoreCase("false")){
                            Snackbar.make(layout_login, "Check your username and password!!!", Snackbar.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "class Volly_Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",string_email);
                params.put("password",string_password);
                return  params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(Login.this).addToRequestQueue(request);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
