package kailashsoni.com.MyBucketList.Mode;

/**
 * Created by kailashsoni on 13/4/18
 */

// This class contains the bucket list item information
    // The variables are defined here too and returns back
public class BucketItem  {
   String item_id,itme_title,item_target_date,item_achieved_date,item_description
           ,item_category,item_image,item_location,item_latitude
           ,item_logitude,item_publish_inspiration,statusIS,userFname,userLname;

    public BucketItem(String item_id, String itme_title, String item_target_date,
                      String item_achieved_date, String item_description,
                      String item_category, String item_image, String item_location,
                      String item_latitude, String item_logitude, String item_publish_inspiration, String statusIS) {
        this.item_id = item_id;
        this.itme_title = itme_title;
        this.item_target_date = item_target_date;
        this.item_achieved_date = item_achieved_date;
        this.item_description = item_description;
        this.item_category = item_category;
        this.item_image = item_image;
        this.item_location = item_location;
        this.item_latitude = item_latitude;
        this.item_logitude = item_logitude;
        this.item_publish_inspiration = item_publish_inspiration;
        this.statusIS = statusIS;
    }

    public BucketItem(String item_id, String itme_title, String item_target_date,
                      String item_achieved_date, String item_description,
                      String item_category, String item_image, String item_location,
                      String item_latitude, String item_logitude, String item_publish_inspiration,
                      String statusIS, String userFname, String userLname) {
        this.item_id = item_id;
        this.itme_title = itme_title;
        this.item_target_date = item_target_date;
        this.item_achieved_date = item_achieved_date;
        this.item_description = item_description;
        this.item_category = item_category;
        this.item_image = item_image;
        this.item_location = item_location;
        this.item_latitude = item_latitude;
        this.item_logitude = item_logitude;
        this.item_publish_inspiration = item_publish_inspiration;
        this.statusIS = statusIS;
        this.userFname = userFname;
        this.userLname = userLname;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getStatusIS() {
        return statusIS;
    }

    public void setStatusIS(String statusIS) {
        this.statusIS = statusIS;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItme_title() {
        return itme_title;
    }

    public void setItme_title(String itme_title) {
        this.itme_title = itme_title;
    }

    public String getItem_target_date() {
        return item_target_date;
    }

    public void setItem_target_date(String item_target_date) {
        this.item_target_date = item_target_date;
    }

    public String getItem_achieved_date() {
        return item_achieved_date;
    }

    public void setItem_achieved_date(String item_achieved_date) {
        this.item_achieved_date = item_achieved_date;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_location() {
        return item_location;
    }

    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }

    public String getItem_latitude() {
        return item_latitude;
    }

    public void setItem_latitude(String item_latitude) {
        this.item_latitude = item_latitude;
    }

    public String getItem_logitude() {
        return item_logitude;
    }

    public void setItem_logitude(String item_logitude) {
        this.item_logitude = item_logitude;
    }

    public String getItem_publish_inspiration() {
        return item_publish_inspiration;
    }

    public void setItem_publish_inspiration(String item_publish_inspiration) {
        this.item_publish_inspiration = item_publish_inspiration;
    }


}
