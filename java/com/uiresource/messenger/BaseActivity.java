package com.uiresource.messenger;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Dytstudio.
 */

public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title;
    public final void changeTitle(int toolbarId, String titlePage){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(titlePage);
        getSupportActionBar().setTitle("");
    }
    public final void setupToolbar(int toolbarId, String titlePage){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(titlePage);

        getSupportActionBar().setTitle("");
    }
    public void setupToolbarWithUpNav(int toolbarId, String titlePage, @DrawableRes int res){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(titlePage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(res);
        getSupportActionBar().setTitle("");
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
