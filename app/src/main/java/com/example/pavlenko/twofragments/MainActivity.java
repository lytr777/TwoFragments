package com.example.pavlenko.twofragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "mainFragment";

    FrameLayout container;
    FragmentManager fragmentManager;
    FragmentReplacer replacer;

    FirstFragment first;
    SecondFragment second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout) findViewById(R.id.container);

        first = new FirstFragment();
        second = new SecondFragment();

        fragmentManager = getSupportFragmentManager();
        replacer = (FragmentReplacer) getLastCustomNonConfigurationInstance();

        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG) == null)
            fragmentManager.beginTransaction()
                    .add(R.id.container, first, FRAGMENT_TAG)
                    .commit();

        if (replacer == null) {
            replacer = new FragmentReplacer(this);
            replacer.execute();
        } else
            replacer.attach(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return replacer;
    }

    public class FragmentReplacer extends AsyncTask<Void, Void, Boolean> {

        MainActivity activity;

        FragmentReplacer(MainActivity activity) {
            this.activity = activity;
        }

        void attach(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean resultCode) {
            activity.fragmentManager.beginTransaction()
                    .replace(R.id.container, activity.second, FRAGMENT_TAG)
                    .commit();
        }
    }
}
