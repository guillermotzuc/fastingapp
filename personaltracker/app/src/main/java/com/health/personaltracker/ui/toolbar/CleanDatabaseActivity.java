package com.health.personaltracker.ui.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.health.personaltracker.MainActivity;
import com.health.personaltracker.R;

public class CleanDatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_reset);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clean_database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clean_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clean_db_to_main) {
            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Bundle is optional
            Bundle bundle = new Bundle();
            bundle.putString("MyValue1", "val1");
            myIntent.putExtras(bundle);
            //end Bundle
            startActivity(myIntent);
        }
        return (super.onOptionsItemSelected(item));
    }
}