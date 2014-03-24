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

package net.hackcasual.freeciv;

import android.util.Log;
import net.hackcasual.freeciv.views.FreeCiv;
import net.hackcasual.freeciv.views.MainMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class StartupSplash extends Activity {


    @Override
    protected void onCreate(Bundle state){
       super.onCreate(state);
       Log.d("StartupSlash", "start freeciv");
       this.startActivity(new Intent(this, MainMenu.class));
       this.finish();

    }
}
