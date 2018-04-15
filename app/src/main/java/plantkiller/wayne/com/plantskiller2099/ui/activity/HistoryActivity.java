package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import plantkiller.wayne.com.plantskiller2099.R;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButton_1;
    private Button mButton_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mButton_1 = findViewById(R.id.tv24);
        mButton_2 = findViewById(R.id.tv34);
        mButton_1.setOnClickListener(this);
        mButton_2.setOnClickListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");

    }

    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(this, SampleMap.class);
        switch (v.getId()) {
            case R.id.tv24:
                startActivity(myIntent);
                break;
            case R.id.tv34:
                startActivity(myIntent);
                break;

        }

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
