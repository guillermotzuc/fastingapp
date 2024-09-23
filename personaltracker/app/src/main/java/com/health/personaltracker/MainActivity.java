package com.health.personaltracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.health.personaltracker.databinding.ActivityMainBinding;
import com.health.personaltracker.toolbar.actions.AnalyticsActivity;
import com.health.personaltracker.toolbar.actions.CleanDatabaseActivity;
import com.health.personaltracker.toolbar.actions.DailyReflectionActivity;
import com.health.personaltracker.toolbar.actions.ReportBugActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Bundle is optional
        Bundle bundle = new Bundle();

        if (item.getItemId() == R.id.main_to_analytics) {
            Intent myIntent = new Intent(this, AnalyticsActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            bundle.putString("activity_title", "Analytics");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }

        if (item.getItemId() == R.id.main_to_daily_reflection) {
            Intent myIntent = new Intent(this, DailyReflectionActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            bundle.putString("activity_title", "Reflexion Diaria");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }

        if (item.getItemId() == R.id.main_to_report_bug) {
            Intent myIntent = new Intent(this, ReportBugActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            bundle.putString("activity_title", "Reportar error/Sugerir funcionalidad");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }

        if (item.getItemId() == R.id.main_to_reset) {
            Intent myIntent = new Intent(this, CleanDatabaseActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            bundle.putString("activity_title", "Limpiar registros");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }
        return (super.onOptionsItemSelected(item));
    }
}