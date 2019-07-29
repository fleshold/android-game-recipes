package ru.gold_opt.star783;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.Random;

import static ru.gold_opt.star783.SFEngine.*;


public class SFGameRenderer implements GLSurfaceView.Renderer {
   private SFBackground background1;
    private SFPanel background2;
    private S783Ship ship;
    private Context context;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mMVPMatrix0 = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mTMatrix = new float[16];
    private SFEnemy[] enemies = new SFEnemy[SFEngine.TOTAL_INTERCEPTORS + SFEngine.TOTAL_SCOUTS + SFEngine.TOTAL_WARSHIPS - 1];
    private SFWeapon[] playerFire = new SFWeapon[4];
    private SFTextures textureLoader;
    private int[] spriteSheets = new int[2];
    public SFGameRenderer(Context appContext) {
        context  = appContext;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        background1= new SFBackground();

        background1.loadTexture(R.drawable.background3,context);
        background2 = new SFPanel();
        background2.loadTexture(R.drawable.panelfire,context);

        ship = new S783Ship();

        ship.loadTexture(CHARACTER_SHEET,context);
        textureLoader = new SFTextures();
        spriteSheets = textureLoader.loadTexture(SFEngine.CHARACTER_SHEET, context, 1);
        spriteSheets = textureLoader.loadTexture(SFEngine.WEAPONS_SHEET, context, 2);
        SFEngine.playerFlightAction = PLAYER_RELEASE;
        initializePlayerWeapons();
        initializeInterceptors();


        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) 0.5* height / width;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //прозрачность
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc (GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0.0f, 0.0f, 0f, 1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix0, 0, mProjMatrix, 0, mVMatrix, 0);

        background1.draw(mMVPMatrix);
        background2.draw(mMVPMatrix,currentFireFrame);
        movePlayer();
        moveBlock();

