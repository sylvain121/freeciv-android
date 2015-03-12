package net.hackcasual.freeciv.game.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.hackcasual.freeciv.R;
import net.hackcasual.freeciv.models.Advance;

import java.util.List;

/**
 * Created by sylvain on 3/12/15.
 */
public class ResearchListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Advance> researchsList = null;

    public ResearchListAdapter(Context context, List researchsList) {
        this.researchsList = researchsList;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return researchsList.size();
    }

    @Override
    public Object getItem(int position) {
        return researchsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView pictureView;
        TextView nameView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.advance, null);
            holder = new ViewHolder();
            holder.pictureView = (ImageView) convertView.findViewById(R.id.icon);
            holder.nameView = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pictureView.setImageBitmap(researchsList.get(position).getIcon());
        holder.nameView.setText(researchsList.get(position).getName());

        return convertView;
    }
}
