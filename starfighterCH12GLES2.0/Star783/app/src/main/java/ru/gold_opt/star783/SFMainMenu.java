package ru.gold_opt.star783;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class SFMainMenu extends Activity {
    /** Called when the activity is first created. */
	final SFEngine engine = new SFEngine();
	private boolean bound = false;
	private SFMusic myService;
	private ServiceConnection sConn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SFEngine.context = getApplicationContext();
       
        
        /** Fire up background music */
       SFEngine.musicThread = new Thread(){
        	public void run(){
        		Intent bgmusic = new Intent(getApplicationContext(), SFMusic.class);
        		startService(bgmusic);

        	}
        };
        SFEngine.musicThread.start();
		Intent bgmusic = new Intent(SFEngine.context, SFMusic.class);


		sConn = new ServiceConnection() {

			public void onServiceConnected(ComponentName name, IBinder binder) {

				myService = ((SFMusic.musicBinder)binder).getService();
				bound = true;
			}

			public void onServiceDisconnected(ComponentName name) {

				bound = false;
			}
		};
		bindService(bgmusic, sConn,0);

        ImageButton music = (ImageButton)findViewById(R.id.btnMusic);
        music.getBackground().setAlpha(SFEngine.MENU_BUTTON_ALPHA);
        music.setHapticFeedbackEnabled(SFEngine.HAPTIC_BUTTON_FEEDBACK);
        /** Set menu button options */
        ImageButton start = (ImageButton)findViewById(R.id.btnStart);
        ImageButton exit = (ImageButton)findViewById(R.id.btnExit);
        
        start.getBackground().setAlpha(SFEngine.MENU_BUTTON_ALPHA);
        start.setHapticFeedbackEnabled(SFEngine.HAPTIC_BUTTON_FEEDBACK);
       
        exit.getBackground().setAlpha(SFEngine.MENU_BUTTON_ALPHA); 
        exit.setHapticFeedbackEnabled(SFEngine.HAPTIC_BUTTON_FEEDBACK);

        music.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				myService.onChange();
				//SFEngine.context.stopService(bgmusic);
			}
		});

        start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/** Start Game!!!! */

				Intent game = new Intent(getApplicationContext(),SFGame.class);
				SFMainMenu.this.startActivity(game);

			}
        	
        });
        
        exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				boolean clean = false;
				clean = engine.onExit(v);	
				if (clean)
				{
					int pid= android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
			}
        	});
    }

}