        moveEnemy();
        detectCollisions();
    }
    private void initializeInterceptors(){
        for (int x = 0; x<SFEngine.TOTAL_INTERCEPTORS  ; x++){
            SFEnemy interceptor = new SFEnemy(SFEngine.TYPE_INTERCEPTOR, SFEngine.ATTACK_RANDOM);
            enemies[x] = interceptor;
        }
    }
    private void initializeScouts(){
        for (int x = SFEngine.TOTAL_INTERCEPTORS -1; x<SFEngine.TOTAL_INTERCEPTORS + SFEngine.TOTAL_SCOUTS -1; x++){
            SFEnemy interceptor;
            if (x>=(SFEngine.TOTAL_INTERCEPTORS + SFEngine.TOTAL_SCOUTS) / 2 ){
                interceptor = new SFEnemy(SFEngine.TYPE_SCOUT, SFEngine.ATTACK_RIGHT);
            }else{
                interceptor = new SFEnemy(SFEngine.TYPE_SCOUT, SFEngine.ATTACK_LEFT);
            }
            enemies[x] = interceptor;
        }
    }
    private void initializeWarships(){
        for (int x = SFEngine.TOTAL_INTERCEPTORS + SFEngine.TOTAL_SCOUTS -1; x<SFEngine.TOTAL_INTERCEPTORS + SFEngine.TOTAL_SCOUTS + SFEngine.TOTAL_WARSHIPS -1; x++){
            SFEnemy interceptor = new SFEnemy(SFEngine.TYPE_WARSHIP, SFEngine.ATTACK_RANDOM);
            enemies[x] = interceptor;
        }
    }
    private void initializePlayerWeapons(){
        for(int x = 0; x < 4; x++){
            SFWeapon weapon = new SFWeapon();
            playerFire[x] = weapon;
        }
        playerFire[0].shotFired = true;
        playerFire[0].posX = playerCurrentLocation;
        playerFire[0].posY = -2.0f;
    }
    private void moveBlock() {

    }

    private void moveEnemy(){


      //  for (int x = 0; x < SFEngine.TOTAL_INTERCEPTORS + SFEngine.TOTAL_SCOUTS + SFEngine.TOTAL_WARSHIPS - 27; x++){
        for (int x = 0; x < SFEngine.TOTAL_INTERCEPTORS ; x++){
            if (!enemies[x].isDestroyed){
                Random randomPos = new Random();
                switch (enemies[x].enemyType){
                    case SFEngine.TYPE_INTERCEPTOR:
                       // if (0==1) {
                            if (enemies[x].posY < -3.0f) {
                                enemies[x].posY = 3.0f;
                                enemies[x].posX = 3 - randomPos.nextFloat() * 6 ;
                                enemies[x].isLockedOn = false;
                                enemies[x].lockOnPosX = 0;
                            }

                            if (enemies[x].posY >= 3) {
                                enemies[x].posY -= SFEngine.INTERCEPTOR_SPEED;
                            } else {
                                if (!enemies[x].isLockedOn) {
                                    enemies[x].lockOnPosX = playerCurrentLocation;
                                    enemies[x].isLockedOn = true;
                                    enemies[x].incrementXToTarget = (float) ((enemies[x].lockOnPosX - enemies[x].posX) / (enemies[x].posY / (SFEngine.INTERCEPTOR_SPEED * 4)));
                                }
                                enemies[x].posY -= (SFEngine.INTERCEPTOR_SPEED * 4);
                                enemies[x].posX += enemies[x].incrementXToTarget;

                            }
                        // }
                        Matrix.setIdentityM(mTMatrix, 0);
                        Matrix.scaleM(mTMatrix, 0,0.25f, 0.25f, 1f);
                        Matrix.setIdentityM(mMVPMatrix0, 0);
                        Matrix.multiplyMM(mMVPMatrix0, 0, mProjMatrix, 0, mVMatrix, 0);
                        Matrix.translateM(mTMatrix, 0, enemies[x].posX, enemies[x].posY, 0);
                        Matrix.multiplyMM(mMVPMatrix0, 0, mTMatrix, 0, mMVPMatrix0, 0);
                     enemies[x].draw(mMVPMatrix0, 0.0f,0.0f,spriteSheets[0]);

                        break;
                    case SFEngine.TYPE_SCOUT:
                        if (enemies[x].posY <= 0){
                            enemies[x].posY = (randomPos.nextFloat() * 4) + 4;
                            enemies[x].isLockedOn = false;
                            enemies[x].posT = SFEngine.SCOUT_SPEED;
                            enemies[x].lockOnPosX = enemies[x].getNextScoutX();
                            enemies[x].lockOnPosY = enemies[x].getNextScoutY();
                            if(enemies[x].attackDirection == SFEngine.ATTACK_LEFT){
                                enemies[x].posX = 0;
                            }else{
                                enemies[x].posX = 3f;
                            }
                        }


                        if (enemies[x].posY >= 2.75f){
                            enemies[x].posY -= SFEngine.SCOUT_SPEED;

                        }else{
                            enemies[x].posX = enemies[x].getNextScoutX();
                            enemies[x].posY = enemies[x].getNextScoutY();
                            enemies[x].posT += SFEngine.SCOUT_SPEED;


                        }


                        break;
                    case SFEngine.TYPE_WARSHIP:
                        if (enemies[x].posY < 0){
                            enemies[x].posY = (randomPos.nextFloat() * 4) + 4;
                            enemies[x].posX = randomPos.nextFloat() * 3;
                            enemies[x].isLockedOn = false;
                            enemies[x].lockOnPosX = 0;
                        }

                        if (enemies[x].posY >= 3){
                            enemies[x].posY -= SFEngine.WARSHIP_SPEED;

                        }else{
                            if (!enemies[x].isLockedOn){
                                enemies[x].lockOnPosX = randomPos.nextFloat() * 3;
                                enemies[x].isLockedOn = true;
                                enemies[x].incrementXToTarget =  (float) ((enemies[x].lockOnPosX - enemies[x].posX )/ (enemies[x].posY / (SFEngine.WARSHIP_SPEED  * 4)));
                            }
                            enemies[x].posY -= (SFEngine.WARSHIP_SPEED  * 2);
                            enemies[x].posX += enemies[x].incrementXToTarget;

                        }


                        break;

                }

            }
        }

    }
    private void detectCollisions(){
        for (int y = 0; y < 3; y ++){
            if (playerFire[y].shotFired){
                for (int x = 0; x < SFEngine.TOTAL_INTERCEPTORS ; x++ ){
                    if(!enemies[x].isDestroyed && enemies[x].posY < 3){
                        if ((playerFire[y].posY >= enemies[x].posY - 1
                                && playerFire[y].posY <= enemies[x].posY )
                                && (playerFire[y].posX <= enemies[x].posX + 1
                                && playerFire[y].posX >= enemies[x].posX - 1)){
                            int nextShot = 0;
                            enemies[x].applyDamage();
                            playerFire[y].shotFired = false;
                            if (y == 3){
                                nextShot = 0;
                            }else{
                                nextShot = y + 1;
                            }
                            if (playerFire[nextShot].shotFired == false){
                                playerFire[nextShot].shotFired = true;
                                playerFire[nextShot].posX = SFEngine.playerCurrentLocation;
                                playerFire[nextShot].posY = -2.0f;
                            }
                        }
                    }
                }
            }
        }
    }
    private void firePlayerWeapon(){

        for(int x = 0; x < 4; x++  ){
            if (playerFire[x].shotFired){
                int nextShot = 0;
                if (playerFire[x].posY > 4.25){
                    playerFire[x].shotFired = false;
                }else{
                    if (playerFire[x].posY> 2){
                        if (x == 3){
                            nextShot = 0;
                        }else{
                            nextShot = x + 1;
                        }
                        if (playerFire[nextShot].shotFired == false){
                            playerFire[nextShot].shotFired = true;
                            playerFire[nextShot].posX = SFEngine.playerCurrentLocation;
                            playerFire[nextShot].posY = -2.0f;
                        }

                    }
                    playerFire[x].posY += SFEngine.PLAYER_BULLET_SPEED;
                    Matrix.setIdentityM(mTMatrix, 0);
                    Matrix.scaleM(mTMatrix, 0,0.25f, 0.25f, 1f);
                    Matrix.setIdentityM(mMVPMatrix, 0);
                    Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

                    Matrix.translateM(mTMatrix, 0,playerFire[x].posX, playerFire[x].posY, 0);
                    Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);


                    playerFire[x].draw(mMVPMatrix,0,0,5);



                }
            }
        }
    }

    private void movePlayer() {
        switch(SFEngine.playerFlightAction){
            case SFEngine.PLAYER_BANK_LEFT_1:

                SFEngine.currentStandingFrame = SFEngine.STANDING_RIGHT;
                SFEngine.playerCurrentLocation -= SFEngine.PLAYER_RUN_SPEED;
              //  SFEngine.playerBankPosX += SFEngine.PLAYER_BANK_SPEED;
                SFEngine.currentRunAniFrame -= .25f;
                if (currentRunAniFrame< .0f)
                {
                    currentRunAniFrame = .75f;
                }
                if (playerCurrentLocation< -4.0f)
                {
                    playerCurrentLocation=4.0f;

                }
                Matrix.setIdentityM(mTMatrix, 0);
                Matrix.scaleM(mTMatrix, 0,0.25f, 0.25f, 1f);

                Matrix.translateM(mTMatrix, 0, playerCurrentLocation, -3.0f, 0);
                Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);

                ship.draw(mMVPMatrix,0.0f,0.0f);
                //ship.draw(mMVPMatrix,SFEngine.currentRunAniFrame,0.0f);
                //   ship.draw(mMVPMatrix, 0.0f,0.0f);
                break;
            case PLAYER_BANK_RIGHT_1:
                currentStandingFrame = STANDING_LEFT;
                playerCurrentLocation += PLAYER_RUN_SPEED;
                currentRunAniFrame += .25f;
                if (currentRunAniFrame> .75f)
                {

                    currentRunAniFrame = .0f;
                }
                if (playerCurrentLocation> 4.0f)
                {
                    playerCurrentLocation=-4.0f;
                //    break;
                }
                Matrix.setIdentityM(mTMatrix, 0);
                Matrix.scaleM(mTMatrix, 0,0.25f, 0.25f, 1f);

                //  Matrix.setIdentityM(mTMatrix, 0);
                Matrix.translateM(mTMatrix, 0, playerCurrentLocation, -3.0f, 0);
                Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);

                ship.draw(mMVPMatrix,0.0f,0.0f);

                break;
            case PLAYER_BANK_FIRE:
                if(currentFireFrame == 0){
                    currentFireFrame = -0.5f;
                }else{
                    currentFireFrame = 0.0f;
                }

               break;
            case  PLAYER_RELEASE:
                Matrix.setIdentityM(mTMatrix, 0);
                Matrix.scaleM(mTMatrix, 0,0.25f, 0.25f, 1f);

                //  Matrix.setIdentityM(mTMatrix, 0);
                Matrix.translateM(mTMatrix, 0, playerCurrentLocation, -3.0f, 0);
                Matrix.multiplyMM(mMVPMatrix, 0, mTMatrix, 0, mMVPMatrix, 0);

                ship.draw(mMVPMatrix,0.0f,0.0f);

                break;
        }
        firePlayerWeapon();
    }
    }
