package volteem.com.volteem.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.util.CalendarUtils;
import volteem.com.volteem.util.VolteemUtils;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<NewsMessage> newsList;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ActionListener.NewsDeletedListener newsDeletedListener;
    private boolean itemWasLongClicked = false;

    public NewsAdapter(ArrayList<NewsMessage> newsList, ActionListener.NewsDeletedListener newsDeletedListener) {
        this.newsList = newsList;
        this.newsDeletedListener = newsDeletedListener;
    }

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_news, parent, false);

        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.NewsViewHolder holder, final int position) {

        final SharedPreferences prefs = VolteemUtils.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if (newsList.get(holder.getAdapterPosition()).isStarred()) {
            holder.starredIcon.setVisibility(View.VISIBLE);
        } else {
            holder.starredIcon.setVisibility(View.GONE);
        }
        switch (newsList.get(holder.getAdapterPosition()).getType()) {
            case ACCEPTED_TO_EVENT:
                holder.typeIcon.setImageResource(R.drawable.ic_checked);
                break;
            case RECEIVED_FEEDBACK:
                holder.typeIcon.setImageResource(R.drawable.ic_feedback_news);
                break;
            case EVENT_DELETED:
                holder.typeIcon.setImageResource(R.drawable.ic_delete);
                break;
            case VOLUNTEER_REGISTERED_TO_EVENT:
                holder.typeIcon.setImageResource(R.drawable.ic_checked);
                break;
            case VOLUNTEER_LEFT:
                holder.typeIcon.setImageResource(R.drawable.ic_delete);
                break;
            default:
                break;
        }

        holder.content.setText(newsList.get(holder.getAdapterPosition()).getContent());
        holder.time.setText(CalendarUtils.getNewsStringDateFromMM(newsList.get(holder.getAdapterPosition()).getTimestamp()));
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemWasLongClicked = true;
                if (newsList.get(holder.getAdapterPosition()).isStarred()) {
                    mDatabase.child("news/" + newsList.get(holder.getAdapterPosition()).getId() + "/starred").setValue(false);
                    holder.starredIcon.setVisibility(View.GONE);
                    newsList.get(holder.getAdapterPosition()).setStarred(false);
                } else {
                    mDatabase.child("news/" + newsList.get(holder.getAdapterPosition()).getId() + "/starred").setValue(true);
                    holder.starredIcon.setVisibility(View.VISIBLE);
                    newsList.get(holder.getAdapterPosition()).setStarred(true);
                }
                return false;
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///TODO: do something when we have actions to do, such that go to a certain activity
                switch (newsList.get(holder.getAdapterPosition()).getType()) {
                    case ACCEPTED_TO_EVENT:
                        break;
                    case RECEIVED_FEEDBACK:
                        break;
                    case EVENT_DELETED:
                        break;
                    case VOLUNTEER_REGISTERED_TO_EVENT:
                        break;
                    case VOLUNTEER_LEFT:
                        break;
                }
                if (!itemWasLongClicked) {
                    //unless it was long-clicked
                    //context.startActivity(intent);
                }
                itemWasLongClicked = false;
            }
        });
    }

    public ItemTouchHelper getItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mDatabase.child("news/" + newsList.get(position).getId()).setValue(null);
                newsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, newsList.size());
                if (newsList.isEmpty()) {
                    newsDeletedListener.onNewsDeleted();
                }
            }
        };
        return new ItemTouchHelper(simpleCallback);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView content, time;
        RelativeLayout item;
        ImageView typeIcon, starredIcon;

        NewsViewHolder(View v) {
            super(v);
            item = (RelativeLayout) v.findViewById(R.id.item_view);
            content = (TextView) v.findViewById(R.id.content);
            time = (TextView) v.findViewById(R.id.time);
            typeIcon = (ImageView) v.findViewById(R.id.news_icon);
            starredIcon = (ImageView) v.findViewById(R.id.starred_icon);
        }
    }
}
