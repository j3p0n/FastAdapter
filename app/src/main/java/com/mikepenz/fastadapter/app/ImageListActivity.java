package com.mikepenz.fastadapter.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.app.dummy.ImageDummyData;
import com.mikepenz.fastadapter.app.items.ImageItem;
import com.mikepenz.materialize.MaterializeBuilder;

public class ImageListActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter<ImageItem> fastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.sample_image_list);

        //style our ui
        new MaterializeBuilder().withActivity(this).build();

        //create our FastAdapter which will manage everything
        fastItemAdapter = new FastItemAdapter<>();

        //configure our fastAdapter
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ImageItem>() {
            @Override
            public boolean onClick(View v, IAdapter<ImageItem> adapter, ImageItem item, int position) {
                Toast.makeText(v.getContext(), item.mName, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //get our recyclerView and do basic setup
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        //find out how many columns we display
        int columns = getResources().getInteger(R.integer.wall_splash_columns);
        if (columns == 1) {
            //linearLayoutManager for one column
            rv.setLayoutManager(new LinearLayoutManager(this));
        } else {
            //gridLayoutManager for more than one column ;)
            rv.setLayoutManager(new GridLayoutManager(this, columns));
        }
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(fastItemAdapter);

        //fill with some sample data
        fastItemAdapter.add(ImageDummyData.getImages(mOnLovedClickListener));

        //restore selections (this has to be done after the items were added
        fastItemAdapter.withSavedInstanceState(savedInstanceState);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    private ImageItem.OnItemClickListener mOnLovedClickListener = new ImageItem.OnItemClickListener() {
        @Override
        public void onLovedClick(String image, boolean starred) {
            Toast.makeText(ImageListActivity.this, image + " - " + starred, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundel
        outState = fastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
