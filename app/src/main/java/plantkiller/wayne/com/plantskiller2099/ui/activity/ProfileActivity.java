package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import plantkiller.wayne.com.plantskiller2099.R;

public class ProfileActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        String value = intent.getStringExtra("name_2"); //if it's a string you stored.
        TextView textName = findViewById(R.id.text_name);
        TextView textID = findViewById(R.id.text_id);
        textName.setText("Name: " + value);
        if (value.equals("batman")) textID.setText("Secret ID: Lao Công");
        if (value.equals("superman")) textID.setText("Secret ID: Cộng Tác Viên");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
