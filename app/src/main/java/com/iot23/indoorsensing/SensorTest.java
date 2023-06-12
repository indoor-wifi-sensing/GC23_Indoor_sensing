package com.iot23.indoorsensing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class SensorTest extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private Sensor gyroscopeSensor;
    private Sensor accelerometerSensor;

    private ImageView compassImage;
    private TextView headingText;
    private TextView startsTxt;
    private TextView endsTxt;
    private TextView directionTxt;
    private TextView distanceTxt;

    private float currentDegree = 0f;
    private float[] rotationMatrix = new float[9];
    private float[] remappedRotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    private float[] gyroscopeValues = new float[3];
    private float[] accelerometerValues = new float[3];

    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;

    // 0에 북쪽 90에 서쪽 180에 남쪽 270에 동쪽으로 0~360까지
    private float targetDegree = 180f;  // 목표 방위각 변수

    private boolean condition = true;
    WifiManager wifiManager;
    List<ScanResult> wifiResult;
    ClassroomGraph classInfo;
    // 전역 변수
    private int direction = 0; // 가야할 방향
    private String starts, ends; // 현재 위치, 도착지
    private PermissionSupport permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        compassImage = findViewById(R.id.compass_image);
        headingText = findViewById(R.id.heading_text);
        startsTxt = findViewById(R.id.starts);
        endsTxt = findViewById(R.id.ends);
        directionTxt = findViewById(R.id.direction);
        distanceTxt = findViewById(R.id.distance);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        starts = bundle.getString("depart");
        ends = bundle.getString("arrival");
        startsTxt.setText(starts + "호");
        endsTxt.setText(ends + "호");

        ClassroomGraphData dataGetHelp = new ClassroomGraphData();
        classInfo = dataGetHelp.inputData();
        if(starts.equals("411호쪽 계단실")) starts = "411";
        else if(starts.equals("416호쪽 계단실")) starts = "stairsBeta_1";
        else if(starts.equals("401쪽 계단실")) starts = "stairsAlpha_1";
        else if(starts.equals("아르테크네쪽 엘레베이터") || starts.equals("4층 아르테크네") || starts.equals("아르테크네쪽 엘레베이터")) starts = "artechne4f";
        else if(starts.equals("418호쪽 엘레베이터")) starts = "418";
        else if(starts.equals("409호쪽 엘레베이터")) starts = "409";
        else if(starts.equals("5층 아르테크네")) starts = "artechne5f";
        else if(starts.equals("C-CUBE SQUARE")) starts = "betweenIH";
        else if(starts.equals("510호 앞 엘리베이터")) starts = "510";
        else if(starts.equals("520호 앞 엘리베이터")) starts = "520";
        else if(starts.equals("526호 옆 엘리베이터")) starts = "526";
        else if(starts.equals("501호 옆 계단실")) starts = "stairsAlpha";
        else if(starts.equals("511호 옆 계단실")) starts = "511";
        else if(starts.equals("519호 옆 계단실")) starts = "stairsBeta";
        else starts = starts.split("호")[0];

        List<String> shortestPath = classInfo.getShortestPath(starts, ends);

        // 만약 위치가 하나 차이나거나 차이가 없을 경우 도착 메세지 리턴
        if (shortestPath.size() <= 2) {
            Toast.makeText(getApplicationContext(), "Arrive to target position", Toast.LENGTH_SHORT).show();
            return;
        }
        NextDirection eee = new NextDirection();
        int finalDirection = eee.getDirection(shortestPath.get(0), shortestPath.get(1));
        if (finalDirection == 1002) {
            Toast.makeText(getApplicationContext(), "Use stair Now", Toast.LENGTH_SHORT).show();
        }
        else if (finalDirection == 1001) {
            Toast.makeText(getApplicationContext(), "Error for return", Toast.LENGTH_SHORT).show();
        }
        directionTxt.setText(finalDirection + "도");
        targetDegree -= finalDirection;

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
                            targetDegree = 180f;
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
                                Toast.makeText(getApplicationContext(), "Network connection not available", Toast.LENGTH_SHORT).show();
                            }

                            List<String> shortestPath1 = classInfo.getShortestPath(starts, ends);
                            startsTxt.setText(starts + "호");
                            endsTxt.setText(ends + "호");

                            if (isNumeric(starts) && isNumeric(ends)) {
                                int a = Integer.parseInt(starts) - Integer.parseInt(ends);
                                if (a < 0) a = -a;
                                if (a < 3) Toast.makeText(getApplicationContext(), "근처에 도착", Toast.LENGTH_SHORT).show();
                            }
                            // 만약 위치가 하나 차이나거나 차이가 없을 경우 도착 메세지 리턴
                            if (shortestPath.size() <= 2) {
                                Toast.makeText(getApplicationContext(), "Arrive to target position", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            NextDirection eee = new NextDirection();
                            int finalDirection = eee.getDirection(shortestPath1.get(0), shortestPath1.get(1));
                            if (finalDirection == 1002) {
                                Toast.makeText(getApplicationContext(), "Use stair Now", Toast.LENGTH_SHORT).show();
                            }
                            else if (finalDirection == 1001) {
                                Toast.makeText(getApplicationContext(), "Error for return", Toast.LENGTH_SHORT).show();
                            }
                            directionTxt.setText(finalDirection + "도");
                            targetDegree -= finalDirection;
                        }
                    }
                });
            }
        }, 3000, 2500);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy( ) {
        super.onDestroy( );
        condition = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // 원하는 축으로 회전 행렬을 변경
            SensorManager.remapCoordinateSystem(rotationMatrix,
                    SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotationMatrix);

            SensorManager.getOrientation(remappedRotationMatrix, orientationAngles);

            float azimuthInRadians = orientationAngles[0];
            float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);

            float azimuthInDegreesWithOffset = -azimuthInDegrees - targetDegree;  // 목표 방위각을 적용하여 회전
            azimuthInDegreesWithOffset = azimuthInDegreesWithOffset % 360;

            // 방위각이 음수인 경우에 대한 처리
            if (azimuthInDegreesWithOffset < 0) {
                azimuthInDegreesWithOffset += 360f;
            }

            RotateAnimation rotateAnimation = new RotateAnimation(
                    currentDegree,
                    azimuthInDegreesWithOffset,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);

            compassImage.startAnimation(rotateAnimation);
            currentDegree = azimuthInDegreesWithOffset;  // 현재 각도를 업데이트

            String heading = "현재 방향: " + Math.round(currentDegree) + "도";
            headingText.setText(heading);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                gyroscopeValues[0] += axisX * dT;
                gyroscopeValues[1] += axisY * dT;
                gyroscopeValues[2] += axisZ * dT;

                float rotationZ = -gyroscopeValues[2];

                currentDegree += Math.toDegrees(rotationZ);

                RotateAnimation rotateAnimation = new RotateAnimation(
                        (float) (currentDegree - Math.toDegrees(rotationZ)),
                        currentDegree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(250);
                rotateAnimation.setFillAfter(true);

                compassImage.startAnimation(rotateAnimation);

                String heading = "현재 방향: " + Math.round(currentDegree) + "도";
                headingText.setText(heading);
            }
            timestamp = event.timestamp;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerValues, 0, event.values.length);

            float[] gravity = new float[3];
            final float alpha = 0.8f;

            // 중력 센서 데이터를 필터링하여 추출
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // 가속도 센서 데이터에서 중력을 제거하여 나머지 움직임을 추출
            float[] linearAcceleration = new float[3];
            linearAcceleration[0] = event.values[0] - gravity[0];
            linearAcceleration[1] = event.values[1] - gravity[1];
            linearAcceleration[2] = event.values[2] - gravity[2];

            // 나머지 움직임을 활용하여 회전 행렬 계산
            float[] rotation = new float[9];
            SensorManager.getRotationMatrixFromVector(rotation, linearAcceleration);

            // 회전 행렬을 적용하여 방향 계산
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotation, orientation);

            float azimuthInRadians = orientation[0];
            float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);

            float azimuthInDegreesWithOffset = -azimuthInDegrees - targetDegree;  // 목표 방위각을 적용하여 회전
            azimuthInDegreesWithOffset = azimuthInDegreesWithOffset % 360;

            // 방위각이 음수인 경우에 대한 처리
            if (azimuthInDegreesWithOffset < 0) {
                azimuthInDegreesWithOffset += 360f;
            }

            RotateAnimation rotateAnimation = new RotateAnimation(
                    currentDegree,
                    azimuthInDegreesWithOffset,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);

            compassImage.startAnimation(rotateAnimation);
            currentDegree = azimuthInDegreesWithOffset;  // 현재 각도를 업데이트

            String heading = "현재 방향: " + Math.round(currentDegree) + "도";
            headingText.setText(heading);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 센서 정확도 변경 시 호출되는 콜백 메서드
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
                String urlString = "http://172.16.228.173:5000/api";
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

                    String temp = starts;
                    starts = receivedData;

                    if(starts.equals("411호쪽 계단실")) starts = "411";
                    else if(starts.equals("416호쪽 계단실")) starts = "stairsBeta_1";
                    else if(starts.equals("401쪽 계단실")) starts = "stairsAlpha_1";
                    else if(starts.equals("아르테크네쪽 엘레베이터") || starts.equals("4층 아르테크네") || starts.equals("아르테크네쪽 엘레베이터")) starts = "artechne4f";
                    else if(starts.equals("418호쪽 엘레베이터")) starts = "418";
                    else if(starts.equals("409호쪽 엘레베이터")) starts = "409";
                    else if(starts.equals("5층 아르테크네")) starts = "artechne5f";
                    else if(starts.equals("C-CUBE SQUARE")) starts = "betweenIH";
                    else if(starts.equals("510호 앞 엘리베이터")) starts = "510";
                    else if(starts.equals("520호 앞 엘리베이터")) starts = "520";
                    else if(starts.equals("526호 옆 엘리베이터")) starts = "526";
                    else if(starts.equals("501호 옆 계단실")) starts = "stairsAlpha";
                    else if(starts.equals("511호 옆 계단실")) starts = "511";
                    else if(starts.equals("519호 옆 계단실")) starts = "stairsBeta";
                    else if(starts.equals("cannot find your position")) starts = temp;
                    else starts = starts.split("호")[0];

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("text", "result = " + result);
            }
        }
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }


}

