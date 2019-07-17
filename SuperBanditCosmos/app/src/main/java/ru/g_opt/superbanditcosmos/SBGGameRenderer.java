package ru.g_opt.superbanditcosmos;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import static ru.g_opt.superbanditcosmos.SBGVars.*;


/**
 * Created by fleshold@yandex.ru on 20.06.2017.
 */

public class SBGGameRenderer implements Renderer {

  private SBG sbg;
    private Context context;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mTMatrix = new float[16];
    private long loopStart = 0;
    private static final String TAG = "SBGGameRenderer";
   private int totalTime = 0;
    public SBGGameRenderer(Context appContext) {
        context  = appContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        sbg = new SBG();
        sbg.loadTexture(R.drawable.sbgrunning,context);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) 0.5*width / height;
        float bottom = -1;
        float top = 1;
        float near = 3.0f;
        float far = 7.0f;

        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, bottom, top, near, far);
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3f, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        Matrix.setIdentityM(mTMatrix, 0);
        Matrix.scaleM(mTMatrix, 0,0.25f, 0.25f, 1f);

        Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        movePlayer();
    }
    private void movePlayer(){
        if(!sbg.isDead)
        {
            switch(playeraction){
                case PLAYER_MOVE_RIGHT:

                    currentStandingFrame = STANDING_RIGHT;
                    playerCurrentLocation += PLAYER_RUN_SPEED;
                    currentRunAniFrame -= .25f;
                    if (currentRunAniFrame< .0f)
                    {
                        currentRunAniFrame = .75f;
                    }
                   // Matrix.translateM(mTMatrix, 0, 0.0f, -1.0f, 0);
                    Matrix.setIdentityM(mTMatrix, 0);
                    Matrix.translateM(mTMatrix, 0, 0.05f, 0.0f, 0);
                    Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);

                    sbg.draw(mMVPMatrix, currentRunAniFrame, .75f);
                    break;
                case PLAYER_MOVE_LEFT:
                    currentStandingFrame = STANDING_LEFT;
                    playerCurrentLocation -= PLAYER_RUN_SPEED;
                    currentRunAniFrame += .25f;
                    if (currentRunAniFrame> .75f)
                    {
                        currentRunAniFrame = .0f;
                    }
                    Matrix.setIdentityM(mTMatrix, 0);
                    Matrix.translateM(mTMatrix, 0, -0.05f, 0.0f, 0);
                    Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);
                    sbg.draw(mMVPMatrix, currentRunAniFrame, .50f);
                    break;
                case PLAYER_STAND:

                    sbg.draw(mMVPMatrix, currentStandingFrame, .25f);
                    break;
            }
        }
    }



    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
