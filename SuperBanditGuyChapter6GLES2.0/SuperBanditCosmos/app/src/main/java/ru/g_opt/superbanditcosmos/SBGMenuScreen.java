package ru.g_opt.superbanditcosmos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by fleshold@yandex.ru on 26.12.2017.
 */

public class SBGMenuScreen extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        ImageButton start = (ImageButton) findViewById(R.id.btnStart);
        ImageButton exit = (ImageButton) findViewById(R.id.btnExit);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(getApplicationContext(),SBGGameMain.class);
                SBGMenuScreen.this.startActivity(game);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pid= android.os.Process.myPid();
                android.os.Process.killProcess(pid);

            }
        });
    }
}
