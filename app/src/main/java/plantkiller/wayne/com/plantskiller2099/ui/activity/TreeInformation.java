package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;

public class TreeInformation extends AppCompatActivity {
    private TextView mTextDes;
    private TextView mTextStatus;
    private ImageView mImageAva;
    private TreeData mTreeData;

    public static Intent getInstance(Context context, TreeData result) {
        Intent intent = new Intent(context, TreeInformation.class);
        intent.putExtra("EXTRA_NAME", result);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tree_information);
        initView();
        getData();
        initToolbar();
        setView();
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mTreeData.getTreeName());
        
        }

    public void getData() {
        Intent intent = getIntent();
        mTreeData = intent.getParcelableExtra("EXTRA_NAME");
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

    public void initView() {
        mTextDes = (TextView) findViewById(R.id.text_description);
        mTextStatus = (TextView) findViewById(R.id.text_status);
        mImageAva = (ImageView) findViewById(R.id.image_tree);
    }

    public void setView() {
        mTextDes.setText("description: " + mTreeData.getDes());
        mImageAva.setImageResource(R.drawable.tree);
        mImageAva.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(mTreeData.getStatus()==1) mTextStatus.setText("Status: All right");
        if(mTreeData.getStatus()==2) mTextStatus.setText("Status: Almost dead");
    }
}
