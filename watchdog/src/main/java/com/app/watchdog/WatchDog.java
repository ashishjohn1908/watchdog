package com.app.watchdog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.app.watchdog.data.AppPreference;

import org.json.JSONObject;

import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class WatchDog {

    public static String TAG = "WatchDog-Log:: ";
    private static Context mContext;
    private static String mApiKey;
    private static String minSdk = "", targetSdk = "", versionCode = "", versionName = "";
    private static String deviceName = "", isEmulator = "", timezone = "", locale = "", countryCode = "", currencyCode = "";

    public static void watch(Context context, String key) {
        mContext = context;
        mApiKey = key;
        getDeviceDetails();
        getAppDetails();

//        This check will prevent watchDog to create multiple entry when app open.
        if (!AppPreference.getInstance(mContext).getIsWatchdogInitialised())
            postData();
        else
            Log.v(TAG, "Watchdog already initialised");
    }

    private static void getDeviceDetails() {

        deviceName = getDeviceName();
        isEmulator = String.valueOf(isEmulator());
        timezone = getTimezoneDetails();
        locale = getLocale();
        countryCode = getCountryCode();
        if (!TextUtils.isEmpty(countryCode))
            currencyCode = getCurrencyCode(getCountryCode());
    }

    private static void getAppDetails() {

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                minSdk = String.valueOf(pInfo.applicationInfo.minSdkVersion);
                targetSdk = String.valueOf(pInfo.applicationInfo.targetSdkVersion);
                versionCode = String.valueOf(pInfo.versionCode);
                versionName = pInfo.versionName;

                if (AppPreference.getInstance(mContext).getAppVersionCode() != pInfo.versionCode) {
                    AppPreference.getInstance(mContext).setVersionCode(pInfo.versionCode);
                    AppPreference.getInstance(mContext).setIsWatchdogInitialised(false);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will return app install id in a device.
     * Install Id will be same app is opened or app is updated.
     * Install Id will be different when app is freshly installed.
     *
     * @return
     */
    private static String getAppInstallId() {
        return Installation.id(mContext);
    }

    /**
     * This method will retrieve currency code
     *
     * @param countryCode
     * @return
     */
    private static String getCurrencyCode(String countryCode) {
        String currencyCode = "";
        try {
            currencyCode = Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyCode;
    }

    /**
     * This method will return country code of the device
     *
     * @return
     */
    private static String getCountryCode() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null && tm.getNetworkCountryIso() != null)
            return tm.getNetworkCountryIso();

        return "";
    }

    /**
     * This method will return Locale used in the device
     *
     * @return
     */
    private static String getLocale() {
        return mContext.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * This method will return timezone of device
     *
     * @return
     */
    private static String getTimezoneDetails() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(true, TimeZone.SHORT) + " TimezoneId :: " + tz.getID();
    }

    /**
     * This method will return OS of device
     *
     * @return
     */
    private static String getDeviceOS() {
        return Build.VERSION.RELEASE;
    }


    /**
     * This method will provide device name with OS version
     *
     * @return
     */
    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String deviceName = "";
        if (model.startsWith(manufacturer)) {
            deviceName = capitalize(model);
        } else {
            deviceName = capitalize(manufacturer) + " " + model;
        }

        return deviceName + " (" + Build.VERSION.RELEASE + ")";
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * This method will tell if its a real device or emulator
     *
     * @return
     */
    private static boolean isEmulator() {
        return Build.FINGERPRINT.contains("generic");
    }


    private static void postData() {
        try {
            JSONObject deviceInfoObject = new JSONObject();
            deviceInfoObject.put("currency_code", currencyCode);
            deviceInfoObject.put("language_code", locale);
            deviceInfoObject.put("model", deviceName);
            deviceInfoObject.put("name", deviceName);
            deviceInfoObject.put("os_version", getDeviceOS());
            deviceInfoObject.put("region_code", countryCode);
            deviceInfoObject.put("time_zone_identifier", timezone);

            JSONObject dataObject = new JSONObject();
            dataObject.put("installation_uuid", getAppInstallId());
            dataObject.put("minimum_os_version", minSdk);
            dataObject.put("target_os_version", targetSdk);
            dataObject.put("name", mContext.getPackageName());
            dataObject.put("version", versionName);
            dataObject.put("build_number", versionCode);
            dataObject.put("bundle_identifier", mContext.getPackageName());
            dataObject.put("device_family", deviceName);
            dataObject.put("device_info", deviceInfoObject);

            ConnectionClass task = new ConnectionClass(mContext, dataObject.toString());
            task.execute(mApiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}