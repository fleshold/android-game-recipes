package ru.gold_opt.star783;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class SFGame extends Activity {
    final SFEngine gameEngine = new SFEngine();
    private SFGameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new SFGameView(this);
        setContentView(gameView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //
        float x = event.getX();
        float y = event.getY();
        DisplayMetrics outMetrics = new DisplayMetrics();
        SFEngine.display.getMetrics(outMetrics);

        int height = outMetrics.heightPixels / 4;
        int playableArea = outMetrics.heightPixels - height;
        if (y > playableArea){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(x < outMetrics.widthPixels / 3) {
                        SFEngine.playerFlightAction = SFEngine.PLAYER_BANK_LEFT_1;
                        break;
                    }
                    if(x > 2 * outMetrics.widthPixels / 3){
                            SFEngine.playerFlightAction = SFEngine.PLAYER_BANK_RIGHT_1;
                    }else{
                        SFEngine.playerFlightAction = SFEngine.PLAYER_BANK_FIRE;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    SFEngine.playerFlightAction = SFEngine.PLAYER_RELEASE;
                    break;
            }

        }

        return false;
    }

}


