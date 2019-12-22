package volteem.com.volteem.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.User;
import volteem.com.volteem.util.CalendarUtils;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemUtils;

public class EventUsersAdapter extends RecyclerView.Adapter<EventUsersAdapter.EventViewHolder> {

    private ArrayList<User> usersList;
    private int mExpandedPosition = -1;
    private int mShortAnimTime;
    private EventUsersActionListener eventUsersActionListener;

    public EventUsersAdapter(ArrayList<User> usersList, EventUsersActionListener eventUsersActionListener) {
        this.usersList = usersList;
        this.eventUsersActionListener = eventUsersActionListener;
    }

    @NonNull
    @Override
    public EventUsersAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event_user, parent, false);

        return new EventUsersAdapter.EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventUsersAdapter.EventViewHolder holder, final int position) {

        mShortAnimTime = VolteemUtils.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        int age = CalendarUtils.getAgeFromBirthdate(usersList.get(position).getBirthDate());
        holder.userName.setText(usersList.get(position).getFirstName() + " " + usersList.get(position).getLastName());
        holder.userCity.setText("City: " + usersList.get(position).getCity());
        holder.userAge.setText("Age: " + age);
        holder.userEmail.setText("Email: " + usersList.get(position).geteMail());
        holder.userPhone.setText("Phone: " + usersList.get(position).getPhone());
        holder.userSecondaryInfo.setText(usersList.get(position).getExperience() + "");

        final boolean isExpanded = position == mExpandedPosition;
        if (isExpanded) {
            holder.expandableItem.setAlpha(0f);
            holder.expandableItem.setVisibility(View.VISIBLE);
            holder.expandableItem.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimTime)
                    .setListener(null);
        } else {
            holder.expandableItem.setVisibility(View.GONE);
        }
        holder.item.setActivated(isExpanded);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyDataSetChanged();
            }
        });


        holder.viewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                eventUsersActionListener.prepareViewForDisplayingFeedback();
               // databaseUtils.getUserFeedbackList();

               /* final ArrayList<String> feedback = new ArrayList<>();
                mDatabase.child("users/volunteers/" + userIDs.get(position) + "/feedback").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final long count = dataSnapshot.getChildrenCount();
                        if (count == 0) {
                            progressBar.setVisibility(View.GONE);
                            noFeedbackText.setVisibility(View.VISIBLE);
                        } else {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                final String feedbackText = dataSnapshot1.getValue().toString();
                                mDatabase.child("events/" + dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener
                                        () {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        ++counter;
                                        String eventName = dataSnapshot2.child("name").getValue().toString();
                                        feedback.add(eventName + ": " + feedbackText);
                                        if (counter == count) {
                                            progressBar.setVisibility(View.GONE);

                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, feedback);
                                            feedbackListView.setAdapter(adapter);
                                            feedbackListView.setVisibility(View.VISIBLE);

                                            counter = 0;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("EvVolAdaptOrg", databaseError.getMessage());
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("EvVolAdapterFeedback", databaseError.getMessage());
                    }
                });*/
            }
        });

        holder.acceptUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventUsersActionListener.onAcceptUserButtonPressed(usersList.get(holder.getAdapterPosition()));
            }
        });

        /*holder.kickUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View parentView = activity.getLayoutInflater().inflate(R.layout.kick_volunteer_alert_dialog, null);
                final RadioGroup radioGroup = (RadioGroup) parentView.findViewById(R.id.kick_volunteer_radio);
                final EditText otherText = (EditText) parentView.findViewById(R.id.feedback);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        if (radioGroup.getCheckedRadioButtonId() == R.id.radio_other) {
                            otherText.setVisibility(View.VISIBLE);
                        } else {
                            otherText.setVisibility(View.GONE);
                        }
                    }
                });

                final AlertDialog kickVolunteerDialog = new AlertDialog.Builder(context)
                        .setView(parentView)
                        .setCancelable(true)
                        .setTitle("Remove volunteer?")
                        .setMessage("Please tell us the reason why you want to remove this volunteer:")
                        .setPositiveButton("REMOVE", null)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                kickVolunteerDialog.show();
                kickVolunteerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedItemID = radioGroup.getCheckedRadioButtonId();
                        switch (selectedItemID) {
                            case R.id.not_respect_duties:
                                mDatabase.child("users/volunteers/" + userIDs.get(position) + "/feedback" + event.getEventID()).setValue("This" +
                                        " user has been kicked out from " + event.getName() + " for not respecting his duties.");
                                removeVolunteerFromEvent(position);
                                kickVolunteerDialog.dismiss();
                                break;
                            case R.id.inappropriate_behaviour:
                                mDatabase.child("users/volunteers/" + userIDs.get(position) + "/feedback" + event.getEventID()).setValue("This" +
                                        " user has been kicked out from " + event.getName() + " for having inappropriate behaviour.");
                                removeVolunteerFromEvent(position);
                                kickVolunteerDialog.dismiss();
                                break;
                            case R.id.cant_come:
                                removeVolunteerFromEvent(position);
                                kickVolunteerDialog.dismiss();
                                break;
                            case R.id.too_many_volunteer:
                                removeVolunteerFromEvent(position);
                                kickVolunteerDialog.dismiss();
                                break;
                            case R.id.radio_other:
                                String reason = otherText.getText().toString();
                                if (reason.isEmpty()) {
                                    Toast.makeText(context, "You've selected Other, please write the reason.", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDatabase.child("users/volunteers/" + userIDs.get(position) + "/feedback" + event.getEventID()).setValue("This" +
                                            " user has been kicked out from " + event.getName() + " with the feedback: \"" + reason + "\".");
                                    removeVolunteerFromEvent(position);
                                    kickVolunteerDialog.dismiss();
                                }
                                break;
                            default:
                                Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void acceptUser(User userToAccept, Event event) {
        DatabaseUtils.acceptVolunteer(userToAccept.getId(), event);
        usersList.remove(userToAccept);
        if(usersList.isEmpty()) {
            eventUsersActionListener.onAllVolunteersRemoved();
        }
        notifyDataSetChanged();
    }

    /*private void removeVolunteerFromEvent(int position) {
        mDatabase.child("events/" + event.getEventID() + "/users/" + userIDs.get(position)).setValue(null);
        String newsID = mDatabase.child("news").push().getKey();
        mDatabase.child("news/" + newsID).setValue(new NewsMessage(CalendarUtil.getCurrentTimeInMillis(), newsID,
                event.getEventID(), user.getUid(), userIDs.get(position), "You have been removed from the event " +
                event.getName(), NewsMessage.VOLUNTEER_LEFT, false, false));
        userIDs.remove(position);
        usersList.remove(position);
        if (userIDs.isEmpty()) {
            volunteersRemovedListener.onAllVolunteersRemoved();
        }
        notifyDataSetChanged();
    }*/

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userSecondaryInfo, userCity, userAge, userPhone,
                userEmail, secondaryInfoTextView;
        RelativeLayout item;
        RelativeLayout expandableItem;
        ImageView acceptUser, viewFeedback, kickUser;
        CircleImageView userImage;

        EventViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item_view);
            userName = itemView.findViewById(R.id.userName);
            userSecondaryInfo = itemView.findViewById(R.id.userSecondaryInfo);
            expandableItem = itemView.findViewById(R.id.expandable_item);
            userCity = itemView.findViewById(R.id.userCity);
            userAge = itemView.findViewById(R.id.userAge);
            userPhone = itemView.findViewById(R.id.userPhone);
            userEmail = itemView.findViewById(R.id.userEmail);
            acceptUser = itemView.findViewById(R.id.acceptVolunteer);
            viewFeedback = itemView.findViewById(R.id.viewFeedback);
            kickUser = itemView.findViewById(R.id.kickUser);
            secondaryInfoTextView = itemView.findViewById(R.id.secondaryInfoTextView);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }

    public interface EventUsersActionListener {
        void onAcceptUserButtonPressed(User userToAccept);

        void prepareViewForDisplayingFeedback();

        void onAllVolunteersRemoved();
    }
}
