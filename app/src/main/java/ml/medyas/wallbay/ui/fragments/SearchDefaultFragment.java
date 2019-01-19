package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.search.SearchViewModel;
import ml.medyas.wallbay.models.search.SearchViewModelFactory;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.SEARCH_QUERY;

public class SearchDefaultFragment extends BaseFragment {
    private String query = "";

    public static Fragment newInstance(String q) {
        Fragment fragment = new SearchDefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY, q);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            query = getArguments().getString(SEARCH_QUERY);
        }

        setUpViewModel();
    }

    @Override
    public void setUpViewModel() {
        SearchViewModel mViewModel = ViewModelProviders.of(this, new SearchViewModelFactory(query)).get(SearchViewModel.class);

        mViewModel.getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
            @Override
            public void onChanged(@Nullable Utils.NetworkState networkState) {
                if (networkState == Utils.NetworkState.LOADED) {
                    getItemLoad().setVisibility(View.GONE);
                    getRecyclerView().setVisibility(View.VISIBLE);

                } else if (networkState == Utils.NetworkState.EMPTY) {
                    Toast.makeText(getContext(), "Error retrieving more data!", Toast.LENGTH_SHORT).show();

                } else if (networkState == Utils.NetworkState.FAILED) {
                    if (getAdapter().getCurrentList() == null ||getAdapter().getCurrentList().size() == 0) {
                        getNetError().setVisibility(View.VISIBLE);
                        getItemLoad().setVisibility(View.GONE);
                        setSnackbar(Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getNetError().setVisibility(View.GONE);
                                        //mListener.reCreateFragment(ForYouFragment.newInstance());
                                    }
                                }));
                        getSnackbar().show();
                    } else {
                        Snackbar.make(getNetError(), "Failed to load more data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //mListener.reCreateFragment(ForYouFragment.newInstance());
                            }
                        }).show();
                    }
                }
            }
        });

        mViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                getAdapter().submitList(imageEntities);
            }
        });
    }
}