package ml.medyas.wallbay.adapters.unsplash;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private UnsplashRepository unsplashRepository;
    private MutableLiveData<Utils.NetworkState> networkState;
    private String orderBy;

    public UnsplashDataSource(Context context, String orderBy) {
        this.unsplashRepository = new UnsplashRepository(context);
        networkState = new MutableLiveData<>();
        this.orderBy = orderBy;
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getPhotos(orderBy, 1).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashPhotoEntity>> call, Response<List<UnsplashPhotoEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (UnsplashPhotoEntity item : response.body()) {
                        ImageEntity imageEntity = new ImageEntity(item.getId(),
                                item.getUser().getUsername(),
                                item.getUser().getProfileImage().getMedium(),
                                Utils.webSite.UNSPLASH,
                                item.getLikes(),
                                0,
                                0,
                                item.getWidth(),
                                item.getHeight(),
                                item.getLinks().getHtml(),
                                item.getUrls().getRaw(),
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                "");
                        list.add(imageEntity);
                    }
                    callback.onResult(list, null, 2);
                    networkState.postValue(Utils.NetworkState.LOADED);

                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);

                    networkState.postValue(Utils.NetworkState.EMPTY);
                    callback.onResult(list, null, null);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);

                networkState.postValue(Utils.NetworkState.FAILED);
                callback.onResult(list, null, null);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImageEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getPhotos(orderBy, params.key).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashPhotoEntity>> call, Response<List<UnsplashPhotoEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (UnsplashPhotoEntity item : response.body()) {
                        ImageEntity imageEntity = new ImageEntity(item.getId(),
                                item.getUser().getUsername(),
                                item.getUser().getProfileImage().getMedium(),
                                Utils.webSite.UNSPLASH,
                                item.getLikes(),
                                0,
                                0,
                                item.getWidth(),
                                item.getHeight(),
                                item.getLinks().getHtml(),
                                item.getUrls().getRaw(),
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                "");
                        list.add(imageEntity);
                    }
                    callback.onResult(list, params.key+1);
                    networkState.postValue(Utils.NetworkState.LOADED);

                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);

                    networkState.postValue(Utils.NetworkState.EMPTY);
                    callback.onResult(list, null);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);

                networkState.postValue(Utils.NetworkState.FAILED);
                callback.onResult(list, null);
            }
        });
    }
}
