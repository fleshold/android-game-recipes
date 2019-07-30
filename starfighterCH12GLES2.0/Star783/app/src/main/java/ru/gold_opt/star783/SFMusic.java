package ru.gold_opt.star783;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class SFMusic extends Service {
	   public static boolean isRunning = false;
	   static MediaPlayer player;
	   private static Context context;
	   private final IBinder binder = new musicBinder();
	   @Override
	   public IBinder onBind(Intent arg0) {
	   	return binder;
		   }     

		   @Override
	   public void onCreate() {         
		   super.onCreate();
		   context = this;
		   setMusicOptions(SFEngine.LOOP_BACKGROUND_MUSIC,SFEngine.R_VOLUME,SFEngine.L_VOLUME,SFEngine.SPLASH_SCREEN_MUSIC);
		   }
	   public static void setMusicOptions(boolean isLooped, int rVolume, int lVolume, int soundFile){
		   player = MediaPlayer.create(context, soundFile);
		   player.setLooping(isLooped);          
		   player.setVolume(rVolume,lVolume);
	   }
	   public int onStartCommand(Intent intent, int flags, int startId) {
		   try
		   {
			   player.start(); 
			   isRunning = true;
		   }catch(Exception e){
			   isRunning = false;
			   player.stop();
		   }

		   return Service.START_STICKY;
	   }      

	   public IBinder onUnBind(Intent arg0) {
			   // TODO Auto-generated method stub          
			   return null;     
		}      
		public void onChange() {
			   isRunning = !isRunning;
			   if (isRunning){
				   player.start();
			   } else {
				   player.pause();
			   }
		}     
		public void onPause() {      }


		public class musicBinder extends Binder {
			SFMusic getService() {
			return SFMusic.this;
		}
	}
		@Override
		public void onDestroy() {          
			   player.stop();         
			   player.release();
		}      
		@Override
		public void onLowMemory() {
			   player.stop();
		}
		
}

