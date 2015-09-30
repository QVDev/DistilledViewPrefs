package distilledview.utils.qvdev.com.distilled;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * A view which displays preferences for distilled pages.  This allows users
 * to change the theme, font size, etc. of distilled pages.
 */
public class DistilledPagePrefsView extends LinearLayout implements SeekBar.OnSeekBarChangeListener {

    private static final float DEFAULT_FONT_SIZE = 16.0f;

    private enum STYLE {
        LIGHT,
        DARK,
        SEPIA
    }

    // XML layout for View.
    private static final int VIEW_LAYOUT = R.layout.distilled_page_prefs_view;

    // RadioGroup for color mode buttons.
    private RadioGroup mRadioGroup;

    // Text field showing font scale percentage.
    private TextView mFontScaleTextView;

    // SeekBar for font scale. Has range of [0, 30].
    private SeekBar mFontScaleSeekBar;

    // Spinner for choosing a font family.
    private Spinner mFontFamilySpinner;

    private final NumberFormat mPercentageFormatter;

    private Map<STYLE, RadioButton> mColorModeButtons;

    private DistilledPagePrefs mDistilledPagePrefs;

    /**
     * Creates a DistilledPagePrefsView.
     *
     * @param context Context for acquiring resources.
     * @param attrs   Attributes from the XML layout inflation.
     */
    public DistilledPagePrefsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPercentageFormatter = NumberFormat.getPercentInstance(Locale.getDefault());
        mColorModeButtons = new EnumMap<>(STYLE.class);
    }

    public static DistilledPagePrefsView create(Context context) {
        return (DistilledPagePrefsView) LayoutInflater.from(context)
                .inflate(VIEW_LAYOUT, null);
    }

    public static void showDialog(Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Base_Theme_AppCompat_Dialog_Alert);
        builder.setView(DistilledPagePrefsView.create(context));
        builder.show();
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        mDistilledPagePrefs = new DistilledPagePrefs(PreferenceManager.getDefaultSharedPreferences(getContext()));

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_button_group);

        mColorModeButtons.put(STYLE.LIGHT, initializeAndGetButton(R.id.light_mode));
        mColorModeButtons.put(STYLE.DARK, initializeAndGetButton(R.id.dark_mode));
        mColorModeButtons.put(STYLE.SEPIA, initializeAndGetButton(R.id.sepia_mode));

        mRadioGroup.check(mDistilledPagePrefs.getBackgroundMode());

        mFontScaleSeekBar = (SeekBar) findViewById(R.id.font_size);
        mFontScaleTextView = (TextView) findViewById(R.id.font_size_percentage);

        mFontFamilySpinner = (Spinner) findViewById(R.id.font_family);
        initFontFamilySpinner();


        // Setting initial progress on font scale seekbar.
        mFontScaleSeekBar.setOnSeekBarChangeListener(this);
        setFontScaleProgress(mDistilledPagePrefs.getFontScale());
    }

    private void initFontFamilySpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(
                R.array.distiller_mode_font_family_values)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return overrideTypeFace(view, position);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return overrideTypeFace(view, position);
            }

            private View overrideTypeFace(View view, int position) {
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;

                    Typeface typeface = getTypeFaceForPosition(position);
                    textView.setTypeface(typeface);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.distilled_page_font_family_spinner);
        mFontFamilySpinner.setAdapter(adapter);
        mFontFamilySpinner.setSelection(mDistilledPagePrefs.getTypeFace());

        mFontFamilySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFontFamilySpinner.setSelection(position);
                mDistilledPagePrefs.setTypeFace(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do.
            }
        });
    }

    public static Typeface getTypeFaceForPosition(int position) {
        switch (position) {
            case 1:
                return Typeface.SERIF;
            case 2:
                return Typeface.SANS_SERIF;
            default:
                return Typeface.MONOSPACE;
        }
    }

    public static int getBackgroundColorFromMode(int mode) {
        if (mode == R.id.dark_mode) {
            return R.color.dark;
        } else if (mode == R.id.sepia_mode) {
            return R.color.sepia;
        } else {
            return R.color.light;
        }
    }

    public static int getTexColorFromMode(int mode) {
        if (mode == R.id.dark_mode) {
            return R.color.light;
        } else if (mode == R.id.sepia_mode) {
            return R.color.light;
        } else {
            return R.color.dark;
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRadioGroup.setOrientation(HORIZONTAL);

        for (RadioButton button : mColorModeButtons.values()) {
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            layoutParams.width = 0;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If text is wider than button, change layout so that buttons are stacked on
        // top of each other.
        for (RadioButton button : mColorModeButtons.values()) {
            if (button.getLineCount() > 1) {
                mRadioGroup.setOrientation(VERTICAL);
                for (RadioButton innerLoopButton : mColorModeButtons.values()) {
                    ViewGroup.LayoutParams layoutParams = innerLoopButton.getLayoutParams();
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                }
                break;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // progress = [0, 30]
        // newValue = .50, .55, .60, ..., setFontScaleProgress1.95, 2.00 (supported font scales)
        float newValue = (progress / 20f + .5f);
        setFontScaleTextView(newValue);
        mDistilledPagePrefs.setFontScale(newValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    /**
     * Initiatializes a Button and selects it if it corresponds to the current
     * theme.
     */
    private RadioButton initializeAndGetButton(final int mode) {
        final RadioButton button = (RadioButton) findViewById(mode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDistilledPagePrefs.setFontMode(mode);
            }
        });
        return button;
    }

    /**
     * Sets the progress of mFontScaleSeekBar.
     */
    private void setFontScaleProgress(float newValue) {
        // newValue = .50, .55, .60, ..., 1.95, 2.00 (supported font scales)
        // progress = [0, 30]
        int progress = (int) ((newValue - .5) * 20);
        mFontScaleSeekBar.setProgress(progress);

    }

    /**
     * Sets the text for the font scale text view.
     */
    private void setFontScaleTextView(float newValue) {
        mFontScaleTextView.setText(mPercentageFormatter.format(newValue));
    }

    public static void restorePreferedOptions(TextView textView) {
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(textView.getContext());

        setTextViewSizeWithScale(textView, defaultPreferences.getFloat(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_SCALE, DistilledPagePrefs.DEFAULT_FONT_SCALE));
        setTextViewMode(textView, defaultPreferences.getInt(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_MODE, DistilledPagePrefs.DEFAULT_FONT_MODE));
        setTextViewTypeFace(textView, defaultPreferences.getInt(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_TYPEFACE, DistilledPagePrefs.DEFAULT_TYPE_FACE));
    }

    public static void applyToTextView(TextView textView, SharedPreferences sharedPreferences, String preferenceName) {
        if (preferenceName.contentEquals(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_SCALE)) {
            float newValue = sharedPreferences.getFloat(preferenceName, 1);
            setTextViewSizeWithScale(textView, newValue);
        } else if (preferenceName.contentEquals(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_MODE)) {
            setTextViewMode(textView, sharedPreferences.getInt(preferenceName, DistilledPagePrefs.DEFAULT_FONT_MODE));
        } else if (preferenceName.contentEquals(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_TYPEFACE)) {
            setTextViewTypeFace(textView, sharedPreferences.getInt(preferenceName, DistilledPagePrefs.DEFAULT_TYPE_FACE));
        }
    }

    private static void setTextViewSizeWithScale(TextView textView, float value) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FONT_SIZE * value);
    }

    private static void setTextViewMode(TextView textView, int mode) {
        textView.setBackgroundResource(DistilledPagePrefsView.getBackgroundColorFromMode(mode));
        textView.setTextColor(textView.getResources().getColor(DistilledPagePrefsView.getTexColorFromMode(mode)));
    }

    private static void setTextViewTypeFace(TextView textView, int typeFace) {
        textView.setTypeface(DistilledPagePrefsView.getTypeFaceForPosition(typeFace));
    }
}