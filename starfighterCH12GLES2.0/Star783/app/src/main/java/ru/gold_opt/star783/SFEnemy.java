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
import java.util.Random;

public class SFEnemy {

		public float posY = 0f;
		public float posX = 0f;
		public float posT = 0f;
		public float incrementXToTarget = 0f;
		public float incrementYToTarget = 0f;
		public int attackDirection = 0;
		public boolean isDestroyed = false;
		private int damage = 0;
		
		public int enemyType = 0;
		
		public boolean isLockedOn = false;
		public float lockOnPosX = 0f;
		public float lockOnPosY = 0f;

		private Random randomPos = new Random();
		
	   private FloatBuffer vertexBuffer;
	   private FloatBuffer textureBuffer;
	private final ShortBuffer indexBuffer;
	private final int mProgram;

	private int mPositionHandle;
	private int mMVPMatrixHandle;

	static final int COORDS_PER_VERTEX = 3;
	static final int COORDS_PER_TEXTURE = 2;
	private final int vertexStride = COORDS_PER_VERTEX * 4;
	public static int textureStride = COORDS_PER_TEXTURE * 4;
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
					"varying vec2 TexCoordOut;" +
					"void main() {" +
					"gl_FragColor = texture2D(textures, vec2(TexCoordOut.x ,TexCoordOut.y ));" +
					"}";
	private int[] textures = new int[1];

	private float vertices[] = {
	                   0.0f, 0.0f, 0.0f, 
	                   1.0f, 0.0f, 0.0f,  
	                   1.0f, 1.0f, 0.0f,  
	                   0.0f, 1.0f, 0.0f,
	                                 };



	    private float texture[] = {          
	                   0.50f, 0.25f,
	                   0.50f, 0.50f,
	                   0.75f, 0.50f,
	                   0.75f, 0.25f,
	                                  };
	        
	    private final short indices[] = {
	                   0,1,2, 
	                   0,2,3, 
	                                  };
	    public void applyDamage(){
	    	damage++;
	    	switch(enemyType){
	    	case SFEngine.TYPE_INTERCEPTOR:
	    		if (damage == SFEngine.INTERCEPTOR_SHIELDS){
	    			isDestroyed = true;
	    		}
	    		break;
	    	case SFEngine.TYPE_SCOUT:
	    		if (damage == SFEngine.SCOUT_SHIELDS){
	    			isDestroyed = true;
	    		}
	    		break;
	    	case SFEngine.TYPE_WARSHIP:
	    		if (damage == SFEngine.WARSHIP_SHIELDS){
	    			isDestroyed = true;
	    		}
	    		break;
	    	}
	    }

	   public SFEnemy(int type, int direction) {
		   enemyType = type;
		   attackDirection = direction;
		   posY = 3;
		   switch(attackDirection){
		   case SFEngine.ATTACK_LEFT:
			   posX = 0;
			   break;
		   case SFEngine.ATTACK_RANDOM:

		   	posX = 3 - randomPos.nextFloat() * 6;

			   break;
		   case SFEngine.ATTACK_RIGHT:
			   posX = 3;
			   break;
		   }
		   posT = SFEngine.SCOUT_SPEED;
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
		   //      SBGGameRenderer.checkGlError("glCreateShader");
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
	   public float getNextScoutX(){
		   if (attackDirection == SFEngine.ATTACK_LEFT){
			   return (float)((SFEngine.BEZIER_X_4*(posT*posT*posT)) + (SFEngine.BEZIER_X_3 * 3 * (posT * posT) * (1-posT)) + (SFEngine.BEZIER_X_2 * 3 * posT * ((1-posT) * (1-posT))) + (SFEngine.BEZIER_X_1 * ((1-posT) * (1-posT) * (1-posT))));
		   }else{
			   return (float)((SFEngine.BEZIER_X_1*(posT*posT*posT)) + (SFEngine.BEZIER_X_2 * 3 * (posT * posT) * (1-posT)) + (SFEngine.BEZIER_X_3 * 3 * posT * ((1-posT) * (1-posT))) + (SFEngine.BEZIER_X_4 * ((1-posT) * (1-posT) * (1-posT))));
		   }
		   
	   }
	   public float getNextScoutY(){
		   return (float)((SFEngine.BEZIER_Y_1*(posT*posT*posT)) + (SFEngine.BEZIER_Y_2 * 3 * (posT * posT) * (1-posT)) + (SFEngine.BEZIER_Y_3 * 3 * posT * ((1-posT) * (1-posT))) + (SFEngine.BEZIER_Y_4 * ((1-posT) * (1-posT) * (1-posT))));
	   }

	   public void draw(float[] mvpMatrix,float posX, float posY,int spriteSheet) {


		   GLES20.glUseProgram(mProgram);
		   mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
		   GLES20.glEnableVertexAttribArray(mPositionHandle);
		   int vsTextureCoord = GLES20.glGetAttribLocation(mProgram, "TexCoordIn");
//bug
		   GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,     GLES20.GL_FLOAT, false,
				   vertexStride, vertexBuffer);
		   GLES20.glVertexAttribPointer(vsTextureCoord, COORDS_PER_TEXTURE,     GLES20.GL_FLOAT, false,
				   textureStride, textureBuffer);
		   GLES20.glEnableVertexAttribArray(vsTextureCoord);
		   GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		   GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
		   GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 4);
		   //  int fsTexture = GLES20.glGetUniformLocation(mProgram, "textures");
		   int fsTexture = GLES20.glGetAttribLocation(mProgram, "TexCoordOut");

		   GLES20.glUniform1i(fsTexture, 0);

		   mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

		   GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

		   GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indices.length,     GLES20.GL_UNSIGNED_SHORT, indexBuffer);
		   // SBGGameRenderer.checkGlError("glCreateShader");
		   GLES20.glDisableVertexAttribArray(mPositionHandle);
	   }

}

