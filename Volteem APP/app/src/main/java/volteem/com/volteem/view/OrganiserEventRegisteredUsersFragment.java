package volteem.com.volteem.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.adapter.EventUsersAdapter;
import volteem.com.volteem.model.entity.User;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.OrganiserEventRegisteredUsersFragmentPresenter;

public class OrganiserEventRegisteredUsersFragment extends Fragment implements OrganiserEventRegisteredUsersFragmentPresenter.View, EventUsersAdapter.EventUsersActionListener {

    private RecyclerView mRegisteredUsersRecView;
    private ProgressBar progressBar;
    private TextView noVolunteersText;
    private OrganiserEventRegisteredUsersFragmentPresenter presenter;
    EventUsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organiser_event_registered_users, container, false);

        presenter = new OrganiserEventRegisteredUsersFragmentPresenter(getArguments(), this);
        progressBar = view.findViewById(R.id.indeterminateBar);
        noVolunteersText = view.findViewById(R.id.no_volunteers);
        mRegisteredUsersRecView = view.findViewById(R.id.RecViewRegUsers);
        mRegisteredUsersRecView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onRetrieveUsersListSuccessful(ArrayList<User> registeredUsers) {
        adapter = new EventUsersAdapter(registeredUsers, this);
        mRegisteredUsersRecView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRegisteredUsersRecView.setLayoutManager(linearLayoutManager);
        progressBar.setVisibility(View.GONE);
        if (registeredUsers.isEmpty()) {
            noVolunteersText.setVisibility(View.VISIBLE);
        } else {
            noVolunteersText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRetrieveUsersListFailed(VolteemCommonException exception) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptUserButtonPressed(final User userToAccept) {
        AlertDialog acceptUserDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Accept volunteer")
                .setMessage("Are you sure you want to accept this volunteer?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.acceptUser(userToAccept, presenter.getEvent());
                        Toast.makeText(getActivity(), "Accepted volunteer!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        acceptUserDialog.show();
    }

    @Override
    public void prepareViewForDisplayingFeedback() {
        final View parentView = getLayoutInflater().inflate(R.layout.volunteer_feedback_alert_dialog, null);
        final ProgressBar progressBar = parentView.findViewById(R.id.progressBar);
        final ListView feedbackListView = parentView.findViewById(R.id.feedback_list);
        final TextView noFeedbackText =  parentView.findViewById(R.id.no_feedback_text);
        final AlertDialog feedbackDialog = new AlertDialog.Builder(getActivity())
                .setView(parentView)
                .setTitle("Feedback")
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        feedbackDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        feedbackListView.setVisibility(View.GONE);
        noFeedbackText.setVisibility(View.GONE);
    }

    @Override
    public void onAllVolunteersRemoved() {
        noVolunteersText.setVisibility(View.VISIBLE);
    }
}
