package ru.gold_opt.star783;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class SFBackground {
    public boolean isDead = false;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 TexCoordIn;" +
                    "varying vec2 TexCoordOut;" +
                    "void main() {" +
                    "gl_Position = uMVPMatrix * vPosition;" +
                    "TexCoordOut = TexCoordIn;" +
                    "}";
    ;
    private final String fragmentShaderCode =
            "precision mediump float;" +

                    "uniform sampler2D textures;" +
                  //  "uniform float posX;" +
                    "uniform float scroll;" +
                    "varying vec2 TexCoordOut;" +
                    "void main() {" +
                    "gl_FragColor = texture2D(textures, vec2(TexCoordOut.x ,TexCoordOut.y +scroll));" +
                    "}";
    private int[] textures = new int[1];


    static float vertices1[] = {
            -1.0f,  1.0f, 0.0f,   // top left
            -1.0f, -1.0f, 0.0f,   // bottom left
            1.0f, -1.0f, 0.0f,   // bottom right
            1.0f,  1.0f, 0.0f }; // top right
    private float[] texture1 = {
            0f, 0f,
            1f, 0f,
            1f, 1f,
            0f, 1f
    };

    private float vertices[] = {
            -1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
    };

    private float texture[] = {
            0.0f, 0.0f,
            1.0f, 0f,
            1f, 1.0f,
            0f, 1f,
    };


   // private float[] texture = {
   //         1f, 0f,
   //         1f, 1f,
   //         0f, 1f,
   //         0f, 0f
   // };


    private final short[] indices = {0, 1, 2, 0, 2, 3};
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;
    private final FloatBuffer textureBuffer;
    // private final ByteBuffer drawListBuffer;
    //   private final ShortBuffer drawListBuffer;

    private final int mProgram;

    private int mPositionHandle;
    private int mMVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3;
    static final int COORDS_PER_TEXTURE = 2;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    public static int textureStride = COORDS_PER_TEXTURE * 4;
    public float scroll = 0;

    public SFBackground() {

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);


        ByteBuffer dlb= ByteBuffer.allocateDirect(indices.length*2);
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer=dlb.asShortBuffer();

        indexBuffer.put(indices);
        indexBuffer.position(0);

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
       // SFGameRenderer.checkGlError("glCreateShader");
        GLES20.glShaderSource(vertexShader, vertexShaderCode);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void loadTexture(int texture, Context context) {
        InputStream imagestream = context.getResources().openRawResource(texture);
        Bitmap bitmap = null;
        Bitmap temp;
        android.graphics.Matrix flip = new android.graphics.Matrix();
        flip.postScale(-1f, 1f);
        try {
            temp = BitmapFactory.decodeStream(imagestream);
            bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),temp.getHeight(),flip , true);
            imagestream.close();
            imagestream = null;

        } catch (Exception e) {//;
        }

        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
    public void draw(float[] mvpMatrix){
        scroll += .001f;
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        int vsTextureCoord = GLES20.glGetAttribLocation(mProgram, "TexCoordIn");

        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,     GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(vsTextureCoord, COORDS_PER_TEXTURE,     GLES20.GL_FLOAT, false,
                textureStride, textureBuffer);
        GLES20.glEnableVertexAttribArray(vsTextureCoord);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

      //  int fsTexture = GLES20.glGetUniformLocation(mProgram, "textures");
        //  int fsTexture = GLES20.glGetAttribLocation(mProgram, "TexCoordOut");

        int fsTexture = GLES20.glGetUniformLocation(mProgram, "TexCoordOut");
        int fsScroll = GLES20.glGetUniformLocation(mProgram, "scroll");
        GLES20.glUniform1i(fsTexture, 0);
        GLES20.glUniform1f(fsScroll, scroll);

        GLES20.glUniform1i(fsTexture, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indices.length,     GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        //SBGGameRenderer.checkGlError("glCreateShader");
        GLES20.glDisableVertexAttribArray(mPositionHandle);



    }


}

