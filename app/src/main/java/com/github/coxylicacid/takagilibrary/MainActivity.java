package com.github.coxylicacid.takagilibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.coxylicacid.takagi.Takagi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout container = findViewById(R.id.takagiContainer);

        final Takagi takagi = new Takagi(this);
        takagi.setTitle("Hello !");
//        takagi.add("Step 1", "step 1 description");
//        takagi.add("Step 2", "step 2 description");
//        takagi.add("Step 3", "step 3 description");
//        takagi.add("Step 4", "step 4 description");
//        takagi.add("Step 5", "step 5 description");
//        takagi.add("Step 6", "step 6 description");
//        takagi.add("Step 7", "step 7 description");
//        takagi.add("Step 8", "step 8 description");
//        takagi.add("Step 9", "step 9 description");
//        takagi.add("Step 10", "step 10 description");
        takagi.parse(R.xml.takagi_test);
        takagi.applyForViewGroup(container);
        takagi.select(0);

        takagi.get(2).getIndicator().setIndicatorIcon(getResources().getDrawable(R.mipmap.ic_launcher_round));

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takagi.previous();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takagi.next();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takagi.delete(0);
            }
        });
    }

}
