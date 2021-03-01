package view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterCatalog;
import contract.MainView;
import contract.ProductEventListener;
import model.MyModel;
import presenter.MainPresenter;

public class FavoritesActivity extends AppCompatActivity implements ProductEventListener, MainView {

    ProgressBar progressBarFav;
    MainPresenter mainPresenter;
    FirebaseFirestore firestore;
    List<MyModel> list;
    AdapterCatalog adapterCatalog;
    RecyclerView recyclerFav;
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Закреп actionBar
        setActionBarTitleFavorites();

        // Инициализация полей
        progressBarFav = findViewById(R.id.progressBarFav);
        recyclerFav = findViewById(R.id.recyclerFav);
        recyclerFav.setHasFixedSize(true);
        recyclerFav.setLayoutManager(layoutManager);

        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapterCatalog = new AdapterCatalog(this);
        recyclerFav.setAdapter(adapterCatalog);

        // Инициализация MainPresenter
        mainPresenter = new MainPresenter(list, firestore, this);

        // Получение данных с FirebaseFirestore
        getDataFromFirestore();

    }

    private void setActionBarTitleFavorites() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_favorites);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_line_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void insertDataToFirestore(String id, String image, String title, String desc, String price, String rating) {
        mainPresenter.insertDataToFirestore(id, image, title, desc, price, rating);
    }

    @Override
    public void deleteDataFromFirestore(int position, String id, String image, String title, String desc, String price, String rating) {
        mainPresenter.deleteDataFromFirestoreWithUpdate(position, id, image, title, desc, price, rating);
    }

    @Override
    public void getDataFromFirestore() {
        mainPresenter.getDataFromFirestore();
    }

    @Override
    public void showProgressBar() {
        progressBarFav.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarFav.setVisibility(View.INVISIBLE);
    }

    @Override
    public void applyCatalogData(List<MyModel> clothes) {
        adapterCatalog.setData(clothes);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void favoritesSuccess() {
        Toast.makeText(this, "Товар добавлен в корзину)", Toast.LENGTH_SHORT).show();
    }
}