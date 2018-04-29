package kailashsoni.com.MyBucketList.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kailashsoni.com.MyBucketList.Activity.AddBucketItem;
import kailashsoni.com.MyBucketList.Mode.BucketItem;
import kailashsoni.com.MyBucketList.R;
import kailashsoni.com.MyBucketList.SessionManger.SessionManager;
import kailashsoni.com.MyBucketList.Utility.Common;
import kailashsoni.com.MyBucketList.Utility.VolleySingleton;

/**
 * Created by kailashsoni on 10/4/18
 */

// This is the updater to be used for the bucketlist items
public class BucketItemAdapter extends BaseAdapter {
    public  ArrayList<Object> bucketitemArray;
    private LayoutInflater inflater;
    private static final int TYPE_UNACHIEVED = 0;
    private static final int TYPE_ACHIEVED = 1;
    private Context context;
    private int count = 0;
    private BucketItem bucket ;
    public static String titleString = null;
    private ProgressDialog pDialog;
    private String TAG = "Bucketitem Adapter";
    private SessionManager sessionManager;

    // Adapter with the array list defined with the session manager
    public BucketItemAdapter(Context applicationContext, ArrayList<Object> bucketArrayList) {
        this.context = applicationContext;
        this.bucketitemArray = bucketArrayList;
        pDialog = new ProgressDialog(context);
        sessionManager = new SessionManager(context);
    }
    // gets the count for bucketitemsarray
    @Override
    public int getCount() {
        Log.e("size ","bucketitemArray is "+bucketitemArray.size());
        return bucketitemArray.size();
    }

