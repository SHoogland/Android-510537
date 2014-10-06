package nl.hoogland.stephan.android_510537.adapters;

import nl.hoogland.stephan.android_510537.R;
import nl.hoogland.stephan.android_510537.pojo.MessageListItem;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HomeListAdapter extends BaseAdapter {
    private List<MessageListItem> mItems;
    private final LayoutInflater mInflater;
    private Context gContext;

    public HomeListAdapter(Context context, List<MessageListItem> items) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItems = items;
        gContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MessageListItem getItem(int arg0) {
        return mItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //reuse existing view, if existing view is not present, create and fill the viewholder
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_listitem, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.homelistitem_image);
            holder.title = (TextView) convertView
                    .findViewById(R.id.homelistitem_title);
            holder.description = (TextView) convertView
                    .findViewById(R.id.homelistitem_description);
            holder.time = (TextView) convertView
                    .findViewById(R.id.homelistitem_time);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //get the item for the current position
        MessageListItem node = mItems.get(position);

        //fill the views in the viewholder with the content for the current position
        holder.title.setText(node.Title);
        holder.description.setText(node.Text);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = sdf.format(new Date(node.Timestamp*1000));
        holder.time.setText(time);

        Random rnd = new Random();
        int color = Color.argb(255, 255, 255, 255);
        holder.image.setBackgroundColor(color);
        if(node.ImageUrl != null) {
            Picasso.with(gContext).load(node.ImageUrl).into((android.widget.ImageView) holder.image);
        } else {
            Picasso.with(gContext).load("http://www.financiereguizot.com/wp-content/themes/twentyeleven/images/img-not-found_600_600.jpg").into((android.widget.ImageView) holder.image);
        }

        //save the viewholder in the view.
        convertView.setTag(holder);

        return convertView;
    }

    static class ViewHolder {

        TextView title;
        TextView description;
        ImageView image;
        TextView time;
    }
}
