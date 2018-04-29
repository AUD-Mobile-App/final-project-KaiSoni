package kailashsoni.com.MyBucketList.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kailashsoni.com.MyBucketList.R;
import kailashsoni.com.MyBucketList.SessionManger.SessionManager;
import kailashsoni.com.MyBucketList.Utility.AppHelper;
import kailashsoni.com.MyBucketList.Utility.Common;
import kailashsoni.com.MyBucketList.Utility.NetConnection;
import kailashsoni.com.MyBucketList.Utility.VolleyMultipartRequest;
import kailashsoni.com.MyBucketList.Utility.VolleySingleton;


/**
 *  * Created by kailashsoni on 4/4/18
 */

// This is the class for the add bucket item activity
public class AddBucketItem extends FragmentActivity implements View.OnClickListener{

    // define variables to be used
    private Button submitButton, uploadBucketImgButton;
    private Toolbar addToolbar;
    private CheckBox publishCheck;
    private EditText titleET, descriptionET, targetDateET, achiverDateET, categoryET;
    public static EditText addressET;
    private String string_btnID= null,markerAddress= null, addbtin, string_title = null, string_desc = null, string_targetdate = null,
            string_achiveddate = null, string_category = null, string_address = null, TAG = "addBucketActivity", test = "test",  updateValue = null,
            string_latitude = null,string_longitude = null;
    private int count = 0;
    public static String string_itemID = null;
    private RelativeLayout relativeLayout;
    private Calendar calendar = null;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialog datePickerDialog;
    private ProgressDialog pDialog;
    public static double latitude, longitude;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private com.google.android.gms.maps.GoogleMap mMap;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ConstraintLayout layout_addItem;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private ImageView alert_image_title, alert_image_dec, alert_image_cate, alert_image_publish_check, alert_image_add;
    private CheckBox publishInspireCheck;
    private boolean checkedIS = false;
    private Bitmap bitmap = null;
    private boolean clicked = false;
    private SessionManager sessionManager;
    public static TextView lonTxt,latTxt;
    private    String dueDate,title, des;
    private TextView toolbarTitle,lonTxtIS,latTxtIS;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bucketitem_layout);
        sessionManager = new SessionManager(this);
        //google add marker

        this.calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        //find view
        initView();


        addToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initView() {
        //text view
        latTxt   = findViewById(R.id.latTxtvalue);
        lonTxt= findViewById(R.id.lonTxtvalue);

        toolbarTitle= findViewById(R.id.toolbarTitleTxt);
        //Check box
        publishInspireCheck = findViewById(R.id.publishCheckbox);
        //alert image
        alert_image_title = findViewById(R.id.alertTitleIMG);
        alert_image_dec = findViewById(R.id.alertDesIMG);
        alert_image_cate = findViewById(R.id.alertCatIMG);
        alert_image_publish_check = findViewById(R.id.alertCheckIMG);
        alert_image_add = findViewById(R.id.alertAddIMG);
        //layout
        layout_addItem = findViewById(R.id.constrainLayoutAddBucket);
        //progress dialog
        pDialog = new ProgressDialog(this);
        //layout
        relativeLayout = findViewById(R.id.relativeLyout);
        //button
        uploadBucketImgButton = findViewById(R.id.addBucketImgBtn);
        submitButton = findViewById(R.id.submitBtn);
        addToolbar = findViewById(R.id.addItemToolbar);
        //edit text
        titleET = findViewById(R.id.firstET);
        descriptionET = findViewById(R.id.decET);
        targetDateET = findViewById(R.id.targetDateSpinner);
        achiverDateET = findViewById(R.id.achivedDateSpinner);
        addressET = findViewById(R.id.locationET);
        categoryET = findViewById(R.id.catET);
        //check box
        publishCheck = findViewById(R.id.publishCheckbox);

        //add click event on view
        targetDateET.setOnClickListener(this);
        achiverDateET.setOnClickListener(this);
        addressET.setOnClickListener(this);
        categoryET.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        uploadBucketImgButton.setOnClickListener(this);
        //set value from intent
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }else {
             title =  bundle.getString("itemtitle_KEY");
             des =  bundle.getString("itemdesc_KEY");
             String cat = bundle.getString("itemcat_KEY");
            String imgURl = bundle.getString("imageKEY");
            updateValue = bundle.getString("updateKEY");
            string_itemID = bundle.getString("itemid_KEY");
            string_latitude = bundle.getString("itemlat_KEY");
            string_longitude = bundle.getString("itemlon_KEY");
            string_targetdate  = bundle.getString("itemtarget_KEY");
            string_address  = bundle.getString("itemlocation_KEY");
            Log.e(TAG,"--yyyyyyyy-"+string_longitude+"-----"+string_latitude+"\t"+string_itemID);

            titleET.setText(title);
            descriptionET.setText(des);
            categoryET.setText(cat);
            lonTxt.setText(string_longitude);
            latTxt.setText(string_latitude);
            targetDateET.setText(string_targetdate);
            addressET.setText( string_address );

            //get value from show google map activity intent


        }
        String conditionButton   = bundle.getString("buttonKEY");
        if(conditionButton != null){
            latitude   = bundle.getDouble("markLateKEY");
            longitude = bundle.getDouble("markLongKEY");
            markerAddress   = bundle.getString("markAddressKEY");
            title   = bundle.getString("intTitleKEY");
            des = bundle.getString("intDescriptionKEY");
            dueDate   = bundle.getString("intDateKEY");
            string_itemID   = bundle.getString("intItemIDKEY");
            string_btnID   = bundle.getString("intBtnKEY");
            titleET.setText(title);
            descriptionET.setText(des);
            targetDateET.setText(dueDate);
            addressET.setText( markerAddress );
            lonTxt.setText(String.valueOf( longitude));
            latTxt.setText(String.valueOf( latitude ));
            Log.e(TAG,"marker latlong "+latitude+"---"+longitude+" address\t "
                    +markerAddress+"\t"+string_itemID+"\t"+string_btnID);
        }else {
            Log.e(TAG, "####nothing in google button###");
        }
        addbtin = bundle.getString("addKEY");
        Log.e(TAG,"add btn clicked "+addbtin);


            if((updateValue != null) || (addbtin == null && string_btnID == null)){
                submitButton.setText("Update");
                titleET.setHint("Edit Bucket List item");
                descriptionET.setHint("Edit Bucket List Description");
                toolbarTitle.setText("Edit Bucket List item");
            }
        }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddBucketItem.this,NavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // when an item is clicked upon
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.targetDateSpinner:
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                targetDateET.setText(MONTHS[view.getMonth()] + " " + dayOfMonth + ", " + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.achivedDateSpinner:
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                achiverDateET.setText(MONTHS[view.getMonth()] + " " + dayOfMonth + ", " + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.locationET:
               Intent intent = new Intent(AddBucketItem.this,GoogleMap.class);
               intent.putExtra("titleKEY",titleET.getText().toString());
               intent.putExtra("descriptionKEY", descriptionET.getText().toString());
               intent.putExtra("dueDateKEY",targetDateET.getText().toString());
               intent.putExtra("itemIDKEY",string_itemID);
               intent.putExtra("addressKEY",string_address);
               intent.putExtra("latKEY",string_latitude);
               intent.putExtra("longKEY",string_longitude);
               intent.putExtra("btnKEY",addbtin);

               startActivity(intent);
                break;
            case R.id.addBucketImgBtn:
                addBucketImage();
                break;
            case R.id.submitBtn:
                Log.e(TAG,"bitn is "+submitButton.getText().toString()+"\t string "+updateValue);
                if(updateValue == null && submitButton.getText().toString().equals("Submit")){
                    string_title = titleET.getText().toString();
                    string_desc = descriptionET.getText().toString();
                    string_targetdate = targetDateET.getText().toString();
                    string_category = categoryET.getText().toString();
                    string_address = addressET.getText().toString();
                    checkedIS = publishInspireCheck.isChecked();

                    Log.e(TAG, "null and not ture==> title is \t" + string_title + "\tdesc is" + string_desc +
                            "\ttarget date " + string_targetdate + "\tachived" + string_achiveddate +
                            "\tcategory " + string_category + "\taddress " + string_address + "\tcheck is " + String.valueOf(checkedIS)+"\tclicked "+clicked+"\tbitmap "+bitmap);

                    if (NetConnection.IsNetPresent(AddBucketItem.this)) {
                        if (!string_title.equals("") && !string_desc.equals("") && !string_targetdate.equals("")
                               ) {
                            if (string_title.equals("")) {
                                Snackbar.make(layout_addItem, "Title Required !!!", Snackbar.LENGTH_SHORT).show();
                            } else if (string_targetdate.equals("")) {
                                Snackbar.make(layout_addItem, "Target Date Required !!!", Snackbar.LENGTH_SHORT).show();
                            }  else if (string_desc.equals("")) {
                                    Snackbar.make(layout_addItem, "Description Required !!!", Snackbar.LENGTH_SHORT).show();
                            }else if (string_address.equals("")) {
                                Snackbar.make(layout_addItem, "Address Required !!!", Snackbar.LENGTH_SHORT).show();
                            }else {
                            Intent intent1 = new Intent(AddBucketItem.this, NavigationActivity.class);
                            startActivity(intent1);
                            finish();
                                //JSON code
                            createBucketItems(string_title,string_desc,string_targetdate,string_achiveddate,string_category,string_address);


                            }
                        } else {
                        alert_image_title.setVisibility(View.VISIBLE);
                        alert_image_dec.setVisibility(View.VISIBLE);
                        alert_image_cate.setVisibility(View.VISIBLE);
                        alert_image_publish_check.setVisibility(View.VISIBLE);
                        alert_image_add.setVisibility(View.VISIBLE);
                            Snackbar.make(layout_addItem, "All Field Required !!!", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(layout_addItem, "Check Net connection !!!", Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                }else  if((updateValue == null || updateValue != null)&& submitButton.getText().toString().equals("Update")){
                    string_title = titleET.getText().toString();
                    string_desc = descriptionET.getText().toString();
                    string_targetdate = targetDateET.getText().toString();

                    string_address = addressET.getText().toString();


                    Log.e(TAG, "title is \t" + string_title +"\ttarget date " + string_targetdate +
                            "\taddress " + string_address+"\t latlan is "+latitude+"\tlong " +longitude+"\n"+string_longitude+"\t"+string_latitude);
                    if (NetConnection.IsNetPresent(AddBucketItem.this)) {
                        if (!string_title.equals("") && !string_desc.equals("") && !string_targetdate.equals("")
                               && !string_address.equals("")) {
                            if (string_title.equals("")) {
                                // alert_image_title.setVisibility(View.GONE);
                                Snackbar.make(layout_addItem, "Title Required !!!", Snackbar.LENGTH_SHORT).show();
                            } else if (string_desc.equals("")) {
                                //alert_image_dec.setVisibility(View.GONE);
                                Snackbar.make(layout_addItem, "Description Required !!!", Snackbar.LENGTH_SHORT).show();
                            }else if (string_address.equals("")) {
                                //alert_image_dec.setVisibility(View.GONE);
                                Snackbar.make(layout_addItem, "Address Required !!!", Snackbar.LENGTH_SHORT).show();
                            } else if (string_targetdate.equals("")) {
                                //alert_image_cate.setVisibility(View.GONE);
                                Snackbar.make(layout_addItem, "Target Date Required !!!", Snackbar.LENGTH_SHORT).show();
                            } else if (string_address.equals("")) {
                                //alert_image_add.setVisibility(View.GONE);
                                Snackbar.make(layout_addItem, "Address Required !!!", Snackbar.LENGTH_SHORT).show();
                            }else {
                            Intent intent2 = new Intent(AddBucketItem.this, NavigationActivity.class);
                            startActivity(intent2);
                            finish();
                                //JSON code
                               upDate_BucketItems(string_title,string_desc,string_targetdate,string_achiveddate,string_category,string_address);

                            }
                        } else {
                        alert_image_title.setVisibility(View.VISIBLE);
                        alert_image_dec.setVisibility(View.VISIBLE);
                        alert_image_cate.setVisibility(View.VISIBLE);
                        alert_image_publish_check.setVisibility(View.VISIBLE);
                        alert_image_add.setVisibility(View.VISIBLE);
                            Snackbar.make(layout_addItem, "All Field Required !!!", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(layout_addItem, "Check Net connection !!!", Snackbar.LENGTH_SHORT).show();
                    }
                }
        }
    }

    // updates the bucket list items with JSON
    private void upDate_BucketItems(final String string_title, final String string_desc, final String string_targetdate,
                                    final String string_achiveddate, final String string_category, final String string_address) {
        pDialog.setTitle("Updating Bucket Item...");
        pDialog.show();
        String URL = Common.ROOT_URL + Common.updateBucketItemAPI;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                pDialog.dismiss();
                String resultResponse = new String(response.data);
                Log.e(TAG,"add item response is "+resultResponse);
                try {
                    JSONObject jsonObj = new JSONObject(resultResponse);
                    String status = jsonObj.getString("status");
                    Log.e(TAG,"status is "+status);
                    if (status.equalsIgnoreCase("true")) {
                        String item_ID =  jsonObj.getString("id");
                        String item_title =  jsonObj.getString("bucket_item_title");
                        String item_des=  jsonObj.getString("bucket_item_description");
                        String item_category =  jsonObj.getString("bucket_item_category");
                        String item_targetDate =  jsonObj.getString("bucket_item_target_date");
                        String item_achievedDate =  jsonObj.getString("bucket_item_achieved_date");
                        String item_address =  jsonObj.getString("bucket_item_location_name");
                        String item_latitude =  jsonObj.getString("bucket_item_latitude");
                        String item_longitude =  jsonObj.getString("bucket_item_longitude");
                        String item_publishCheck =  jsonObj.getString("publish_to_inspirations");
                        String item_image =  jsonObj.getString("bucket_item_image");


                        titleET.setText(getResources().getString(R.string.titleTxt));
                        descriptionET.setText(getResources().getString(R.string.decriptionTxt));
                        targetDateET.setText(getResources().getString(R.string.targetdateETxt));
                        achiverDateET.setText(getResources().getString(R.string.achiveddateTxt));
                        categoryET.setText(getResources().getString(R.string.catTxt));
                        addressET.setText(getResources().getString(R.string.address));
                        publishInspireCheck.setChecked(false);
                        clicked = false;
                        bitmap = null;

                        Intent intent = new Intent(AddBucketItem.this,NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Log.e(TAG,"after update title is "+item_title);
                        Log.e(TAG,"after update description is "+item_des);
                       onBackPressed();

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                params.put("bucket_item_id", string_itemID);
                params.put("bucket_item_title", string_title);
                params.put("bucket_item_description", string_desc);
                params.put("bucket_item_category", " ");
                params.put("bucket_item_target_date", string_targetdate);
                params.put("bucket_item_achieved_date", "0/0/0");
                params.put("bucket_item_location_name", string_address);
                if(string_latitude != null && string_longitude!= null){
                    params.put("bucket_item_latitude", String.valueOf(string_latitude));
                    params.put("bucket_item_longitude", String.valueOf(string_longitude));
                }else {
                    params.put("bucket_item_latitude", String.valueOf(latitude));
                    params.put("bucket_item_longitude", String.valueOf(longitude));
                }
                params.put("publish_to_inspirations", "");
                Log.e(TAG,"param is    "+params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                //if (bitmap != null) {
                String imageName = "IMG_" + System.currentTimeMillis() + ".png";
                //Bitmap bitmap = EventTitleActivity.bitmap;
                if(bitmap !=null && clicked != false ){
                    Log.e("bitmap", "" + bitmap);
                    Log.e("imageName", "" + imageName);
                    params.put("bucket_item_image", new DataPart(imageName, AppHelper.getFileDataFromDrawable(getBaseContext(), bitmap), "image/.png"));
                    Log.e(TAG, "" + params);
                }

                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(AddBucketItem.this).addToRequestQueue(request);
    }

    // create the bucket items to be used using JSON
    private void createBucketItems(final String string_title, final String string_desc, final String string_targetdate, final String string_achiveddate, final String string_category, final String string_address) {
        pDialog.setTitle("Adding Bucket Item...");
        pDialog.show();
        String URL = Common.ROOT_URL + Common.createBucketItemsAPI;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                pDialog.dismiss();
                String resultResponse = new String(response.data);
                Log.e(TAG,"add item response is "+resultResponse);
                try {
                    JSONObject jsonObj = new JSONObject(resultResponse);
                    String status = jsonObj.getString("status");
                    Log.e(TAG,"status is "+status);
                    if (status.equalsIgnoreCase("true")) {
                        String item_ID =  jsonObj.getString("id");
                        String item_title =  jsonObj.getString("bucket_item_title");
                        String item_des=  jsonObj.getString("bucket_item_description");
                        String item_category =  jsonObj.getString("bucket_item_category");
                        String item_targetDate =  jsonObj.getString("bucket_item_target_date");
                        String item_achievedDate =  jsonObj.getString("bucket_item_achieved_date");
                        String item_address =  jsonObj.getString("bucket_item_location_name");
                        String item_latitude =  jsonObj.getString("bucket_item_latitude");
                        String item_longitude =  jsonObj.getString("bucket_item_longitude");
                        String item_publishCheck =  jsonObj.getString("publish_to_inspirations");
                        String item_image =  jsonObj.getString("bucket_item_image");

                        Intent intent = new Intent(AddBucketItem.this,NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        titleET.setText(getResources().getString(R.string.titleTxt));
                        descriptionET.setText(getResources().getString(R.string.decriptionTxt));
                        targetDateET.setText(getResources().getString(R.string.targetdateETxt));
                        achiverDateET.setText(getResources().getString(R.string.achiveddateTxt));
                        categoryET.setText(getResources().getString(R.string.catTxt));
                        addressET.setText(getResources().getString(R.string.address));
                        publishInspireCheck.setChecked(false);
                        clicked = false;
                        bitmap = null;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    } else {
                        pDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Snackbar.make(layout_addItem, "Please use valid username and password", Snackbar.LENGTH_SHORT).show();
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
                params.put("bucket_item_title", string_title);
                params.put("bucket_item_description", string_desc);
                params.put("bucket_item_category", string_category);
                params.put("bucket_item_target_date", string_targetdate);
                params.put("bucket_item_achieved_date", "0/0/0");
                params.put("bucket_item_location_name", string_address);
                params.put("bucket_item_latitude", String.valueOf(GoogleMap.latitude));
                params.put("bucket_item_longitude", String.valueOf(GoogleMap.longitude));
                params.put("publish_to_inspirations", "");
                params.put("user_id", sessionManager.getUserDetails().get(SessionManager.KEY_UID));
                Log.e(TAG,"param is "+params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                String imageName =  "IMG_" + System.currentTimeMillis() + ".png";
                Log.e(TAG,"bitm api is is ===="+bitmap);
                if (bitmap != null) {
                     imageName = "IMG_" + System.currentTimeMillis() + ".png";
                    //Bitmap bitmap = EventTitleActivity.bitmap;
                    Log.e("bitmap", "" + bitmap);
                    Log.e("imageName", "" + imageName);
                    params.put("bucket_item_image", new DataPart(imageName, AppHelper.getFileDataFromDrawable(getBaseContext(), bitmap), "image/.png"));
                    Log.e(TAG, "" + params);
               }
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(AddBucketItem.this).addToRequestQueue(request);
    }

    // when an item is changed
    private void addBucketImage() {
        clicked = true;
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddBucketItem.this);
        builder.setTitle("Add Cover Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent photo = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(photo, 2);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    // Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    // Always show the chooser (if there are multiple options available)
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }


    private void getAddress() {
        try {
            pDialog.show();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(AddBucketItem.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        pDialog.dismiss();
    }

    // gets the activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                bitmap = (Bitmap) data.getExtras().get("data");
                //Toast.makeText(this, "data of image is " + data, Toast.LENGTH_SHORT).show();
                //toolbar.setBackground(bitmap);
                // BitMapToString(bitmap);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                Log.e(TAG, "request code 2 "+ bitmap);
            } else if (requestCode == 3) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    BitMapToString(bitmap);
                    Log.e(TAG, "request code 3 "+String.valueOf(bitmap));
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    bitmapDrawable.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.MIRROR);
                    //profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
