package kailashsoni.com.MyBucketList.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kailashsoni.com.MyBucketList.Adapter.showBucketItemAdapter;
import kailashsoni.com.MyBucketList.Mode.BucketItem;

import kailashsoni.com.MyBucketList.R;
import kailashsoni.com.MyBucketList.SessionManger.SessionManager;
import kailashsoni.com.MyBucketList.Utility.Common;
import kailashsoni.com.MyBucketList.Utility.VolleySingleton;

// This class is for the navigating the bucket list
public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // define variables to be used
    private String TAG = "NavigationActivity",test = "test",userID = null;
    private Bitmap bitmap;
    private  Toolbar toolbar;
    private SessionManager sessionManager ;
    private ProgressDialog pDialog;
    private DrawerLayout drawerLayout;
    private ListView bucketItemList;
    private ArrayList<BucketItem> bucketArrayList = new ArrayList<>();
    private  BucketItem Unachieved_Item,Achieved_Item;
    public static int fabCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bucket);

        // initialize variables to be used
        drawerLayout = findViewById(R.id.drawer_layout);
        bucketItemList = findViewById(R.id.bucketitemListview);
        sessionManager = new SessionManager(this);
        pDialog = new ProgressDialog(this);
        userID = sessionManager.getUserDetails().get(SessionManager.KEY_UID);
        Log.e(TAG,"user id session "+userID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get all achieved and unachievd bucket items
        getBucketItems(userID,pDialog);

        //click event on single listitem
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabCount = 1;
                Log.e(TAG,"fab count is "+fabCount);
                startActivity(new Intent(NavigationActivity.this, AddBucketItem.class)
                .putExtra("addKEY","addbutton"));

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // get bucket item from string by using JSON array
    private void getBucketItems(final String userID, final ProgressDialog pDialog) {
        bucketArrayList.clear();
        pDialog.show();
        String URL = Common.ROOT_URL + Common.getAchieveUnBucketItemAPI;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(TAG,"response is "+response);
                pDialog.dismiss();
                try {
                    JSONArray jsonArrObj = new JSONArray(response);

                    for (int i = 0;i<jsonArrObj.length();i++){
                        JSONObject jsonObj = jsonArrObj.getJSONObject(i);
                        String status =  jsonObj.getString("status");
                        if(status.equalsIgnoreCase("true")){
                            String unachievedlist =  jsonObj.getString("unachived_result");
                            String achievedlist =  jsonObj.getString("acheved_result");
                            //Log.e(TAG,"unachieved is "+unachievedlist);
                            JSONArray UNjsonArray = new JSONArray(unachievedlist);
                            JSONArray ACjsonArray = new JSONArray(achievedlist);
                            Log.e(TAG,"json array size is "+UNjsonArray.length());
                            for(int j = 0;j<UNjsonArray.length();j++){
                                JSONObject jsonObject = UNjsonArray.getJSONObject(j);
                                String un_status = jsonObject.getString("status");
                                Log.e(TAG,"unachieved status is "+un_status);
                                if(un_status.equalsIgnoreCase("true")){
                                    String un_item_id = jsonObject.getString("bucket_item_id ");
                                    String un_item_image= jsonObject.getString("bucket_item_image");
                                    String un_item_title = jsonObject.getString("bucket_item_title");
                                    String un_item_description = jsonObject.getString("bucket_item_description");
                                    String un_item_category = jsonObject.getString("bucket_item_category ");
                                    String un_item_target_date = jsonObject.getString("bucket_item_target_date");
                                    String un_item_achieved_date  = jsonObject.getString("bucket_item_achieved_date");
                                    String un_item_location_name = jsonObject.getString("bucket_item_location_name");
                                    String un_item_latidude = jsonObject.getString("bucket_item_latitude");
                                    String un_item_longitude = jsonObject.getString("bucket_item_longitude");
                                    String un_item_inspirations = jsonObject.getString("publish_to_inspirations");

                                    //TODO add to model class
                                    Unachieved_Item = new BucketItem(un_item_id,un_item_title,un_item_target_date
                                            ,un_item_achieved_date,un_item_description,un_item_category,un_item_image,un_item_location_name
                                            ,un_item_latidude,un_item_longitude,un_item_inspirations,"false");
                                    Log.e(TAG,"===un_item_id is "+un_item_id);
                                    bucketArrayList.add(Unachieved_Item);
                                }
                                Log.e(TAG,"\tstatus is "+un_status);
                            }

                            for(int v = 0;v<ACjsonArray.length();v++){
                                JSONObject jsonObject = ACjsonArray.getJSONObject(v);
                                String ac_status = jsonObject.getString("status");
                                Log.e(TAG,"achieved status is "+ac_status);
                                if(ac_status.equalsIgnoreCase("true")){
                                    String ac_item_id = jsonObject.getString("bucket_item_id ");
                                    String ac_item_image= jsonObject.getString("bucket_item_image");
                                    String ac_item_title = jsonObject.getString("bucket_item_title");
                                    String ac_item_description = jsonObject.getString("bucket_item_description");
                                    String ac_item_category = jsonObject.getString("bucket_item_category ");
                                    String ac_item_target_date = jsonObject.getString("bucket_item_target_date");
                                    String ac_item_achieved_date  = jsonObject.getString("bucket_item_achieved_date");
                                    String ac_item_location_name = jsonObject.getString("bucket_item_location_name");
                                    String ac_item_latidude = jsonObject.getString("bucket_item_latitude");
                                    String ac_item_longitude = jsonObject.getString("bucket_item_longitude");
                                    String ac_item_inspirations = jsonObject.getString("publish_to_inspirations");

                                    //TODO add to model class
                                    Achieved_Item = new BucketItem(ac_item_id,ac_item_title,ac_item_target_date,ac_item_achieved_date
                                            ,ac_item_description,ac_item_category,ac_item_image,ac_item_location_name,ac_item_latidude
                                            ,ac_item_longitude,ac_item_inspirations,"true");

                                    bucketArrayList.add(Achieved_Item);
                                    Log.e(TAG,"===ac_item_id is "+ac_item_id);
                                }else  if(ac_status.equalsIgnoreCase("false")){
                                    Log.e(TAG,"NO data for achieved list !!!");
                                }
                            }
                        }else  if(status.equalsIgnoreCase("false")){
                            Snackbar.make(drawerLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
                        }
                    }
                    Log.e(TAG,"bucket array list size is "+bucketArrayList.size());

                    bucketItemList.setAdapter(new showBucketItemAdapter(NavigationActivity.this,bucketArrayList));
                    bucketItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                            Log.e(TAG,"item name is "+bucketArrayList.get(i));
                        }
                    });
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
                params.put("user_id",userID);
                params.put("target_date_old","true");
                return  params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(NavigationActivity.this).addToRequestQueue(request);
    }

    // when the back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            //TODO get all achieved and unachievd bucket items
            getBucketItems(userID,pDialog);
        } else if (id == R.id.nav_category) {
           // showCategoryFilter();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_logout)
        {
            sessionManager.logOutUser();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // gets the results of the on activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                bitmap = (Bitmap) data.getExtras().get("data");
                Toast.makeText(this, "data of image is " + data, Toast.LENGTH_SHORT).show();
                //toolbar.setBackground(bitmap);
//                BitMapToString(bitmap);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                toolbar.setBackgroundDrawable(bitmapDrawable);
                Log.e(TAG, "" + bitmap);
            } else if (requestCode == 3) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    BitMapToString(bitmap);
                    Log.e(TAG, String.valueOf(bitmap));
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    bitmapDrawable.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.MIRROR);
                    toolbar.setBackgroundDrawable(bitmapDrawable);
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
