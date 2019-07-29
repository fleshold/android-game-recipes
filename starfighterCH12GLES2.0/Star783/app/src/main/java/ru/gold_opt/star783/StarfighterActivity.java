package ru.gold_opt.star783;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.content.Intent;
import android.content.Context;
public class StarfighterActivity extends Activity {
    static int GAME_THREAD_DELAY = 3999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SFEngine.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        super.onCreate(savedInstanceState);
        /*display the splash screen image*/
        setContentView(R.layout.splashscreen);
        /*start up the splash screen and main menu in a time delayed thread*/
        SFEngine.context = this;
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                Intent mainMenu = new Intent(StarfighterActivity.this, SFMainMenu.class);
                StarfighterActivity.this.startActivity(mainMenu);
                StarfighterActivity.this.finish();
                overridePendingTransition(R.layout.fadein,R.layout.fadeout);
            }
        }, SFEngine.GAME_THREAD_DELAY);
    }
}


