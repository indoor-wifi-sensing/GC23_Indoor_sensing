package com.iot23.indoorsensing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_find, btn_route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_find = findViewById(R.id.find);
        btn_route = findViewById(R.id.direct);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindLocationActivity.class);
                startActivity(intent);
            }
        });

        btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalculateRouteActivity.class);
                startActivity(intent);
            }
        });
    }
}