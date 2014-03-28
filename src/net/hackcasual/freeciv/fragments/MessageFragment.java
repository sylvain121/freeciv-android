package net.hackcasual.freeciv.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.hackcasual.freeciv.R;

/**
 * Created by esme7383 on 28/03/14.
 */
public class MessageFragment extends Fragment {

    public MessageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.researchlist, container, false);
        return rootView;
    }
}
