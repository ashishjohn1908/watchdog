package com.app.watchdog.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ashish John on 28/2/19.
 */
public class AppPreference {

    private static AppPreference mPreferenceInstance;
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private String IS_WATCHDOG_INIT = "is_watchdog_init";
    private String APP_VERSION_CODE = "app_version_code";

    public AppPreference(Context context) {
        mContext = context;
        if (mContext != null) {
            mSharedPreferences = mContext.getSharedPreferences("WatchDogPreference", Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }
    }


    public static AppPreference getInstance(Context context) {
        if (mPreferenceInstance == null) {
            mPreferenceInstance = new AppPreference(context);
        }

        return mPreferenceInstance;
    }

    /**
     * This method will save true after WatchDog is initialised.
     *
     * @param isInit
     */
    public void setIsWatchdogInitialised(boolean isInit) {
        mEditor.putBoolean(IS_WATCHDOG_INIT, isInit);
        mEditor.commit();
    }

    public boolean getIsWatchdogInitialised() {
        return mSharedPreferences.getBoolean(IS_WATCHDOG_INIT, false);
    }

    /**
     * This method will save the current app version code,
     * so that on every new app install watchdog can be re-initialised.
     *
     * @param versionCode
     */
    public void setVersionCode(int versionCode) {
        mEditor.putInt(APP_VERSION_CODE, versionCode);
        mEditor.commit();
    }

    public int getAppVersionCode() {
        return mSharedPreferences.getInt(APP_VERSION_CODE, -1);
    }
}
