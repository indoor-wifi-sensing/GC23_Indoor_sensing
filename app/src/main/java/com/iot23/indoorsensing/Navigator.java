package com.iot23.indoorsensing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Navigator extends AppCompatActivity {

    // 캔버스 그리기 부분
    private DrawTouchView mView;

    // 센서 사용 관련 부분
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;

    // 전역 변수
    private int direction = 0; // 가야할 방향
    private String starts, ends; // 현재 위치, 도착지

    // 정기적으로 루프할 타이머 관련 변수
    private Handler handler = new Handler();
    private Runnable runnable;
    private final int delay = 5000; // 루프 시간 (밀리초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new DrawTouchView(this);
        setContentView(mView);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        starts = bundle.getString("depart");
        ends = bundle.getString("arrival");

        // TODO 출발지와 도착지를 넣으면 방위각을 리턴하는 함수 적용 (서버)
        direction = 100;

        if (direction == 1001) {
            // 에러 리턴 시의 처리

        }
        if (direction == 1002) {
            // 계단 오르라고 할 시의 처리
        }

        // SensorManager 초기화
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 가속도계 센서 초기화
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // SensorEventListener 초기화
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // 가속도 센서 값이 변경될 때 호출되는 메서드
                float x = event.values[0];  // x축 가속도 값
                float y = event.values[1];  // y축 가속도 값

                // 가속도 센서 값을 전달하여 이미지 회전
                mView.rotateImage(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // 가속도 센서의 정확도가 변경될 때 호출되는 메서드
            }
        };
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);

                // TODO 여기에 5초마다 반복할 함수 적기. WIFI 스캐닝 후 서버에서 현재 위치 불러오는 함수 작성
                // 현재 위치값을 "starts" 변수에 넣어줄 것

                starts = "405호";
                direction += 10;
            }
        }, delay);

        super.onResume();
        mView.startRotation();

        // 가속도계 센서 등록
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.stopRotation();

        // 루프 해제
        handler.removeCallbacks(runnable);
        // 가속도계 센서 해제
        sensorManager.unregisterListener(sensorEventListener);
    }

    public class DrawTouchView extends View {
        private static final int ROTATION_DELAY = 1000; // 1초
        private static final float DEGREE_PER_FRAME = 1.0f;

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Bitmap image;
        private float canvasX, canvasY;
        private float centerX, centerY;
        private Matrix matrix;
        private boolean rotating = false;
        private Handler rotationHandler;

        public DrawTouchView(Context context) {
            super(context);
            init();
        }

        public DrawTouchView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DrawTouchView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            // 이미지 로드
            image = BitmapFactory.decodeResource(getResources(), R.drawable.downarrow);

            // 회전을 위한 Matrix 객체 생성
            matrix = new Matrix();

            rotationHandler = new Handler();
        }

        public void startRotation() {
            rotating = true;
            rotateImage(0, 0);
        }

        public void stopRotation() {
            rotating = false;
            rotationHandler.removeCallbacksAndMessages(null);
        }

        public void rotateImage(float x, float y) {
            if (rotating) {
                float rotation = (float) Math.toDegrees(Math.atan2(x, y));
                rotation += Math.floorMod(180 + direction, 360);
                matrix.setRotate(rotation, image.getWidth() / 2.0f, image.getHeight() / 2.0f);
                invalidate();

            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            // 이미지 중심 좌표 계산
            centerX = w / 2.0f;
            centerY = h / 2.0f;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // 이미지 그리기
            canvasX = canvas.getWidth() + image.getWidth();
            canvasY = canvas.getHeight() + image.getHeight();

//            android.util.Log.e("test", Float.toString(canvas.getWidth())); // 1080
//            android.util.Log.e("test", Float.toString(canvas.getHeight())); // 2067
//            android.util.Log.e("test", Float.toString(image.getWidth())); // 1344
//            android.util.Log.e("test", Float.toString(image.getHeight())); // 1344
            centerX = canvas.getWidth() / 2.0f - image.getWidth() / 2.0f;
            centerY = canvas.getHeight() / 2.0f - image.getHeight() / 2.0f;
            matrix.postTranslate(centerX, centerY);
            canvas.drawBitmap(image, matrix, paint);

            paint.setTextSize(80);
            canvas.drawText("현재 위치 : " + starts, 10, 80, paint);
            canvas.drawText("도착지 : " + ends, 10, 160, paint);
            canvas.drawText("현재 방향 : " + direction, 10, 240, paint);
            canvas.drawText("남은 거리 : " + direction, 10, 320, paint);
        }
    }
}
