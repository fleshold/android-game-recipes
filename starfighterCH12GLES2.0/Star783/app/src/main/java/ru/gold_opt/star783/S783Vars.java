
package ru.gold_opt.star783;
import android.view.Display;

public class S783Vars {
    public static Display sDisplay;
    public static final int PLAYER_MOVE_LEFT = 1;
    public static final int PLAYER_STAND = 0;
    public static final int PLAYER_MOVE_RIGHT = 2;


    // public static Display display;
    public static int playeraction = 0;
    public static float playerCurrentLocation = -0.75f;
    public static float playerCurrentLocationY = -0.0f;
    public static int numberOfCicles = 0;
    public static float currentRunAniFrame = 0f;
    public static float currentStandingFrame = 0.75f;
    public static final float PLAYER_RUN_SPEED = .25f;
    public static final float STANDING_LEFT = 0f;

    public static final float STANDING_RIGHT = .75f;
    public static int field[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, //0-8
            1, 0, 0, 0, 0, 0, 0, 0, 0, //9-17
            1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 2, 0, 0, 0, 0,
            0, 0, 2, 2, 2, 0, 0, 2, 0

    };

}
