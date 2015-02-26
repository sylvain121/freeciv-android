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

import android.app.Fragment;
import net.hackcasual.freeciv.Civ;
import net.hackcasual.freeciv.NativeEventListener;

import android.os.Bundle;
import android.util.Log;

public abstract class NativeAwareFragment extends Fragment implements NativeEventListener {
	public boolean isConnected = false;
	
	public void setConnectionStatus(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Civ app = (Civ) this.getActivity().getApplication();
		
		app.hookNativeEventListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Civ app = (Civ) this.getActivity().getApplication();
		
		app.unhookNativeEventListener(this);
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.i("FreeCiv", "In NAA: oC");
    }
}