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

import org.json.JSONArray;
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
 * Created by kailashsoni on 11/4/18
 */

// This  class is created for the bucket list items to be updated
public class showBucketItemAdapter extends BaseAdapter {
    public ArrayList<BucketItem> bucketitemArray;
    private LayoutInflater inflater;
    private static final int TYPE_UNACHIEVED = 0;
    private static final int TYPE_ACHIEVED = 1;
    private Context context;
    private int count = 0;
    private BucketItem bucket;
    public static String titleString = null;
    private ProgressDialog pDialog;
    private String TAG = "Bucketitem Adapter";
    private SessionManager sessionManager;
    public static  String bukID = null;

// Adapter with the array list defined with the session manager
    public showBucketItemAdapter(Context applicationContext, ArrayList<BucketItem> bucketArrayList) {
        this.context = applicationContext;
        this.bucketitemArray = bucketArrayList;
        pDialog = new ProgressDialog(context);
        sessionManager = new SessionManager(context);
    }

    // returns the count for bucket items array
    @Override
    public int getCount() {
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

    // gets the view of the items required by using the Viewholder
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = new ViewHolder();
        final BucketItem bucket = (BucketItem) getItem(position);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.single_bucketitem_cardview_layout, null);

            holder.item_cardview = convertView.findViewById(R.id.SingleCardView);
            holder.item_title = convertView.findViewById(R.id.itemTitle);
            holder.item_subtitle =  convertView.findViewById(R.id.item_sub_Title);
            holder.target_date =  convertView.findViewById(R.id.item_target_date);
            holder.item_imgs = convertView.findViewById(R.id.itemImg);
            holder.latngTxt =  convertView.findViewById(R.id.latTxtvalueSingleitem);
            holder.longiTxt =  convertView.findViewById(R.id.lonTxtvalueSingleitem);
            holder.itemCheck =  convertView.findViewById(R.id.achivedCheckbox);
            holder.image_check = convertView.findViewById(R.id.image_check);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.item_title.setText(bucketitemArray.get(position).getItme_title());
        holder.item_subtitle.setText(bucketitemArray.get(position).getItme_title());
        holder.target_date.setText(bucketitemArray.get(position).getItem_target_date());
        String itemImg_URL = bucketitemArray.get(position).getItem_image();

        holder.latngTxt.setText(bucketitemArray.get(position).getItem_latitude());
        holder.longiTxt.setText(bucketitemArray.get(position).getItem_logitude());
        final String statusValue = bucketitemArray.get(position).getStatusIS();

        bukID = bucketitemArray.get(position).getItem_id();

        Log.e("status:",""+statusValue);

        if (statusValue.equalsIgnoreCase("true")) {
            holder.itemCheck.setVisibility(View.GONE);
            holder.image_check.setVisibility(View.VISIBLE);
            holder.itemCheck.setEnabled(true);
            //holder.itemCheck.setChecked(true);

        } else  {
            holder.image_check.setVisibility(View.GONE);
            holder.itemCheck.setVisibility(View.VISIBLE);
            holder.itemCheck.setEnabled(true);
            holder.itemCheck.setChecked(false);

        }

