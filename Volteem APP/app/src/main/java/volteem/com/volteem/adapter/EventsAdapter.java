package volteem.com.volteem.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.SelectedEventsCategory;
import volteem.com.volteem.util.CalendarUtils;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter
        .EventViewHolder> {

    private ArrayList<Event> eventsList;
    private ActionListener.EventAdapterListener eventAdapterListener;
    private SelectedEventsCategory flag;
    private boolean wasUIActivated = false;

    public EventsAdapter(ArrayList<Event> eventsList, ActionListener.EventAdapterListener eventAdapterListener, SelectedEventsCategory flag) {
        this.eventsList = eventsList;
        this.eventAdapterListener = eventAdapterListener;
        this.flag = flag;
    }

    @NonNull
    @Override
    public EventsAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event, parent, false);
        return new EventViewHolder(v);
    } ///TODO: implement adapter for OWN EVENTS, VOLUNTEER_REGISTERED_TO_EVENT EVENTS

    @Override
    public void onBindViewHolder(@NonNull final EventsAdapter.EventViewHolder holder, final int
            position) {
        /* Different layouts will be provided for different flags */
        holder.cardName.setText(eventsList.get(position).getName());
        holder.cardLocation.setText(eventsList.get(position).getLocation());
        if(flag == SelectedEventsCategory.UNREGISTERED_EVENTS) { ///If the user is seeing the unregistered events, the deadline is displayed
            holder.cardDate.setText(CalendarUtils.getStringDateFromMM(eventsList.get(holder.getAdapterPosition()).getDeadline()));
        } else {
            holder.cardDate.setText(CalendarUtils.getNewsStringDateFromMM(eventsList.get(holder.getAdapterPosition()).getStartDate()));
        }
        Glide.with(holder.cardImage).load(Uri.parse(eventsList.get(position).getImageUri())).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (!wasUIActivated && (position == 2 || position ==
                                eventsList.size() - 1)) {
                            if (eventAdapterListener != null) {
                                wasUIActivated = true;
                                eventAdapterListener.onPicturesLoaded();
                            }
                        }
                        return false;
                    }
                })
                .into(holder.cardImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAdapterListener.onClickEvent(eventsList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView cardName;
        TextView cardDate;
        TextView cardLocation;
        ImageView cardImage;
        CardView cardView;

        EventViewHolder(View v) {
            super(v);

            cardName = v.findViewById(R.id.event_name);
            cardDate = v.findViewById(R.id.event_start_date);
            cardLocation = v.findViewById(R.id.event_location);
            cardImage = v.findViewById(R.id.event_image);
            cardView = v.findViewById(R.id.card_element);
        }
    }
}
