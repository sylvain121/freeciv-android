package net.hackcasual.freeciv.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import net.hackcasual.freeciv.Civ;
import net.hackcasual.freeciv.NativeHarness;
import net.hackcasual.freeciv.R;
import net.hackcasual.freeciv.game.adapter.ResearchListAdapter;
import net.hackcasual.freeciv.models.Player;

/**
 * Created by sylvain on 3/12/15.
 */
public class NationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Player player = NativeHarness.getPlayerInfo();

        View rootView = inflater.inflate(R.layout.nation_info, container, false);
        ((TextView) rootView.findViewById(R.id.player_name)).setText(player.getName());
        ((TextView) rootView.findViewById(R.id.nation_name)).setText(player.getNationName());
        ((TextView) rootView.findViewById(R.id.governement_type)).setText(player.getCurrentGovernment().getName());
        ((TextView) rootView.findViewById(R.id.population)).setText(String.valueOf(player.getPopulation()));

        ((ImageView)rootView.findViewById(R.id.flags)).setImageBitmap(player.getFlag());
        ((ImageView)rootView.findViewById(R.id.governement_type_icon)).setImageBitmap(player.getCurrentGovernment().getIcon());

        return rootView;
    }

}
