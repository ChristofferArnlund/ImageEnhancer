package com.example.christoffer.pixplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final String TAG ="pixPlusMessage";
    private ImageButton loadButton;
    final static int SELECT_IMAGE = 10;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_Feed);

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_Enhance);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_Settings);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        loadButton = (ImageButton) findViewById(R.id.load_button);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.i(TAG,"onCreate");




        loadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_IMAGE);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }
}
