package com.example.pavlenko.twofragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

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
        Log.d("Activity", (replacer == null) + "");
        if (replacer == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, first)
                    .commit();

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

        public FragmentReplacer(MainActivity activity) {
            this.activity = activity;
        }

        public void attach(MainActivity activity) {
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
                    .replace(R.id.container, activity.second)
                    .commit();
        }
    }
}
