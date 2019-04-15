package volteem.com.volteem.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.Feedback;

public class ProfileEventAdapter extends RecyclerView.Adapter<ProfileEventAdapter.ProfileEventViewHolder> {

    private ArrayList<Event> events;
    private ArrayList<Feedback> feedbacks;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public ProfileEventAdapter(ArrayList<Event> events, ArrayList<Feedback> feedbacks) {
        this.events = events;
        this.feedbacks = feedbacks;
    }

    @NonNull
    @Override
    public ProfileEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_profile_event, parent, false);

        return new ProfileEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileEventViewHolder holder, int position) {

        holder.eventName.setText(events.get(position).getName());
        if (feedbacks != null) {
            for (Event event : events) {
                if (event.getEventID().equals(feedbacks.get(position).getEventId())) {
                    holder.eventFeedback.setText(feedbacks.get(position).getEventFeedback());
                    break;
                }
            }
        } else holder.eventFeedback.setText(R.string.profile_adapter_no_feedback);

        if (events.get(position).getEventID() != null) {
            storageRef.child("Photos").child("Event").child(events.get(position).getEventID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.eventImage).load(uri).centerCrop().into(holder.eventImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide.with(holder.eventImage).load(Uri.parse("android.resource://volteem.com.volteem/drawable/ic_profile_default")).into(holder.eventImage);

                }
            });
        } else
            Glide.with(holder.eventImage).load(Uri.parse("android.resource://volteem.com.volteem/drawable/ic_profile_default"))
                    .into(holder.eventImage);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ProfileEventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventFeedback;
        ImageView eventImage;

        ProfileEventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.profile_event_name);
            eventImage = itemView.findViewById(R.id.profile_event_image);
            eventFeedback = itemView.findViewById(R.id.profile_event_feedback);
        }
    }
}
