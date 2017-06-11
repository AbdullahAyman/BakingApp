package com.victorylink.bakingapp.Interfaces.Component;

import com.victorylink.bakingapp.Interfaces.Scope.BakingRecyclerFragmentViewScope;
import com.victorylink.bakingapp.Views.BakingRecyclerFragmentView;
import com.victorylink.bakingapp.modules.BakingRecyclerFragmentViewModule;

import dagger.Component;

/**
 * Created by aayman on 5/29/2017.
 */
@BakingRecyclerFragmentViewScope
@Component(modules = BakingRecyclerFragmentViewModule.class, dependencies = BakingAppComponent.class)
/**
 * Use dependencies = BakingAppComponent.class when we need to link between to component to share data
 * */
public interface BakingRecyclerFragmentViewComponent {
    /**
     * use reference to BakingRecyclerFragmentView not to the Activity class
     */
    void injectBakingRecyclerFragmentViewComponent(BakingRecyclerFragmentView bakingRecyclerFragmentView);

}
