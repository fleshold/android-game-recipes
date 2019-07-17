package ru.g_opt.superbanditcosmos;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/**
 * Created by fleshold@yandex.ru on 26.12.2017.
 */

public class SBGGameMain extends Activity {
    private SBGGameView gameView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new SBGGameView(this);
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

        float x = event.getX();
        float y = event.getY();

        DisplayMetrics outMetrics = new DisplayMetrics();

        SBGVars.sDisplay.getMetrics(outMetrics);

        int height = outMetrics.heightPixels / 4;

        int playableArea = outMetrics.heightPixels - height;
        if (y > playableArea){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(x < outMetrics.widthPixels / 2){
                        SBGVars.playeraction = SBGVars.PLAYER_MOVE_LEFT;
                    }else{
                        SBGVars.playeraction = SBGVars.PLAYER_MOVE_RIGHT;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    SBGVars.playeraction = SBGVars.PLAYER_STAND;
                    break;
            }
        }
        else {
            return false;
        }

        return false;
    }

}
