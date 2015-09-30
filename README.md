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
    compile 'distilledview.utils.qvdev.com.distilled:distilledview-lib:1.1'
}
```

Register for preference changes. Take the default as the library does as well
```java
//Let your class implement
implements SharedPreferences.OnSharedPreferenceChangeListener

//Register for listener and set text view defaults
private void initTextViewWithPreferedOptions() {                	

//Register
PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

//Get the text view
mDemoTextView = (TextView) findViewById(R.id.example_text_view);

//Restore
restorePreferedTextViewOptions();
}

private void restorePreferedTextViewOptions() {   
//Call the DistilledPagePrefsView to restore stored values
DistilledPagePrefsView.restorePreferedOptions(mDemoTextView);

}
```

And now let the preferences do the magic
```java
@Override
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String preferenceName) {

DistilledPagePrefsView.applyToTextView(mDemoTextView, sharedPreferences, preferenceName);

}

```
And last but not least call the dialog where you want it via
```java
public void showDistilledDialog() {
    DistilledPagePrefsView.showDialog(this);
}
```
