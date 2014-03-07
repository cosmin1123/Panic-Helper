package ninja.PanicHelper.adapter;

/**
 * Created by Cataaa on 3/6/14.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import ninja.PanicHelper.R;

public class ListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ListAdapter(Context context, String[] values) {
        super(context, R.layout.list_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_adapter, parent, false);
        TextView contactName = (TextView) rowView.findViewById(R.id.contactName);
        TextView contactNr = (TextView) rowView.findViewById(R.id.contactNr);
        contactNr.setText((position + 1) + "");
        contactName.setText(values[position]);
        return rowView;
    }
}