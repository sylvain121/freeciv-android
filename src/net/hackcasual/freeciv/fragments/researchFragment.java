package net.hackcasual.freeciv.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.hackcasual.freeciv.R;

public class researchFragment extends Fragment {

	public researchFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.imprlist, container, false);
         
        return rootView;
    }
}
