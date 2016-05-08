package org.dalol.slidewidget;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SliderView mSliderView;
    private TextView mSliderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSliderView = (SliderView) findViewById(R.id.sliderView);
        mSliderProgress = (TextView) findViewById(R.id.sliderProgress);

        mSliderView.setListener(new SliderView.SliderListener() {
            @Override
            public void onSlide(float progress, SliderState state) {
                mSliderProgress.setText(String.format(Locale.UK, "Progress: %.0f", progress));
                switch (state) {
                    case OPENED:
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAbout:
                showAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("About Developer")
                .setMessage("Filippo Engidashet")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
        dialog.show();
    }
}
