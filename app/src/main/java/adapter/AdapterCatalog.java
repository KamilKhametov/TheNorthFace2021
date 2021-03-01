package adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import contract.ProductEventListener;
import model.MyModel;


public class AdapterCatalog extends RecyclerView.Adapter<AdapterCatalog.ViewHolder> {

    private List<MyModel> modelList = new ArrayList<>();
    private SharedPreferences preferences;
    ProductEventListener listener;

    public AdapterCatalog(ProductEventListener listener) {
        this.listener = listener;
    }

    public void setData(List<MyModel> modelList) {
        this.modelList.clear();
        this.modelList.addAll(modelList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        // Инициализация SharedPreferences
        if (preferences == null) {
            preferences = parent.getContext().getSharedPreferences("NICE", Context.MODE_PRIVATE);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageProduct;
        private ImageView imageLike;
        private TextView titleProduct;
        private TextView descProduct;
        private TextView priceProduct;
        private RatingBar ratingProduct;

        MyModel currentModel;
        boolean like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            imageLike = itemView.findViewById(R.id.imageLike);
            titleProduct = itemView.findViewById(R.id.titleProduct);
            descProduct = itemView.findViewById(R.id.descProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            ratingProduct = itemView.findViewById(R.id.ratingBarProduct);

            // Происходит клик, если иконка закрашена, то убери раскраску
            // Иначе закрась
            // И сохрани состояние иконки
            imageLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (preferences.getBoolean(currentModel.getIdProd(), false)) {
                        imageLike.setImageResource(R.drawable.ic_unlike);
                        saveData(currentModel.getIdProd(), false);

                        // удаление товара с FirebaseFirestore
                        listener.deleteDataFromFirestore(getAdapterPosition(), currentModel.getIdProd(), currentModel.getImageProd(), currentModel.getTitleProd(), currentModel.getDescProd(), currentModel.getPriceProd(), currentModel.getRatingProd());

                    } else {
                        imageLike.setImageResource(R.drawable.ic_like);
                        saveData(currentModel.getIdProd(), true);

                        // Передача избранного товара на экран "Избранное"
                        String id = currentModel.getIdProd();
                        String image = currentModel.getImageProd();
                        String title = currentModel.getTitleProd();
                        String desc = currentModel.getDescProd();
                        String price = currentModel.getPriceProd();
                        String rating = currentModel.getRatingProd();

                        // Передача параметров товара в метод
                        listener.insertDataToFirestore(id, image, title, desc, price, rating);

                    }
                }
            });
        }

        // Метод привязки данных товара
        private void bind(MyModel myModel) {
            currentModel = myModel;
            // При закреплении элементов идет проверка на состояние иконки: закрашена или не закрашена
            like = preferences.getBoolean(currentModel.getIdProd(), true);

            // Получение url картинки и ее установка
            String imageURL = myModel.getImageProd();
            Picasso.with(itemView.getContext())
                    .load(imageURL)
                    .into(imageProduct);

            // Установка значений на виджеты activity
            titleProduct.setText(myModel.getTitleProd());
            descProduct.setText(myModel.getDescProd());
            priceProduct.setText(myModel.getPriceProd() + " ₽");
            ratingProduct.setRating(Float.parseFloat(myModel.getRatingProd()));

            // Как только заходим на активити все иконки пустые
            // Если до этого мы заходили и закрашивали иконки, то присвой им их состояние
            if (preferences.getBoolean(currentModel.getIdProd(), false)) {
                imageLike.setImageResource(R.drawable.ic_like);
            } else {
                imageLike.setImageResource(R.drawable.ic_unlike);
            }
        }
    }

    public void saveData(String id, boolean dataToSave) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(id, dataToSave);
        editor.apply();
    }

}
