package com.victorylink.bakingapp.Interfaces.Component;

import com.victorylink.bakingapp.Interfaces.Scope.RecipeRecyclerFragmentScope;
import com.victorylink.bakingapp.Views.RecipeRecyclerFragment;
import com.victorylink.bakingapp.modules.RecipeRecyclerFragmentModule;

import dagger.Component;

/**
 * Created by aayman on 5/30/2017.
 */
@RecipeRecyclerFragmentScope
@Component(modules = RecipeRecyclerFragmentModule.class, dependencies = BakingAppComponent.class)

public interface RecipeRecyclerFragmentComponent {
    void injectRecipeRecyclerFragmentComponent(RecipeRecyclerFragment recipeRecyclerFragment);

}
