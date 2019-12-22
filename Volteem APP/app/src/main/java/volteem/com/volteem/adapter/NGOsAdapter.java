package volteem.com.volteem.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.NGO;

public class NGOsAdapter extends RecyclerView.Adapter<NGOsAdapter.EventViewHolder> {

    private ArrayList<NGO> ngoList;
    private ActionListener.NGOAdapterListener ngoAdapterListener;

    public NGOsAdapter(ArrayList<NGO> ngoList, ActionListener.NGOAdapterListener ngoAdapterListener)
    {
        this.ngoAdapterListener= ngoAdapterListener;
        this.ngoList=ngoList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_ngo,parent,false);

        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, int position) {

        /*
        //todo add name to holder when we decide the fields of the NGO
          holder.cardName.setText(ngoList.get(position).getName());
          holder.cardRating.setText(ngoList.get(position).getRating());
          Glide.with(holder.cardImage).load(Uri.parse(ngoList.get(position).getImageUri())).centerCrop().listener(new RequestFutureTarget<Drawable>())
       */

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngoAdapterListener.onClickNGO(ngoList.get(holder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return ngoList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder
    {
        TextView cardName;
        TextView cardRating;
        ImageView cardImage;
        CardView cardView;


        EventViewHolder(View v) {
            super(v);

            cardName= v.findViewById(R.id.ngo_name);
            cardRating=v.findViewById(R.id.ngo_rating);
            cardImage= v.findViewById(R.id.ngo_image);
            cardView=v.findViewById(R.id.ngo_card_element);



        }

    }
}
