package com.example.karimm7mad.sensorbasedauth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    public TextView msgTxt, debugTxt, sensorReadingTxt;
    public Button startTimerBtn, stopTimerBtn;
    public CountDownTimer mainDownTimer, subDownTimer;
    private SensorManager sensMan;
    private Sensor linSensor;
    public int numOfShakesInInterval;
    // variables for shake detection
    private static final float SHAKE_THRESHOLD = 3.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 550;
    private long mLastShakeTime;
    public boolean startedTimer = false;
    public ProgressBar loading;
    public static final String patternToFind = "1-2-3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //txt Viewers
        this.msgTxt = findViewById(R.id.msgTxt);
        this.debugTxt = findViewById(R.id.debugTxt);
        this.sensorReadingTxt = findViewById(R.id.sensorReadings);
        this.loading = findViewById(R.id.determinateBar);
        this.loading.setProgress(100);
        this.numOfShakesInInterval = 0;
        //sensor itself
        this.sensMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.linSensor = sensMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //create Timer Object
        msgTxt.setText("");
        debugTxt.setText("Click on \"Start Timer\" to Proceed");
        this.createTimers();
        //startTimerbtn
        this.startTimerBtn = findViewById(R.id.startTimerbtn);
        this.startTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDownTimer.start();
                subDownTimer.start();
                debugTxt.setText("");
                Log.d("asdasd", "debugyAna: Timer Started");
            }
        });
        //stopTimerbtn
        this.stopTimerBtn = findViewById(R.id.stopTimerbtn);
        this.stopTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDownTimer.cancel();
                subDownTimer.cancel();
                startedTimer = false;
                loading.setProgress(100);
                msgTxt.setText("");
                debugTxt.setText("Click on \"Start Timer\" to Proceed");
                Log.d("asdasd", "debugyAna: Timer Stopped");
            }
        });

    }


    public void createTimers() {
        msgTxt.setText("");
        this.mainDownTimer = new CountDownTimer(10000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                subDownTimer.cancel();
                loading.setProgress(0);
                Log.d("asdasd", "debugyAna: Seconds Remaining->" + (millisUntilFinished / 1000));
                if (startedTimer)
                    msgTxt.append(numOfShakesInInterval + "-");
                else
                    startedTimer = true;
                numOfShakesInInterval = 0;
                subDownTimer.start();
            }

            @Override
            public void onFinish() {
                Log.d("asdasd", "debugyAna: Timer is Finished");
                msgTxt.append(numOfShakesInInterval + "");
                numOfShakesInInterval = 0;
                startedTimer = false;
                if (msgTxt.getText().toString().equalsIgnoreCase(MainActivity.patternToFind))
                    debugTxt.setText("Password Correct");
                else {
                    debugTxt.setText("Wrong Password, Try Again");
                    msgTxt.setBackgroundColor(getResources().getColor(R.color.red, getTheme()));
                }
            }
        };
        this.subDownTimer = new CountDownTimer(3000, 150) {
            @Override
            public void onTick(long millisUntilFinished) {
                loading.setProgress((int) ((3000 - millisUntilFinished) / 30));
            }

            @Override
            public void onFinish() {
                loading.setProgress(100);
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.sensMan.registerListener(this, linSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                double acceleration = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    numOfShakesInInterval++;
                    Log.d("asdasd", "Device Shaked");
                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
