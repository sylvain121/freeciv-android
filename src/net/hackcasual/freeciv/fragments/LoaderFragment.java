package net.hackcasual.freeciv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.hackcasual.freeciv.R;
import net.hackcasual.freeciv.views.NativeAwareFragment;

import java.util.HashMap;
import java.util.Map;

class TilesetProgress {
    public final String name;
    public final int progressVal;
    public TilesetProgress(String n, int v) {
        name = n;
        progressVal = v;
    }
}
public class LoaderFragment extends NativeAwareFragment {

    public LoaderFragment() {}

    private static final Map<String, TilesetProgress> progressMap = new HashMap<String, TilesetProgress>();

    static {
        progressMap.put("loading_indicators", new TilesetProgress("Indicators", 4));
        progressMap.put("loading_mask", new TilesetProgress("Masks", 8));
        progressMap.put("loading_spaceship", new TilesetProgress("Spaceship", 12));
        progressMap.put("loading_cursors", new TilesetProgress("Cursors", 17));
        progressMap.put("loading_roads", new TilesetProgress("Roads", 21));
        progressMap.put("loading_unitinfo", new TilesetProgress("Units", 25));
        progressMap.put("loading_cities", new TilesetProgress("Cities", 30));
        progressMap.put("loading_overlays", new TilesetProgress("Overlays", 34));
        progressMap.put("loading_farmland", new TilesetProgress("Farmland", 38));
        progressMap.put("loading_fog", new TilesetProgress("Fog", 43));
        progressMap.put("Lake", new TilesetProgress("Lakes", 47));
        progressMap.put("Ocean", new TilesetProgress("Oceans", 51));
        progressMap.put("Deep Ocean", new TilesetProgress("Deep Oceans", 56));
        progressMap.put("Glacier", new TilesetProgress("Glaciers", 60));
        progressMap.put("Desert", new TilesetProgress("Deserts", 64));
        progressMap.put("Forest", new TilesetProgress("Forests", 69));
        progressMap.put("Grassland", new TilesetProgress("Grasslands", 73));
        progressMap.put("Hills", new TilesetProgress("Hills", 77));
        progressMap.put("Jungle", new TilesetProgress("Jungles", 82));
        progressMap.put("Mountains", new TilesetProgress("Mountains", 86));
        progressMap.put("Plains", new TilesetProgress("Plains", 90));
        progressMap.put("Swamp", new TilesetProgress("Swamps", 95));
        progressMap.put("Tundra", new TilesetProgress("Tundra", 100));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.loader, container, false);

        return rootView;
    }

    @Override
    public void receiveTilesetUpdate(String info) {


        if (progressMap.containsKey(info)) {
            final TilesetProgress tp = progressMap.get(info);
            final ProgressBar progressBar = (ProgressBar)this.getActivity().findViewById(R.id.current_progress_bar);
            final TextView progressItem = (TextView)this.getActivity().findViewById(R.id.current_progress_item);
            this.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (progressBar.getProgress() <= tp.progressVal) {
                        progressBar.setProgress(tp.progressVal);
                        progressItem.setText(tp.name);
                    }
                }

            });
        }

    }
}
