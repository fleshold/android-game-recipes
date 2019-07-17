package ru.g_opt.superbanditcosmos;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by fleshold@yandex.ru on 20.06.2017.
 */

public class SBGGameView extends GLSurfaceView {

    public SBGGameView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        setRenderer(new SBGGameRenderer(context));
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