        Log.e(TAG,"items img url is "+itemImg_URL);
        if(itemImg_URL.isEmpty()){
            holder.item_imgs.setVisibility(View.GONE);
        }else {
            holder.item_imgs.setVisibility(View.VISIBLE);
            Picasso.with(context).load(Common.ROOT_URL + itemImg_URL).into(holder.item_imgs);
        }

// For the card the onsetclicklistener for each of the items on the bucketlist
        holder.item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemIntent = new Intent(context, AddBucketItem.class);
                itemIntent.putExtra("itemid_KEY", bucketitemArray.get(position).getItem_id());
                itemIntent.putExtra("itemimg_KEY", bucketitemArray.get(position).getItem_image());
                itemIntent.putExtra("itemtitle_KEY", bucketitemArray.get(position).getItme_title());
                itemIntent.putExtra("itemdesc_KEY", bucketitemArray.get(position).getItem_description());
                itemIntent.putExtra("itemcat_KEY", bucketitemArray.get(position).getItem_category());
                itemIntent.putExtra("itemtarget_KEY", bucketitemArray.get(position).getItem_target_date());
                itemIntent.putExtra("itemachieved_KEY", bucketitemArray.get(position).getItem_achieved_date());
                itemIntent.putExtra("itemlocation_KEY", bucketitemArray.get(position).getItem_location());
                itemIntent.putExtra("itemlat_KEY", bucketitemArray.get(position).getItem_latitude());
                itemIntent.putExtra("itemlon_KEY", bucketitemArray.get(position).getItem_logitude());
                itemIntent.putExtra("itemInspire_KEY", bucketitemArray.get(position).getItem_publish_inspiration());
                itemIntent.putExtra("status_KEY", bucketitemArray.get(position).getStatusIS());
                itemIntent.putExtra("updateKEY", "true");
                itemIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(itemIntent);
            }
        });

        if (!holder.itemCheck.isChecked()) {
            holder.itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked == true) {
                        //Log.e(TAG,"session is "+sessionManager.getUserDetails().get(SessionManager.KEY_UID));
                        Log.e(TAG,"itemId is "+bucketitemArray.get(position).getItem_id() );
                        Log.e(TAG,"session si "+sessionManager.getUserDetails().get(SessionManager.KEY_UID) );
                        setAchievedItem("true",position,bucketitemArray.get(position).getItem_id());

                    }else {
                        Log.e(TAG,"unchecked clicked ");
                    }

                }

            });
        }
        final ViewHolder finalHolder = holder;
        holder.image_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG,"image clicked");
                finalHolder.image_check.setVisibility(View.GONE);
                finalHolder.itemCheck.setVisibility(View.VISIBLE);
                Log.e(TAG,"itemId is "+bucketitemArray.get(position).getItem_id() );
                Log.e(TAG,"session si "+sessionManager.getUserDetails().get(SessionManager.KEY_UID) );
                setAchievedItem("false",position,bucketitemArray.get(position).getItem_id());

            }
        });

        return convertView;

    }
   // define variables for the viewholder
    public class ViewHolder {

        TextView item_title;
        TextView item_subtitle;
        TextView target_date;
        ImageView item_imgs;

        TextView latngTxt;
        TextView longiTxt;

        CheckBox itemCheck;
        CardView item_cardview;
        ImageView image_check;
    }
    // when the items are completed using the update items and using JSON array
    private void setAchievedItem(final  String statusIS ,final int position, final String itemId) {
        pDialog.show();
        String URL = Common.ROOT_URL + Common.updateBucketStatusAPI;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response is" + response);
                pDialog.dismiss();
                notifyDataSetChanged();
                try {
                    JSONArray jsonArrObj = new JSONArray(response);

                    for (int i = 0;i<jsonArrObj.length();i++){
                        JSONObject jsonObj = jsonArrObj.getJSONObject(i);
                        String status =  jsonObj.getString("status");
                        if(status.equalsIgnoreCase("true")){
                            if (!bucketitemArray.isEmpty()){
                                bucketitemArray.clear();
                            }
                            String unachievedlist =  jsonObj.getString("unachived_result");
                            String achievedlist =  jsonObj.getString("acheved_result");
                            //Log.e(TAG,"unachieved is "+unachievedlist);
                            JSONArray UNjsonArray = new JSONArray(unachievedlist);
                            JSONArray ACjsonArray = new JSONArray(achievedlist);
                            //Log.e(TAG,"json array size is "+UNjsonArray.length());
                            for(int j = 0;j<UNjsonArray.length();j++){
                                JSONObject jsonObject = UNjsonArray.getJSONObject(j);
                                String un_status = jsonObject.getString("status");
                                //Log.e(TAG,"unachieved status is "+un_status);
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
                                    BucketItem Unachieved_Item = new BucketItem(un_item_id,un_item_title,un_item_target_date
                                            ,un_item_achieved_date,un_item_description,un_item_category,un_item_image,un_item_location_name
                                            ,un_item_latidude,un_item_longitude,un_item_inspirations,"false");
                                    bucketitemArray.add(Unachieved_Item);
                                }
                            }

                            // update items in array for the string
                            for(int v = 0;v<ACjsonArray.length();v++){
                                JSONObject jsonObject = ACjsonArray.getJSONObject(v);
                                String ac_status = jsonObject.getString("status");
                               // Log.e(TAG,"achieved status is "+ac_status);
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
                                    BucketItem Achieved_Item = new BucketItem(ac_item_id,ac_item_title,ac_item_target_date,ac_item_achieved_date
                                            ,ac_item_description,ac_item_category,ac_item_image,ac_item_location_name,ac_item_latidude
                                            ,ac_item_longitude,ac_item_inspirations,"true");

                                    bucketitemArray.add(Achieved_Item);
                                    //Log.e(TAG,"===ac_item_id is "+ac_item_id);
                                }else  if(ac_status.equalsIgnoreCase("false")){
                                    //Log.e(TAG,"NO data for achieved list !!!");
                                }
                            }
                        }
                    }

                    notifyDataSetChanged();
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
                params.put("bucket_item_id", itemId);
                params.put("status", statusIS);
                params.put("target_date_old", "true");
                params.put("user_id", sessionManager.getUserDetails().get(SessionManager.KEY_UID));
                Log.e(TAG, "params is " + params);
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}
