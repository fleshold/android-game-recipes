package ru.gold_opt.star783;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class SFWeapon {

		public float posY = 0f;
		public float posX = 0f;
		public boolean shotFired = false;

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
	   
	    private float texture1[] = {
	    		0.0f, 0.0f,
	    		0.25f, 0.0f,
	    		0.25f, 0.25f,
	    		0.0f, 0.25f, 
	    };
	private float texture[] = {
			0.0f, 0.0f,
			0.f, 0.25f,
			0.25f, 0.25f,
			0.25f, 0.0f,
	};

	private final short indices[] = {
			0,1,2,
			0,2,3,
	};

	   public SFWeapon() {

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
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 5);
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
