package pro.devapp.flashlight;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton flashlightButton;
    Switch switch_button;
    boolean isCameraFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Приветствие
         */
        if(PreferenceHelper.get(this, "hello", "no").equals("no")){
            findViewById(R.id.hello).setVisibility(View.VISIBLE);
            PreferenceHelper.save(MainActivity.this, "hello", "yes");

            findViewById(R.id.hide_hello).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.hello).setVisibility(View.GONE);
                }
            });
        }

        /**
         * Доступность вспышки
         */
        isCameraFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        /**
         * Кнопка переключения режима
         */
        switch_button = (Switch) findViewById(R.id.switch_button);
        switch_button.setChecked(PreferenceHelper.get(this, "type", "led").equals("screen"));

        if(!isCameraFlash){
            switch_button.setVisibility(View.GONE);
            PreferenceHelper.save(this, "type", "screen");
        } else {
            switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        PreferenceHelper.save(MainActivity.this, "type", "screen");
                    } else {
                        PreferenceHelper.save(MainActivity.this, "type", "led");
                    }
                }
            });
        }

        flashlightButton = (ToggleButton) findViewById(R.id.flashlightButton);

        flashlightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(CameraHolder.isProcess()){
                    return;
                }
                if(isChecked){
                    if(!isCameraFlash || !CameraHolder.hasFlash() || PreferenceHelper.get(MainActivity.this, "type", "led").equals("screen")){
                        Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
                        startActivity(intent);
                    } else {
                        OnTask task = new OnTask();
                        task.execute();
                    }
                } else {
                    CameraHolder.stop();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        // подготавливаем камеру
        CameraHolder.getCamera();
        // отмечаем состояние и запускаем камеру если нужно
        switch_button.setChecked(PreferenceHelper.get(this, "type", "led").equals("screen"));
        flashlightButton.setChecked(CameraHolder.isOn());
        if(CameraHolder.isOn()){
            CameraHolder.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        CameraHolder.release();
    }
}
