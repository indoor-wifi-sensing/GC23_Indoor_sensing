package com.iot23.indoorsensing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CalculateRouteActivity extends AppCompatActivity {

    ClassroomGraph classInfo;
    //EditText startPoint, endPoint;
    Button testBtn;
    TextView logView1, logView2, logView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_route);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String starts = bundle.getString("depart");
        String ends = bundle.getString("arrival");

        ClassroomGraphData dataGetHelp = new ClassroomGraphData();
        classInfo = dataGetHelp.inputData();

        //startPoint = findViewById(R.id.startPointEditText);
        //endPoint = findViewById(R.id.endPointEditText);
        testBtn = findViewById(R.id.saveRouteButton);

        logView1 = findViewById(R.id.outputLog);
        logView2 = findViewById(R.id.outputLog2);
        logView3 = findViewById(R.id.outputLog3);

        starts = starts.split("호")[0];

        // 여기부터 원래 버튼 안에 있던 것
        //String startPosition = startPoint.getText().toString();
        //String endPosition = endPoint.getText().toString();

        List<String> shortestPath = classInfo.getShortestPath(starts, ends);
        logView1.setText("Shortest path from " + starts + " to " + ends + ": " + shortestPath);

        // 만약 위치가 하나 차이나거나 차이가 없을 경우 도착 메세지 리턴
        if (shortestPath.size() <= 2) {
            logView2.setText("Arrive to target position");
            return;
        }

        NextDirection eee = new NextDirection();

        int finalDirection = eee.getDirection(shortestPath.get(0), shortestPath.get(1));
        if (finalDirection == 1002) {
            logView2.setText("Use stair Now");
        }
        else if (finalDirection == 1001) {
            logView2.setText("Error for return");
        }
        logView2.setText("Direction : " + finalDirection);


        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
