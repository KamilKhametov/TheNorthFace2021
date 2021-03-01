package contract;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import adapter.AdapterCatalog;
import model.MyModel;

public interface MainPresenterInterface {

    // Realtime FB
    void getDataFromFB(MyModel myModel);
    // get data from firestore
    void getDataFromFirestore();
}
