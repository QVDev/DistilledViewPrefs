package distilledview.utils.qvdev.com.distilled;

import android.content.SharedPreferences;

/**
 * A preferences for distilled pages.  This allows users
 * to save the preferences set in the view.
 */
public class DistilledPagePrefs {

    public static final float DEFAULT_FONT_SCALE = 1.0f;
    public static final int DEFAULT_FONT_MODE = R.id.light_mode;
    public static final int DEFAULT_TYPE_FACE = 1;

    public static final String DISTILLED_PREF_USER_SET_FONT_SCALE = "user_font_scale";
    public static final String DISTILLED_PREF_USER_SET_FONT_MODE = "user_font_mode";
    public static final String DISTILLED_PREF_USER_SET_FONT_TYPEFACE = "user_font_typeface";

    private final SharedPreferences mSharedPreferences;

    public DistilledPagePrefs(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void setFontScale(float value) {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putFloat(DISTILLED_PREF_USER_SET_FONT_SCALE, value);
        sharedPreferencesEditor.apply();
    }

    public float getFontScale() {
        return mSharedPreferences.getFloat(DISTILLED_PREF_USER_SET_FONT_SCALE, DEFAULT_FONT_SCALE);
    }

    public void setFontMode(int value) {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putInt(DISTILLED_PREF_USER_SET_FONT_MODE, value);
        sharedPreferencesEditor.apply();
    }

    public int getBackgroundMode() {
        return mSharedPreferences.getInt(DISTILLED_PREF_USER_SET_FONT_MODE, DEFAULT_FONT_MODE);
    }

    public void setTypeFace(int value) {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putInt(DISTILLED_PREF_USER_SET_FONT_TYPEFACE, value);
        sharedPreferencesEditor.apply();
    }

    public int getTypeFace() {
        return mSharedPreferences.getInt(DISTILLED_PREF_USER_SET_FONT_TYPEFACE, DEFAULT_TYPE_FACE);
    }

}
