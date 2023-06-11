package com.iot23.indoorsensing;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CalculateRouteActivity extends AppCompatActivity {

    ClassroomGraph classInfo;
    //EditText startPoint, endPoint;
    Button testBtn;
    TextView logView1, logView2, logView3;
    private String starts;

    // Wifi 스캐닝, 권한 획득 관련 변수
    WifiManager wifiManager;
    private PermissionSupport permission;

    List<ScanResult> wifiResult;

    private boolean condition = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_route);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        starts = bundle.getString("depart");
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

        Timer timerMTimer = new Timer(true);
        Handler handler = new Handler();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        timerMTimer.schedule(new TimerTask() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                handler.post(new Runnable(){
                    public void run(){
                        if(condition) {
                            boolean success = wifiManager.startScan();
                            if (!success) {
                                scanFailure();
                            }
                            wifiResult = wifiManager.getScanResults();

                            Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                                @Override
                                public int compare(ScanResult o1, ScanResult o2) {
                                    return o2.level - o1.level;
                                }
                            };
                            Collections.sort(wifiResult, comparator);

                            String[][] wifiData = new String[500][2];
                            int i = 0;
                            for (ScanResult choseWifi : wifiResult) {
                                String MAC = choseWifi.BSSID;
                                int rss = choseWifi.level;
                                wifiData[i][0] = MAC;
                                wifiData[i][1] = Integer.toString(rss);
                                i++;
                            }

                            Gson gson = new Gson();
                            String jsonWifiData = gson.toJson(wifiData); // converting wifiData to JSON format

                            if (isNetworkAvailable()) {
                                new SendDataTask().execute(jsonWifiData); // passing the json string instead of String array
                            } else {
                                logView3.setText("Network connection not available");
                            }

                            List<String> shortestPath1 = classInfo.getShortestPath(starts, ends);
                            logView1.setText("1Shortest path from " + starts + " to " + ends + ": " + shortestPath1);
                        }
                    }
                });
            }
        }, 10000, 10000);
    }

    //===========================================
    //========== WiFi 스캐닝 컨트롤 영역 ===========
    //===========================================
    //수집한 Wifi 정보를 배열에 뿌리는 역할
    private void wifiAnalyzer() {
        List<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        String mac, dbm, freq;
        for (ScanResult choseWifi : wifiResult) {
            mac = choseWifi.BSSID;
            dbm = Integer.toString(choseWifi.level);
            freq = Integer.toString(choseWifi.frequency);

            String completeInfo = mac + " | " + dbm + " | " + freq;
            list.add(completeInfo);
        }
    }

    //Wifi 정보 스캔에 성공했을 경우에 행동할 것들
    private void scanSuccess() {
        @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();
        Log.e("wifi", results.toString());

    }

    //Wifi 정보 스캔에 실패했을 경우에 행동할 것들
    private void scanFailure() {
        @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();
        Log.e("wifi", results.toString());
        Toast.makeText(this.getApplicationContext(), "Wifi Scan Failure, Old Information may appear.", Toast.LENGTH_LONG).show();
    }

    private void permissionCheck() {
        permission = new PermissionSupport(this, this);
        if (!permission.checkPermission()) {
            permission.requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            permission.requestPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class SendDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlString = "http://172.30.1.13:5000/api";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // UTF-8 인코딩 설정
                conn.setDoOutput(true);

                // 요청 데이터 생성
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("data", params[0]);

                Log.d("wifi", jsonParam.toString());

                // 요청 데이터 전송
                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                Log.e("server", "성공");
                // 응답 데이터 수신
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); // UTF-8 인코딩으로 읽기
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Network request failed: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String message = jsonResponse.getString("message");
                    String receivedData = jsonResponse.getString("received_data");

                    logView3.setText("Message: " + message + "\nReceived Data: " + receivedData);
                    starts = receivedData.split("호")[0];

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("text", "result = " + result);
            }
        }
    }

}

