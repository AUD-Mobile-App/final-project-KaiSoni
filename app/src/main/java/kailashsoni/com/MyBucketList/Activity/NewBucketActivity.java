package kailashsoni.com.MyBucketList.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import kailashsoni.com.MyBucketList.R;

// This is for the toolbar for the newest bucket list item
public class NewBucketActivity extends AppCompatActivity {

    private Toolbar newestToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newestToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
