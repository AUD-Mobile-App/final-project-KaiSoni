package kailashsoni.com.MyBucketList.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kailashsoni.com.MyBucketList.R;
import kailashsoni.com.MyBucketList.Utility.Common;
import kailashsoni.com.MyBucketList.Utility.NetConnection;
import kailashsoni.com.MyBucketList.Utility.VolleySingleton;

// This is the sign up activity class
public class SignUp extends AppCompatActivity implements View.OnClickListener {
    // define variables to be used
    private Toolbar signUpToolbar;
    private TextView alreadyLoginBtn, googleBtn;
    private Button signUpButton;
    private LoginButton facebookLoginbtn;
    private EditText email_et, password_et, first_et, last_et, confirm_pwd_et, mobile_et;
    private String string_email = null, string_password = null, string_firstName = null, string_lastName = null, string_confirmPwd = null, string_mobileNO, TAG = "SignUp activity";
    private ScrollView scrollView;
    private ProgressDialog pDialog;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        //The find view
        initView();
        //The click event on toolbar
        signUpToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //The set view to onclick
        alreadyLoginBtn.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void initView() {
        //layout
        scrollView = findViewById(R.id.scorllviewSignUP);
        constraintLayout = findViewById(R.id.constrainLayoutSignup);
        //toolbar
        signUpToolbar = findViewById(R.id.signToolbar);
        //button
        alreadyLoginBtn = findViewById(R.id.LoginNowTxt);
        signUpButton = findViewById(R.id.signInBtn);
        //edit text
        email_et = findViewById(R.id.emailET);
        password_et = findViewById(R.id.pwdET);
        first_et = findViewById(R.id.firstET);
        last_et = findViewById(R.id.lNameET);
        confirm_pwd_et = findViewById(R.id.confirmET);
        mobile_et = findViewById(R.id.mobileET);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // When an item is clicked it will store item in string
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn:
                // get all view
                string_email = email_et.getText().toString();
                string_password = password_et.getText().toString();
                string_firstName = first_et.getText().toString();
                string_lastName = last_et.getText().toString();
                string_confirmPwd = confirm_pwd_et.getText().toString();
                string_mobileNO = mobile_et.getText().toString();

                pDialog = new ProgressDialog(this);
                if (NetConnection.IsNetPresent(SignUp.this)) {
                    if (!string_firstName.equals("") && !string_lastName.equals("") && !string_email.equals("")
                            && !string_password.equals("") && !string_confirmPwd.equals("") && !string_mobileNO.equals("")
                            )  // && (!clicked == false)
                    {
                        if (string_firstName.equals("")) {
                            Snackbar.make(scrollView, "First Name Field Required", Snackbar.LENGTH_SHORT).show();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(string_email).matches()) {
                            Snackbar.make(scrollView, "Invalid Email Field", Snackbar.LENGTH_SHORT).show();
                        } else if (string_lastName.equals("")) {
                            Snackbar.make(scrollView, "Last Name Field Required", Snackbar.LENGTH_SHORT).show();
                        } else if (string_password.equals("")) {
                            Snackbar.make(scrollView, "Password Field Required", Snackbar.LENGTH_SHORT).show();
                        } else if (string_password.length() <= 5) {
                            Snackbar.make(scrollView, "Password Should be Greater than Six digit.", Snackbar.LENGTH_SHORT).show();
                        } else if (string_confirmPwd.equals("")) {
                            Snackbar.make(scrollView, "Password Field Required", Snackbar.LENGTH_SHORT).show();
                        } else if (!string_confirmPwd.equals(string_password)) {
                            Snackbar.make(scrollView, "Password Not Match.", Snackbar.LENGTH_SHORT).show();
                        } else if (string_mobileNO.length() < 10) {
                            Snackbar.make(scrollView, "Mobile Number can't be less then 10 digit", Snackbar.LENGTH_SHORT).show();
                        } else if (string_firstName.equals("")) {
                            Snackbar.make(scrollView, "It is required", Snackbar.LENGTH_SHORT).show();
                        } else {
                            pDialog = new ProgressDialog(SignUp.this);
                            //The registration
                            getSignIn();
                        }
                    } else {
                        Snackbar.make(scrollView, "All Field Required !!!", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(scrollView, "Check Net connection !!!", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.LoginNowTxt:
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    // when the user signs in, and this uses JSON
    private void getSignIn() {
        pDialog.setTitle("SignIn...");
        pDialog.show();
        String URL = Common.ROOT_URL + Common.setSignInAPI;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response is " + response);
                pDialog.dismiss();
                try {

                    JSONObject jsonObj = new JSONObject(response);

                    String status = jsonObj.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        String user_id = jsonObj.getString("id");
                        String user_firstname = jsonObj.getString("firstname");
                        String user_lastname = jsonObj.getString("lastname");
                        String user_mobileno = jsonObj.getString("mobilenumber");
                        String user_email = jsonObj.getString("email");

                        Intent intent1 = new Intent(SignUp.this, Login.class);
                        startActivity(intent1);
                        finish();
                    } else if (status.equalsIgnoreCase("false")) {
                        Snackbar.make(scrollView, "Something is getting wrong !!!", Snackbar.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", string_firstName);
                params.put("lastname", string_lastName);
                params.put("mobilenumber", string_mobileNO);
                params.put("email", string_email);
                params.put("password", string_password);
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(SignUp.this).addToRequestQueue(request);
    }


}
