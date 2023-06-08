package com.iot23.indoorsensing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CalculateRouteActivity extends AppCompatActivity {

    ClassroomGraph classInfo;
    EditText startPoint, endPoint;
    Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_route);

        ClassroomGraphData dataGetHelp = new ClassroomGraphData();
        classInfo = dataGetHelp.inputData();

        startPoint = findViewById(R.id.startPointEditText);
        endPoint = findViewById(R.id.endPointEditText);
        testBtn = findViewById(R.id.saveRouteButton);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPosition = startPoint.getText().toString();
                String endPosition = endPoint.getText().toString();

                List<String> shortestPath = classInfo.getShortestPath(startPosition, endPosition);
                Log.i("dataResult2", "Shortest path from " + startPosition + " to " + endPosition + ": " + shortestPath);

                // 만약 위치가 하나 차이나거나 차이가 없을 경우 도착 메세지 리턴
                if (shortestPath.size() <= 2) {
                    Log.i("dataResult2", "Arrive to target position");
                    return;
                }

                NextDirection eee = new NextDirection();

                int finalDirection = eee.getDirection(shortestPath.get(0), shortestPath.get(1));
                if (finalDirection == 1002) {
                    Log.i("dataResult2", "Use stair Now");
                }
                else if (finalDirection == 1001) {
                    Log.i("dataResult2", "Error for return");
                }
                Log.i("dataResult2", "Direction : " + finalDirection);
            }
        });
    }

}
