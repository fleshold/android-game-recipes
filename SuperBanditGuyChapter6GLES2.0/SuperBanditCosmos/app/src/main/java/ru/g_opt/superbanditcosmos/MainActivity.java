package ru.g_opt.superbanditcosmos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends Activity {
 // static int GAME_THREAD_DELAY = 99900;
 static int GAME_THREAD_DELAY = 3999;
    private GLSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SBGVars.sDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {

                Intent mainMenu = new Intent(MainActivity.this, SBGMenuScreen.class);
                MainActivity.this.startActivity(mainMenu);
                MainActivity.this.finish();
                overridePendingTransition(R.layout.fadein,R.layout.fadeout);
            }
        } , GAME_THREAD_DELAY);
    }


}
