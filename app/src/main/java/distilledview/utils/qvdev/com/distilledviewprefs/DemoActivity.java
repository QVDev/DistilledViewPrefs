package distilledview.utils.qvdev.com.distilledviewprefs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import distilledview.utils.qvdev.com.distilled.DistilledPagePrefs;
import distilledview.utils.qvdev.com.distilled.DistilledPagePrefsView;

public class DemoActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final float DEFAULT_FONT_SIZE = 16.0f;
    private SharedPreferences mSharedPreferences;
    private TextView mDemoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        initTextView();
    }

    private void initTextView() {
        mDemoTextView = (TextView) findViewById(R.id.example_text_view);
        setTextViewSizeWithScale(mSharedPreferences.getFloat(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_SCALE, DistilledPagePrefs.DEFAULT_FONT_SCALE));
        setTextViewMode(mSharedPreferences.getInt(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_MODE, DistilledPagePrefs.DEFAULT_FONT_MODE));
        setTextViewTypeFace(mSharedPreferences.getInt(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_TYPEFACE, DistilledPagePrefs.DEFAULT_TYPE_FACE));
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

    public void showDistilledDialog() {
        DistilledPagePrefsView.showDialog(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String preferenceName) {
        if (preferenceName.contentEquals(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_SCALE)) {
            float newValue = sharedPreferences.getFloat(preferenceName, 1);
            setTextViewSizeWithScale(newValue);
        } else if (preferenceName.contentEquals(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_MODE)) {
            setTextViewMode(sharedPreferences.getInt(preferenceName, DistilledPagePrefs.DEFAULT_FONT_MODE));
        } else if (preferenceName.contentEquals(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_TYPEFACE)) {
            setTextViewTypeFace(sharedPreferences.getInt(preferenceName, DistilledPagePrefs.DEFAULT_TYPE_FACE));
        }
    }

    private void setTextViewSizeWithScale(float value) {
        mDemoTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_FONT_SIZE * value);
    }

    private void setTextViewMode(int mode) {
        mDemoTextView.setBackgroundResource(DistilledPagePrefsView.getBackgroundColorFromMode(mode));
        mDemoTextView.setTextColor(getResources().getColor(DistilledPagePrefsView.getTexColorFromMode(mode)));
    }

    private void setTextViewTypeFace(int typeFace) {
        mDemoTextView.setTypeface(DistilledPagePrefsView.getTypeFaceForPosition(typeFace));
    }
}
