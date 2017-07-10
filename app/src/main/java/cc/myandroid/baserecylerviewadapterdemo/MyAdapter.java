package cc.myandroid.baserecylerviewadapterdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chenzaidong on 2017/7/10.
 */

public class MyAdapter extends BaseAdapter<String> {

    public MyAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_layout, parent, false);
        return new MHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, String data) {
        MHolder holder = (MHolder) viewHolder;
        holder.textView.setText(data);
    }

    static class MHolder extends Holder {
        TextView textView;

        public MHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
