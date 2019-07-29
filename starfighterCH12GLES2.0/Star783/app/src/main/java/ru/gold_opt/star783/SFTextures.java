package ru.gold_opt.star783;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public class SFTextures {
	   
	   private int[] textures = new int[2];
	   
	public SFTextures(){

		GLES20.glGenTextures(2, textures, 0);
	      
	}
	 public int[] loadTexture( int texture, Context context, int textureNumber) {
		 InputStream imagestream = context.getResources().openRawResource(texture);
		 Bitmap bitmap = null;
		 Bitmap temp;
		 android.graphics.Matrix flip = new android.graphics.Matrix();
		 //flip.postScale(-1f, 1f);
		 try {
			 temp = BitmapFactory.decodeStream(imagestream);
			 bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),temp.getHeight(), flip, true);
			 imagestream.close();
			 imagestream = null;

		 } catch (Exception e) {//;
		 }


		 GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[textureNumber - 1]);
		 GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		 GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		 GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		 GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		 GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		 bitmap.recycle();
	      
	      return textures;
	   }
}
