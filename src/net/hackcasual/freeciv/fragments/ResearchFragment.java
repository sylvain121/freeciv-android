package net.hackcasual.freeciv.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import net.hackcasual.freeciv.Civ;
import net.hackcasual.freeciv.NativeHarness;
import net.hackcasual.freeciv.R;
import net.hackcasual.freeciv.game.adapter.ResearchListAdapter;
import net.hackcasual.freeciv.models.Advance;
import net.hackcasual.freeciv.models.AdvanceExpense;

import java.lang.reflect.Array;
import java.util.*;

public class ResearchFragment extends Fragment {
    Set<FrameLayout> checked;
    Map<Integer, FrameLayout> layoutMap;
    private List<Advance> advanceList;

    public ResearchFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

         advanceList = ((Civ) this.getActivity().getApplicationContext()).getAdvances();
        //Advance[] advance = NativeHarness.getHarness().loadAdvances();
        //ArrayList<Advance> advanceList = new ArrayList<Advance>(Arrays.asList(advance));

        View rootView = inflater.inflate(R.layout.researchlist, container, false);
        ListView researchView = (ListView) rootView.findViewById(R.id.researchListView);
        researchView.setAdapter(new ResearchListAdapter(this.getActivity().getApplicationContext(), advanceList));

        return rootView;
    }
}
