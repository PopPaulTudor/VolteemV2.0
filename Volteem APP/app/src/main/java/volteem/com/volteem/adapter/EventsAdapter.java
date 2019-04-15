package volteem.com.volteem.adapter;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.util.CalendarUtils;
import volteem.com.volteem.util.VolteemUtils;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter
        .EventViewHolder> {

    private ArrayList<Event> eventsList;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private ArrayList<Uri> imageList = new ArrayList<>();
    private ActionListener.EventAdapterListener eventAdapterListener;
    private boolean wasUIActivated = false;
    private Resources resources = VolteemUtils.getContext().getResources();

    public EventsAdapter(ArrayList<Event> eventsList, ActionListener.EventAdapterListener eventAdapterListener) {
        this.eventsList = eventsList;
        this.eventAdapterListener = eventAdapterListener;
    }

    @NonNull
    @Override
    public EventsAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventsAdapter.EventViewHolder holder, final int
            position) {

        holder.cardName.setText(eventsList.get(position).getName());
        holder.cardLocation.setText(eventsList.get(position).getLocation());
        holder.cardDate.setText(CalendarUtils.getStringDateFromMM(eventsList.get(holder.getAdapterPosition()).getDeadline()));
        populateUriList();
        Uri uri = imageUris.get(eventsList.get(holder.getAdapterPosition()).getType().ordinal());
        Picasso.get().load(uri).fit().centerCrop().into(holder.cardImage);
        imageList.add(holder.getAdapterPosition(), uri);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("Photos").child("Event").child(eventsList.get(position).getEventID())
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uri = task.getResult();
                    imageList.add(holder.getAdapterPosition(), uri);
                    Log.w(eventsList.get(holder.getAdapterPosition()).getName(), " has an image.");
                    Picasso.get().load(uri).fit().centerCrop().into(holder
                            .cardImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (!wasUIActivated && (holder.getAdapterPosition() == 2 || holder.getAdapterPosition() ==
                                    eventsList.size() - 1)) {
                                if (eventAdapterListener != null) {
                                    wasUIActivated = true;
                                    eventAdapterListener.onPicturesLoaded();
                                    //TODO aici se apeleaza metoda pt animatie daca ultimul event are poza
                                }
                            }
                        }

                        @Override
                        public void onError(Exception ex) {
                            // TODO handle
                        }
                    });
                } else {
                    Log.w(eventsList.get(holder.getAdapterPosition()).getName(), " doesn't have an image.");
                    if (!wasUIActivated && (holder.getAdapterPosition() == 2 || holder.getAdapterPosition() == eventsList
                            .size() - 1)) {
                        if (eventAdapterListener != null) {
                            wasUIActivated = true;
                            eventAdapterListener.onPicturesLoaded();
                            //TODO aici se apeleaza metoda pt animatie daca ultimul event nu are poza
                        }
                    }
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAdapterListener.onClickEvent(eventsList.get(holder.getAdapterPosition()), imageList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    private Uri parseUri(int ID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + resources.getResourcePackageName(ID)
                + '/' + resources.getResourceTypeName(ID) + '/' + resources.getResourceEntryName
                (ID));
    }

    private void populateUriList() {
        imageUris.add(parseUri(R.drawable.image_sports));
        imageUris.add(parseUri(R.drawable.image_music));
        imageUris.add(parseUri(R.drawable.image_festival));
        imageUris.add(parseUri(R.drawable.image_charity));
        imageUris.add(parseUri(R.drawable.image_training));
        imageUris.add(parseUri(R.drawable.image_other));
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
