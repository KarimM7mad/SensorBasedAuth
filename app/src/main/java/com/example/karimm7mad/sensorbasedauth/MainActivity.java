package com.example.karimm7mad.sensorbasedauth;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    public TextView msgTxt, debugTxt, sensorReadingTxt;
    public Button startTimerBtn, stopTimerBtn;
    public CountDownTimer downTimer;
    private SensorManager sensMan;
    private Sensor linSensor;
    public int numOfShakesInInterval;
    // variables for shake detection
    private static final float SHAKE_THRESHOLD = 3.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 550;
    private long mLastShakeTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //txt Viewers
        this.msgTxt = findViewById(R.id.msgTxt);
        this.debugTxt = findViewById(R.id.debugTxt);
        this.sensorReadingTxt = findViewById(R.id.sensorReadings);
        this.numOfShakesInInterval = 0;
        //sensor itself
        this.sensMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.linSensor = sensMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //create Timer Object
        this.createTimeer();
        //startTimerbtn
        this.startTimerBtn = findViewById(R.id.startTimerbtn);
        this.startTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgTxt.setText("");
                downTimer.start();
                Log.d("asdasd", "debugyAna: Timer Started");
            }
        });
        //stopTimerbtn
        this.stopTimerBtn = findViewById(R.id.stopTimerbtn);
        this.stopTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asdasd", "debugyAna: Timer Stopped");
                downTimer.cancel();
            }
        });

    }


    public void createTimeer() {
        msgTxt.setText("");
        this.downTimer = new CountDownTimer(10000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("asdasd", "debugyAna: Seconds Remaining->" + (millisUntilFinished / 1000));
//                Log.d("asdasd", "debugyAna: NumOfShakes->" + numOfShakesInInterval);

                msgTxt.append(numOfShakesInInterval + "-");

                numOfShakesInInterval = 0;
            }

            @Override
            public void onFinish() {
                Log.d("asdasd", "debugyAna: Timer is Finished");


                msgTxt.append(numOfShakesInInterval + "-");

                numOfShakesInInterval = 0;
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
//                Log.d("asdasd", "Acceleration is " + acceleration + "m/s^2");

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    numOfShakesInInterval++;
                    Log.d("asdasd", "Shake, Rattle, and Roll");
                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
