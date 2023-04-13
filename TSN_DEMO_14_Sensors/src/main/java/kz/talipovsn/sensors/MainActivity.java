package kz.talipovsn.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private ImageView compass;
    private static Sensor sensor;
    private float current_degree;
    private MediaPlayer mp;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compass = findViewById(R.id.compass_arrow);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.lobster);

        text = findViewById(R.id.textView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        RotateAnimation animation = new RotateAnimation(current_degree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(120);
                animation.setFillAfter(true);
                compass.startAnimation(animation);
                current_degree = -degree;
                text.setText(String.format("%s %s", current_degree, " градусов"));
                if (current_degree > -226 && current_degree < -224) {
                    mp.start();
                }
            }

    // Задаем метод, вызываемый при старте программы и возвращении ее к активности из фона
    @Override
    protected void onResume() {
        super.onResume();

        // Регистрируем в менеджере сенсоров прослушивание компаса с небольшой точностью (зато быстро определяется)
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    // Задаем метод, вызываемый при изменении точности сенсора
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
