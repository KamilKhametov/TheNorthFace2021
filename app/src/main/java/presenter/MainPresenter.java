package presenter;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import contract.MainPresenterInterface;
import contract.MainView;
import model.MyModel;

public class MainPresenter implements MainPresenterInterface {

    DatabaseReference reference;
    List<MyModel> modelList;
    MainView viewState;
    SharedPreferences preferences;
    FirebaseFirestore firestore;

    // Конструктор для экрана "Каталог"
    public MainPresenter(DatabaseReference reference, List<MyModel> modelList, MainView viewState, SharedPreferences preferences) {
        this.reference = reference;
        this.modelList = modelList;
        this.viewState = viewState;
        this.preferences = preferences;
    }

    // Констуктор для экрана "Избранное"
    public MainPresenter(List<MyModel> modelList, FirebaseFirestore firestore, MainView viewState) {
        this.viewState = viewState;
        this.modelList = modelList;
        this.firestore = firestore;
    }

    // Метод получения данных с Firebase
    @Override
    public void getDataFromFB(MyModel myModel) {
        // Покажи progressBar
        viewState.showProgressBar();
        reference = FirebaseDatabase.getInstance().getReference().child("ShopApp");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MyModel myModel = dataSnapshot.getValue(MyModel.class);
                    modelList.add(myModel);
                    // Убери progressBar
                    viewState.hideProgressBar();
                    viewState.applyCatalogData(modelList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Добавление данных в FirebaseFirestore
    public void insertDataToFirestore(String id, String image, String title, String desc, String price, String rating) {
        firestore = FirebaseFirestore.getInstance();

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("image", image);
        map.put("title", title);
        map.put("desc", desc);
        map.put("price", price);
        map.put("rating", rating);

        firestore.collection("TheNorthFaceCollection").document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            viewState.favoritesSuccess();
                        }
                    }
                }).addOnFailureListener(e -> {
            viewState.showError("Error");
        });
    }

    // Удаление данных с Firebase с обновлением для экрана "Избранное"
    public void deleteDataFromFirestoreWithUpdate(int position, String id, String image, String title, String desc, String price, String rating) {
        viewState.showProgressBar();
        firestore = FirebaseFirestore.getInstance();
        MyModel myModel = modelList.get(position);
        firestore.collection("TheNorthFaceCollection").document(myModel.getIdProd()).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getDataFromFirestore();
                    } else {
                        viewState.showError("Error");
                    }
                    viewState.hideProgressBar();
                });
    }

    // Удаление данных с Firebase
    public void deleteDataFromFirestore(int position, String id, String image, String title, String desc, String price, String rating) {
        firestore = FirebaseFirestore.getInstance();
        MyModel myModel = modelList.get(position);
        firestore.collection("TheNorthFaceCollection").document(myModel.getIdProd()).delete()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        viewState.showError("Error");
                    }
                });
    }


    // Получение данных с FirebaseFirestore
    @Override
    public void getDataFromFirestore() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("TheNorthFaceCollection").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            MyModel myModel = new MyModel(snapshot.getString("id"), snapshot.getString("image"), snapshot.getString("title"), snapshot.getString("desc"), snapshot.getString("price"), snapshot.getString("rating"));
                            modelList.add(myModel);
                            viewState.applyCatalogData(modelList);

                        }
                        viewState.hideProgressBar();
                        if(modelList.size() == 0){
                            viewState.hideProgressBar();
                        }


                    }
                }).addOnFailureListener(e -> {
            viewState.showError("Error");
        });
    }

}
