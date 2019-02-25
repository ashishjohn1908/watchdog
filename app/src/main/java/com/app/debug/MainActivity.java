package com.app.debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.watchdog.WatchDog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchDog.Watch(MainActivity.this, "15506655826388me9KPYX8R");
            }
        });
    }
}
