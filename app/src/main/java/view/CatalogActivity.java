package view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterCatalog;
import contract.MainView;
import contract.ProductEventListener;
import model.MyModel;
import presenter.MainPresenter;

public class CatalogActivity extends AppCompatActivity implements MainView, ProductEventListener {

    // Объявление полей
    ProgressBar progressBar;
    RecyclerView recyclerView;

    AdapterCatalog adapterCatalog;

    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    MainPresenter mainPresenter;
    List<MyModel> modelList;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    MyModel myModel = new MyModel();
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Инициализация адаптера
        adapterCatalog = new AdapterCatalog(this);
        // FirebaseFirestore
        firestore = FirebaseFirestore.getInstance();
        //SharedPreferences
        preferences = getSharedPreferences("NICE", MODE_PRIVATE);
        // Database
        reference = FirebaseDatabase.getInstance().getReference().child("ShopApp");
        // RecyclerView and Adapter init
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setAdapter(adapterCatalog);
        modelList = new ArrayList<>();
    }

    // Получение данных и их установка
    @Override
    protected void onResume() {
        super.onResume();

        modelList.clear();
        // Установка MVP
        setupMVP();
        // Установка layout-manager
        setupView();
        // Показ данных
        getProduct();
    }

    public void setupMVP() {
        mainPresenter = new MainPresenter(reference, modelList, this, preferences);
    }

    public void setupView() {
        setActionBarTitleCatalog();
        recyclerView.setLayoutManager(layoutManager);
    }

    public void getProduct() {
        mainPresenter.getDataFromFB(myModel);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void applyCatalogData(List<MyModel> clothes) {
        adapterCatalog.setData(clothes);
    }

    @Override
    public void showError(String message) {
        // Ошибка
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void favoritesSuccess() {
        // Избранное успешное
        Toast.makeText(this, "Товар добавлен в корзину)", Toast.LENGTH_SHORT).show();
    }


    // Установка иконки "Корзина" на toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_in_catalog_act, menu);
        return true;
    }

    // Обработка нажатия на item в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fav_item) {
            Intent intent = new Intent(CatalogActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }
        return true;
    }

    // Закреп actionBar
    private void setActionBarTitleCatalog() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_catalog);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_line_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void insertDataToFirestore(String id, String image, String title, String desc, String price, String rating) {
        mainPresenter.insertDataToFirestore(id, image, title, desc, price, rating);
    }

    @Override
    public void deleteDataFromFirestore(int position, String id, String image, String title, String desc, String price, String rating) {
        mainPresenter.deleteDataFromFirestore(position, id, image, title, desc, price, rating);
    }

    @Override
    public void getDataFromFirestore() {

    }
}