package cc.myandroid.baserecylerviewadapterdemo;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzaidong on 2017/7/10.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_FOOTER = 1;
    private static final int ITEM_TYPE_CONTENT = 2;

    private List<T> mDatas = new ArrayList<>();
    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemSelectedListener mOnItemSelectedListener;

    private View mHeaderView;
    private View mFooterView;
    private OnHeaderClickListener mOnHeaderClickListener;
    private OnFooterClickListener mOnFooterClickListener;
    private int mHeaderViewId = -1;
    private int mFooterViewId = -1;
     Context mContext;
     LayoutInflater mInflater;
    @Override
    public int getItemCount() {
        if (mHeaderViewId == -1 && mFooterViewId == -1) return mDatas.size();
        if (mHeaderViewId !=  -1 && mFooterViewId == -1|| mHeaderViewId == -1)
            return mDatas.size() + 1;
        else return mDatas.size() + 2;
    }

    public BaseAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderViewId != -1) return ITEM_TYPE_HEADER;
        if (position == getItemCount() - 1 && mFooterViewId != -1) return ITEM_TYPE_FOOTER;
        return ITEM_TYPE_CONTENT;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //设置itemSelected监听 即获取焦点监听
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(v, position);
                }
            }
        });
        //顶部布局点击监听
        if (getItemViewType(position) == ITEM_TYPE_HEADER && mOnHeaderClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnHeaderClickListener.onClick(v);
                }
            });
            return;
        }
        //底部布局点击监听
        if (getItemViewType(position) == ITEM_TYPE_FOOTER && mOnFooterClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFooterClickListener.onClick(v);
                }
            });
            return;
        }
        //内容布局
        final int realPosition = getRealPosition(holder);
        final T data = mDatas.get(realPosition);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, realPosition, data);
                }
            });
        }
        onBind(holder, realPosition, data);//统一处理
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER){
            mHeaderView = mInflater.inflate(mHeaderViewId,parent,false);
            return new Holder(mHeaderView);
        }
        if (viewType == ITEM_TYPE_FOOTER){
            mFooterView= mInflater.inflate(mFooterViewId,parent,false);
            return new Holder(mFooterView);
        }
        return onCreate(parent, viewType);
    }

    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);

    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, T data);

    public static class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    /*由于RecycleView支持LinearLayoutManager、GridLayoutManager、StaggeredGridLayoutManager，
    而GridLayoutManager、StaggeredGridLayoutManager在添加header时候需要注意横跨整个屏幕宽度即：
    GridLayoutManager 是要设置SpanSize每行的占位大小
    StaggerLayoutManager 就是要获取StaggerLayoutManager的LayoutParams 的setFullSpan 方法来设置占位宽度，
    因此在BaseAdapter中做了针对性处理
    */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == ITEM_TYPE_HEADER || getItemViewType(position) == ITEM_TYPE_FOOTER)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (getItemViewType(holder.getLayoutPosition()) == ITEM_TYPE_HEADER || getItemViewType(holder.getLayoutPosition()) == ITEM_TYPE_FOOTER)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public void setDatas(List<T> datas) {
        this.mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public interface OnFooterClickListener {
        void onClick(View view);
    }

    public interface OnHeaderClickListener {
        void onClick(View view);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T data);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener mOnItemSelectedListener) {
        this.mOnItemSelectedListener = mOnItemSelectedListener;
    }


    public void setOnHeaderClickListener(OnHeaderClickListener mOnHeaderClickListener) {
        this.mOnHeaderClickListener = mOnHeaderClickListener;
    }


    public void setOnFooterClickListener(OnFooterClickListener mOnFooterClickListener) {
        this.mOnFooterClickListener = mOnFooterClickListener;
    }


    public void setHeaderViewId(int resId) {
        this.mHeaderViewId = resId;
    }


    public void setFooterViewId(int  resId) {
        this.mFooterViewId = resId;
    }
}
