package distilledview.utils.qvdev.com.distilledviewprefs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import distilledview.utils.qvdev.com.distilled.DistilledPagePrefsView;

public class DemoActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mDemoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        initTextViewWithPreferedOptions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                showDistilledDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initTextViewWithPreferedOptions() {
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        mDemoTextView = (TextView) findViewById(R.id.example_text_view);
        restorePreferedTextViewOptions();
    }

    private void restorePreferedTextViewOptions() {
        DistilledPagePrefsView.restorePreferedOptions(mDemoTextView);
    }

    private void showDistilledDialog() {
        DistilledPagePrefsView.showDialog(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String preferenceName) {
        DistilledPagePrefsView.applyToTextView(mDemoTextView, sharedPreferences, preferenceName);
    }
}
