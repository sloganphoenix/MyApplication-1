package com.example.nim.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nim.R;

/**
 * Created by apple on 2016/7/19.
 */
public class Message extends Activity {

    private TextView msg;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        msg = (TextView)findViewById(R.id.message);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("friendAccount");
        msg.setText(data);



    }
}
