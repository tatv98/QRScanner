package com.tatv.tffqrscanner.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tatv.tffqrscanner.Entity.History;
import com.tatv.tffqrscanner.R;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    // Variables
    private List<History> historyList;
    private Context context;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }
    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder vh;
        if (rowView == null) {//lan dau bat len thi rowview se null
            LayoutInflater inflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflate.inflate(R.layout.history_item, null);
            vh = new ViewHolder();

            vh.date = rowView.findViewById(R.id.date);
            vh.textView =  rowView.findViewById(R.id.contextTextView);
            vh.search =  rowView.findViewById(R.id.searchImageView);
            vh.copy =  rowView.findViewById(R.id.copyImageView);
            vh.share =  rowView.findViewById(R.id.shareImageView);

            rowView.setTag(vh);//set du lieu tu viewHolder cho rowview
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textView.setText((position + 1) + ". " + historyList.get(position).getContext());
        vh.date.setText(historyList.get(position).getDate());
        vh.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if(Patterns.WEB_URL.matcher(historyList.get(position).getContext()).matches()) {
                    url = historyList.get(position).getContext();
                }else {
                    url = "http://www.google.com/#q=" + historyList.get(position).getContext();
                }
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
        vh.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", historyList.get(position).getContext());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Đã copy vào clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        vh.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = historyList.get(position).getContext();
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share "));

            }
        });

        return rowView;
    }

    public static class ViewHolder{
        public AppCompatTextView textView;
        public TextView  date;
        public ImageView search, copy, share;
    }
}
