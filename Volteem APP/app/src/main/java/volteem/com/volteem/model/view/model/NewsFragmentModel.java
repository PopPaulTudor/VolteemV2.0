package volteem.com.volteem.model.view.model;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import volteem.com.volteem.model.entity.DataRetrieveException;
import volteem.com.volteem.model.entity.NewsMessage;

public class NewsFragmentModel {
    private ArrayList<NewsMessage> newsList;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private ModelCallback modelCallback;

    public NewsFragmentModel(ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void retrieveNewsList() {
        newsList = new ArrayList<>();
        mDatabase.child("news").orderByChild("receivedBy").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    newsList.add(dataSnapshot1.getValue(NewsMessage.class));
                }
                Collections.reverse(newsList);
                modelCallback.onDataRetrieved(newsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DataRetrieveException dataRetrieveException = new DataRetrieveException("unknown", databaseError.getMessage());
                modelCallback.onDataRetrieveFailed(dataRetrieveException);
            }
        });
    }

    public interface ModelCallback {
        void onDataRetrieved(ArrayList<NewsMessage> newsList);

        void onDataRetrieveFailed(DataRetrieveException dataRetrieveException);
    }
}
