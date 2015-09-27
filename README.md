# DistilledViewPrefs
Dialog for Android TextView to improve readability. Mostly used for applications where the main focus is to read. Spin-off from the blog https://medium.com/@qvdev/improving-readability-3baa2d943936

Please feel free to post feedback on any topic. Improvements, code related or suggestions. Fork it if you feel for it as well. :)

# Screens
![Alt text](/screens/dialog_open_default.png "The dialog")
![Alt text](/screens/dialog_open_with_text.png "Choose Font")

# Implementation example
Add this to your gradle build file
```java
repositories {
    maven { url "https://github.com/QVDev/DistilledViewPrefs/raw/master/releases/" }
}

dependencies {
    compile 'distilledview.utils.qvdev.com.distilled:distilledview-lib:1.0'
}
```

Register for preference changes. Take the default as the library does as well
```java
//Let your class implement
implements SharedPreferences.OnSharedPreferenceChangeListener

//Register for it
mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
```

Then initialize the TextView
```java
    private void initTextView() {
        mDemoTextView = (TextView) findViewById(R.id.example_text_view);
        setTextViewSizeWithScale(mSharedPreferences.getFloat(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_SCALE, DistilledPagePrefs.DEFAULT_FONT_SCALE));
        setTextViewMode(mSharedPreferences.getInt(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_MODE, DistilledPagePrefs.DEFAULT_FONT_MODE));
        setTextViewTypeFace(mSharedPreferences.getInt(DistilledPagePrefs.DISTILLED_PREF_USER_SET_FONT_TYPEFACE, DistilledPagePrefs.DEFAULT_TYPE_FACE));
    }
```

And now the listeners including example to change the TextView
```java
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
```
And last but not least call the dialog where you want it via
```java
public void showDistilledDialog() {
        DistilledPagePrefsView.showDialog(this);
    }
```
