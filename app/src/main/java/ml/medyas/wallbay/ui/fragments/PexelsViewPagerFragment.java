package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.pexels.PexelsCuratedViewModel;
import ml.medyas.wallbay.models.pexels.PexelsSearchViewModel;
import ml.medyas.wallbay.models.pexels.PexelsViewModel;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.FRAGMENT_POSITION;
import static ml.medyas.wallbay.utils.Utils.SEARCH_QUERY;

public class PexelsViewPagerFragment extends BaseFragment {
    private int position;
    private String query = "";


    public static Fragment newInstance(int position) {
        Fragment fragment = new PexelsViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int position, String query) {
        Fragment fragment = new PexelsViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        bundle.putString(SEARCH_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            position = getArguments().getInt(FRAGMENT_POSITION);
            if(getArguments().containsKey(SEARCH_QUERY)) {
                query = getArguments().getString(SEARCH_QUERY);
            }
        }

        setUpViewModel();
    }

    @Override
    public void setUpViewModel() {
        ViewModel mViewModel;

        if(position == 0) {
            mViewModel = ViewModelProviders.of(this, new PexelsViewModel.PexelsViewModelFactory(getContext())).get(PexelsViewModel.class);

            ((PexelsViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((PexelsViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
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
                            setSnackbar( Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
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

        } else if(position == 1) {
            mViewModel = ViewModelProviders.of(this, new PexelsCuratedViewModel.PexelsCuratedViewModelFactory(getContext())).get(PexelsCuratedViewModel.class);

            ((PexelsCuratedViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((PexelsCuratedViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
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
                            setSnackbar( Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
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

        } else {
            mViewModel = ViewModelProviders.of(this, new PexelsSearchViewModel.PexelsSearchViewModelFactory(getContext(), query)).get(PexelsViewModel.class);

            ((PexelsSearchViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((PexelsSearchViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
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
                            setSnackbar( Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
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

        }
    }
}
