package ru.gold_opt.star783;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class SFGameView extends GLSurfaceView{
    private SFGameRenderer renderer;

    public SFGameView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        renderer = new SFGameRenderer(context);

        this.setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);


    }
}
