package ru.g_opt.superbanditcosmos;

import android.view.Display;

/**
 * Created by fleshold@yandex.ru on 15.01.2018.
 */

public class SBGVars {
    public static Display sDisplay;
    public static final int PLAYER_MOVE_LEFT = 1;
    public static final int PLAYER_STAND = 0;
    public static final int PLAYER_MOVE_RIGHT = 2;

    public static int playeraction = 0;

    public static float playerCurrentLocation = .0f;
    public static float currentRunAniFrame = 0f;
    public static float currentStandingFrame = 0.75f;
    public static final float PLAYER_RUN_SPEED = .05f;
    public static final float STANDING_LEFT = 0f;

    public static final float STANDING_RIGHT = .75f;

}
