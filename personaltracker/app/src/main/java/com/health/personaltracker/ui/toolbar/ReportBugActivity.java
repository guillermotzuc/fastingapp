package com.health.personaltracker.ui.toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.health.personaltracker.MainActivity;
import com.health.personaltracker.R;

public class ReportBugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_report_bug);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_bug);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText txtContent = findViewById(R.id.report_edit_text);
        final Button sendReport = findViewById(R.id.btn_send_report);
        final SwitchCompat reportSwitch = findViewById(R.id.report_switch);

        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                String contactMail = getString(R.string.contact_mail);
                String subject = getString(reportSwitch.isChecked() ?
                        R.string.mail_subject_new_feature :
                        R.string.mail_subject_bug
                );
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ contactMail }); // Single recipient
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                // Adding HTML body
                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(txtContent.getText().toString(), Html.FROM_HTML_MODE_COMPACT));
                emailIntent.setType("text/html");
                startActivity(emailIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_bug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.report_bug_to_main) {
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