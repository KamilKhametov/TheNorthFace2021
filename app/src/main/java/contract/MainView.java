package contract;

import java.util.List;

import model.MyModel;

public interface MainView {

    void showProgressBar();
    void hideProgressBar();
    void applyCatalogData(List<MyModel> clothes);
    void showError(String message);
    void favoritesSuccess();
    void notifyRemove(int position);
}