    @Override
    public Object getItem(int position) {
        return bucketitemArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        // UNACHIEVED and ACHIEVED
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof BucketItem) {
            return TYPE_UNACHIEVED;
        }
        return TYPE_ACHIEVED;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_UNACHIEVED);
    }
    // gets the view of the items required by using switch cases for the different items on the bucketlist
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int type = getItemViewType(position);
        Log.e("type of postion is ","\t postion "+type);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            switch (type) {
                case TYPE_UNACHIEVED:
                    convertView = inflater.inflate(R.layout.single_bucketitem_cardview_layout, null);
                    break;
                case TYPE_ACHIEVED:
                    convertView = inflater.inflate(R.layout.listview_row_header,null);
                    break;
            }
        }

        switch (type) {
            case TYPE_UNACHIEVED:
                final BucketItem bucket = (BucketItem) getItem(position);
                final CardView item_cardview = (CardView) convertView.findViewById(R.id.SingleCardView);
                TextView item_title = (TextView)convertView.findViewById(R.id.itemTitle);
                TextView item_subtitle = (TextView)convertView.findViewById(R.id.item_sub_Title);
                TextView target_date = (TextView)convertView.findViewById(R.id.item_target_date);
                ImageView item_imgs = (ImageView) convertView.findViewById(R.id.itemImg);

                TextView latngTxt = (TextView) convertView.findViewById(R.id.latTxtvalueSingleitem);
                TextView longiTxt = (TextView) convertView.findViewById(R.id.lonTxtvalueSingleitem);

                final CheckBox   itemCheck = (CheckBox) convertView.findViewById(R.id.achivedCheckbox);
                item_title.setText(bucket.getItme_title());
                item_subtitle.setText(bucket.getItme_title());
                target_date.setText(bucket.getItem_target_date());
                String itemImg_URL = bucket.getItem_image();

                latngTxt.setText(bucket.getItem_latitude());
                longiTxt.setText(bucket.getItem_logitude());
                final String statusValue = bucket.getStatusIS();


                Log.e("----","===="+statusValue);

                Log.e(TAG,"array obj is "+bucketitemArray.toString());
                if(statusValue.equalsIgnoreCase("achived")){
                    itemCheck.setVisibility(View.VISIBLE);
                    itemCheck.setEnabled(false);
                    itemCheck.setChecked(true);
                    notifyDataSetChanged();
                }else  if(statusValue.isEmpty() && count == 0 ){

                        itemCheck.setVisibility(View.VISIBLE);
                        itemCheck.setEnabled(true);
                        itemCheck.setChecked(false);
                        notifyDataSetChanged();

                }

                Log.e("title , date","\t "+bucket.getItem_latitude()+"\t"+bucket.getItem_logitude());
                Picasso.with(context).load(Common.ROOT_URL+itemImg_URL).into(item_imgs);

                item_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("type of postion is ","\t postion "+type);
                        Log.e("title , date","\t "+bucket.getItem_latitude()+"\t"+bucket.getItem_logitude());
                        Log.e("title , date","\t "+bucket.getItem_category()+"\t"+bucket.getItem_location());
                        Intent itemIntent = new Intent(context, AddBucketItem.class);
                        itemIntent.putExtra("itemid_KEY",bucket.getItem_id());
                        itemIntent.putExtra("itemimg_KEY",bucket.getItem_image());
                        itemIntent.putExtra("itemtitle_KEY",bucket.getItme_title());
                        itemIntent.putExtra("itemdesc_KEY",bucket.getItem_description());
                        itemIntent.putExtra("itemcat_KEY",bucket.getItem_category());
                        itemIntent.putExtra("itemtarget_KEY",bucket.getItem_target_date());
                        itemIntent.putExtra("itemachieved_KEY",bucket.getItem_achieved_date());
                        itemIntent.putExtra("itemlocation_KEY",bucket.getItem_location());
                        itemIntent.putExtra("itemlat_KEY",bucket.getItem_latitude());
                        itemIntent.putExtra("itemlon_KEY",bucket.getItem_logitude());
                        itemIntent.putExtra("itemInspire_KEY",bucket.getItem_publish_inspiration());
                        itemIntent.putExtra("status_KEY",bucket.getStatusIS());
                        itemIntent.putExtra("updateKEY", "true");
                        itemIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(itemIntent);
                    }
                });

                // when an item is checked
                itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(count == 0){

                        }
                        if(statusValue.isEmpty() && statusValue.equalsIgnoreCase("")){
                            if(isChecked == true){
                                count = count + 1;
                                String achi = "achived";
                               // setAchievedItem(achi);

                                Log.e(TAG,"achieved method clicked and button is"+isChecked);
                                //Toast.makeText(context, "check box clicked ", Toast.LENGTH_SHORT).show();
                            }else if(isChecked == false){
                                Log.e(TAG,"achieved method not-clicked and button is"+isChecked);

                            }
                        }

                    }

                });
                break;
            case TYPE_ACHIEVED:
                TextView title = (TextView)convertView.findViewById(R.id.headerTitle);
                titleString = (String)getItem(position);
                Log.e("achieved is","==== title is "+titleString);
                title.setText(titleString);
                title.setTextColor(context.getResources().getColor(R.color.firstColor));
                break;
        }

        return convertView;

    }

    // when the items are completed using the update items and using JSON array
    private void setAchievedItem(final String statusIS) {
        pDialog.show();
        String URL = Common.ROOT_URL + Common.updateBucketStatusAPI;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"response is "+response);
                pDialog.dismiss();

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String status =  jsonObj.getString("status");
                    if(status.equalsIgnoreCase("true")){
                        String Bucket_itemid =  jsonObj.getString("bucket_item_id");
                        String Bucket_itemStatus =  jsonObj.getString("input_status");
                       bucketitemArray.remove(Bucket_itemid);
                       notifyDataSetChanged();
                        Log.e(TAG,"id\t"+Bucket_itemid+"\t\n"+Bucket_itemStatus);
                    }else  if(status.equalsIgnoreCase("false")){
                        Log.e(TAG,"No data found !!!");
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
                params.put("bucket_item_id",bucket.getItem_id());
                params.put("status",statusIS);
                Log.e(TAG,"item id is "+bucket.getItem_id()+"\t \n"+statusIS);
                return  params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}
