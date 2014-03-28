/********************************************************************** 
 Android-Freeciv - Copyright (C) 2010 - C Vaughn
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
***********************************************************************/

package net.hackcasual.freeciv.views;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import net.hackcasual.freeciv.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import net.hackcasual.freeciv.game.MainGameActivity;




public class MainMenu extends Activity {
	private static final int LOAD_GAME = 1;

	

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainMenu.java", " start freeciv");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("MainMenu.java", " start displaying menu");
    	setContentView(R.layout.mainmenu);
        Log.d("MainMenu.java", " display main menu");
    	
    	/*Thread t = new Thread() {
    		@Override
    		public void run() {
    			try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
				
				that.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Animation aa = new AlphaAnimation(1.0f, 0.0f);
						aa.setDuration(1000);
						aa.setFillAfter(true);
						View v = that.findViewById(R.id.tileset_progress_bar);
						v.startAnimation(aa);
						//v.
						//aa.start();
					}
					
				});
    		}
    	};
    	
    	t.start();*/
    	
    }


	
	public void newGameListener(View v) {
		Intent freeCiv = new Intent(this, MainGameActivity.class);
		startActivity(freeCiv);
		finish();
	}
	
	public void loadGameListener(View v) {
		Intent loadGame = new Intent(this, LoadGame.class);
		//startActivity(freeCiv);
		this.startActivityForResult(loadGame, LOAD_GAME);
	}

    public void multiplayerListener(View v){
        Context context = getApplicationContext();
        CharSequence text = "Coming soon";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void exitListener(View v){
        finish();
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (LOAD_GAME == requestCode && Activity.RESULT_OK == resultCode) {
			//Intent freeCiv = new Intent(this, FreeCiv.class);
            Log.d("MainMenu.java", "onActivityResult");
			//freeCiv.putExtra(LoadGame.SAVED_GAME_TAG, data.getStringExtra(LoadGame.SAVED_GAME_TAG));
			//startActivity(freeCiv);
			finish();			
		}
	}
}
