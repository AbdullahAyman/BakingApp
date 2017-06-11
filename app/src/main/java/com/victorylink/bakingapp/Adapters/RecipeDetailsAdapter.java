package com.victorylink.bakingapp.Adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.DataModel.Ingredient;
import com.victorylink.bakingapp.DataModel.Step;
import com.victorylink.bakingapp.Interfaces.ActionTaken;
import com.victorylink.bakingapp.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aayman on 5/24/2017.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.VHHeader> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context mContext;
    BackingResponse mBackingResponses;
    ActionTaken mBakingView;

    @Inject
    public RecipeDetailsAdapter(Context context) {
        mContext = context;
        mBakingView = (ActionTaken) this.mContext;
    }

    public void swapAdapterData(BackingResponse baking) {
        mBackingResponses = baking;
    }

    @Override
    public VHHeader onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_recipe, parent, false);

        return new VHHeader(itemView);

    }

    @Override
    public void onBindViewHolder(VHHeader holder, int position) {
        if (position == 0) {
            assignHeaderData(holder);
        } else {
            Step dataItem = getItem(position);
            holder.title.setText(dataItem.getShortDescription());
            final int newPos = position - 1;
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBakingView.loadSelectedItem(mBackingResponses, newPos);
                }
            });
            holder.collapseDescription.setVisibility(View.GONE);
            holder.ingredientsDescription.setVisibility(View.GONE);

        }
    }

    private void assignHeaderData(final VHHeader holder) {
        float degree = holder.collapseDescription.getRotation();
        if (degree == 180.0f || degree == 360.0f)
            holder.ingredientsDescription.setVisibility(View.VISIBLE);
        holder.collapseDescription.setVisibility(View.VISIBLE);
        holder.title.setText(mContext.getResources().getString(R.string.recipeIngredients));
      /*  holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
        holder.collapseDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ingredientsDescription.isShown()) {
                    YoYo.with(Techniques.ZoomOutUp)
                            .duration(300)
                            .repeat(1)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    holder.ingredientsDescription.setVisibility(View.GONE);
                                    holder.collapseDescription.setRotation(0);
                                }
                            })
                            .playOn(holder.ingredientsDescription);

                    return;
                }
                String mStringBuilder = "";
                List<Ingredient> ingredientList = getIngredients();
                for (int i = 0; i < ingredientList.size(); i++) {
                    Ingredient mIngredient = ingredientList.get(i);
                    mStringBuilder = mStringBuilder + "# " + mIngredient.getQuantity() + " " + mIngredient.getMeasure() + " " + holder.labelOf + " " + mIngredient.getIngredient() + "\n";
                }
                holder.ingredientsDescription.setVisibility(View.VISIBLE);
                holder.collapseDescription.setRotation(180);
                holder.ingredientsDescription.setText(mStringBuilder);
                YoYo.with(Techniques.ZoomInDown)
                        .duration(300)
                        .repeat(1)
                        .playOn(holder.ingredientsDescription);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBackingResponses.getSteps().size() + 1;
    }

    private Step getItem(int position) {
        return mBackingResponses.getSteps().get(position - 1);
    }

    private List<Ingredient> getIngredients() {
        return mBackingResponses.getIngredients();
    }

    class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.card_bake_name)
        TextView title;
        @BindView(R.id.card_img_collapse)
        ImageView collapseDescription;
        @BindView(R.id.card_bake_description)
        TextView ingredientsDescription;
        @BindString(R.string.labelOf)
        String labelOf;
        View mView;

        public VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;

        }
    }
}