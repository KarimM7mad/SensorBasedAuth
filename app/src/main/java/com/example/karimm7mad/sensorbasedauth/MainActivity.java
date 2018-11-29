package com.example.karimm7mad.sensorbasedauth;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    public TextView msgTxt,debugTxt;
    private SensorManager sensMan;
    private Sensor linSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.msgTxt = findViewById(R.id.msgTxt);
        this.debugTxt= findViewById(R.id.debugTxt);

        this.sensMan = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        this.linSensor = sensMan.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        Toast.makeText(this.getBaseContext(), this.linSensor.getName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this.getBaseContext(), this.linSensor.getMinDelay()+"", Toast.LENGTH_SHORT).show();



        List<Sensor> deviceSensors = this.sensMan.getSensorList(Sensor.TYPE_ALL);

        this.debugTxt.setText("");
        for (Sensor s:deviceSensors){
            this.debugTxt.append(""+s.getName()+"\n");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.sensMan.registerListener(this,linSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
            return;

        Log.d("asdasd", "onSensorChanged: x="+event.values[0]+" y="+event.values[1]+" z="+event.values[2]);
//        msgTxt.setText("x="+event.values[0]+" y="+event.values[1]+" z="+event.values[2]);
        msgTxt.setText("x="+event.values[0]+" y="+event.values[1]+" z="+event.values[2]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
