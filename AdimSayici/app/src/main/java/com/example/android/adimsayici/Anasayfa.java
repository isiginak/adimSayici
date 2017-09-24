package com.example.android.adimsayici;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static android.R.attr.x;

public class Anasayfa extends AppCompatActivity implements SensorEventListener{
Button basla,durdur,kaydet;
    TextView txtAdim;
    Chronometer kronometre;
    Sensor sensor;
    boolean dogrulama;
    private SensorManager sensorManager;
    private long lastTime;
    public static int steps;
    public static int bigSteps;
    private int tempCount;//For counting real steps without external influence.
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        config.android_id= Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        context = this;

        basla=(Button)findViewById(R.id.basla);
        durdur=(Button)findViewById(R.id.durdur);
        kaydet=(Button)findViewById(R.id.kaydet);
        txtAdim=(TextView)findViewById(R.id.adim);
        kronometre=(Chronometer)findViewById(R.id.sayac);


        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
         sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        tiklama();
    }

    private void tiklama() {
        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            kronometre.setBase(SystemClock.elapsedRealtime());
                kronometre.start();
            dogrulama=true;

            }
        });
        durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            kronometre.stop();
                dogrulama=false;
            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child(config.android_id);
                uygModel model=new uygModel();

                Date tarihSaat = new Date();
                model.tarih=tarihSaat;
                model.sayac=kronometre.getText().toString();
                model.adimSayisi=txtAdim.getText().toString();
                dbref.push().setValue(model);
                Toast.makeText(Anasayfa.this, "Kayıt edildi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER || dogrulama==false) {
            return;
        }
        if(dogrulama==true){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float accelationSquareRoot =  + ((x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)) - 1.0f;
        long actualTime = System.currentTimeMillis();
        if (actualTime - lastTime > 300) {
            if(accelationSquareRoot < -0.45f){
                steps++;
                tempCount++;

                if(steps%50==0){
                    steps++;
                }

                lastTime = actualTime;
            }
        }
        txtAdim.setText(String.valueOf(steps));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.adimsay://listview'ın id si
                Toast.makeText(getApplicationContext(), "Adım Sayısı", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Anasayfa.this,gecmisListesi.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
