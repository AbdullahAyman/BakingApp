package com.victorylink.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Interfaces.BakingView;
import com.victorylink.bakingapp.Prefrences.BakingSharedPreference;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.utilities.BakingConstants;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aayman on 5/23/2017.
 */

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.BakingAdapterHolder> {

    Context mContext;
    ArrayList<BackingResponse> mBackingResponses;
    BakingView mBakingView;

    @Inject
    public BakingAdapter(Context context) {
        mContext = context;
        mBakingView = (BakingView) this.mContext;
    }

    public void swapAdapterData(ArrayList<BackingResponse> baking) {
        mBackingResponses = baking;
    }

    @Override
    public BakingAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.layout_card_view, parent, false);

        return new BakingAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BakingAdapterHolder holder, int position) {
        final BackingResponse backingResponse = mBackingResponses.get(position);
        Glide.with(mContext)
                .load(backingResponse.getImage())
                .centerCrop()
                .placeholder(R.drawable.cooking)
                .crossFade()
                .error(R.drawable.cooking)
                .into(holder.cardImage);
        holder.cardBakeName.setText(backingResponse.getName());
        holder.cardBakeDescription.setText(holder.serving + " " + backingResponse.getServings() + " " + holder.people);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBakingView.loadSelectedItem(backingResponse);
                BakingSharedPreference bakingSharedPreference = new BakingSharedPreference(mContext);
                bakingSharedPreference.saveStringToSharedPreference(BakingConstants.LAST_VISITED_ID, backingResponse.getId() + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBackingResponses.size();
    }

    class BakingAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_card)
        ImageView cardImage;
        @BindView(R.id.card_img_collapse)
        ImageView cardCollapseImage;

        @BindView(R.id.card_bake_name)
        TextView cardBakeName;
        @BindView(R.id.card_bake_description)
        TextView cardBakeDescription;

        @BindString(R.string.labelBakeServing)
        String serving;
        @BindString(R.string.labelPeople)
        String people;

        View mView;

        public BakingAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
