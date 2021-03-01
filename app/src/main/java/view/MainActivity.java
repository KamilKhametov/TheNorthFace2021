package view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shopapp.R;

public class MainActivity extends AppCompatActivity {

    private Button btnGoCatalog;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        // Закреп actionBar
        setActionBarTitleMain ();

        // Нахождение полей
        btnGoCatalog = findViewById ( R.id.btnGoCatalog );

        btnGoCatalog.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent (MainActivity.this, CatalogActivity.class);
                startActivity ( intent );
            }
        } );
    }

    private void setActionBarTitleMain() {
        getSupportActionBar ().setDisplayOptions ( ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar ().setCustomView ( R.layout.action_bar_main );
    }

}