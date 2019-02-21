package com.app.watchdog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.watchdog.api.ApiServices;
import com.app.watchdog.api.DataModel;
import com.app.watchdog.utils.Installation;

import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchDog {

    private static String TAG = "WatchDog-Log:: ";
    private static Context mContext;
    private static DataModel.DeviceInfo deviceInfo;
    private static DataModel dataModel;
    private static String mApiKey;

    public static void Watch(Context context, String key) {
        mContext = context;
        mApiKey = key;
        getDeviceDetails();
        getAppDetails();
        postData();
    }

    private static void getDeviceDetails() {

        Log.v(TAG + "DeviceName", getDeviceName());
        Log.v(TAG + "Is Emulator", String.valueOf(isEmulator()));
        Log.v(TAG + "Timezone", getTimezoneDetails());
        Log.v(TAG + "Locale", getLocale());
        Log.v(TAG + "CountryCode", getCountryCode());
        Log.v(TAG + "CurrencyCode", getCurrencyCode(getCountryCode()));

        deviceInfo = new DataModel.DeviceInfo(
                getCurrencyCode(getCountryCode()),
                getLocale(),
                getDeviceName(),
                getDeviceName(),
                getDeviceOS(),
                getCountryCode(),
                getTimezoneDetails());
    }

    private static void getAppDetails() {

        String packageName = mContext.getPackageName();
        Log.v(TAG + "PackageName", packageName);

        Log.v(TAG + "AppInstallationId", getAppInstallId());
        String minSdk = "", targetSdk = "", versionCode = "", versionName = "";

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);

            minSdk = String.valueOf(pInfo.applicationInfo.minSdkVersion);
            Log.v(TAG + "MinSdkVersion", minSdk);

            targetSdk = String.valueOf(pInfo.applicationInfo.targetSdkVersion);
            Log.v(TAG + "MaxSdkVersion", targetSdk);

            versionCode = String.valueOf(pInfo.versionCode);
            Log.v(TAG + "VersionCode", versionCode);

            versionName = pInfo.versionName;
            Log.v(TAG + "VersionName", versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dataModel = (new DataModel(getAppInstallId(),
                minSdk,
                targetSdk,
                packageName,
                versionName,
                versionCode,
                packageName,
                getDeviceName(),
                deviceInfo));
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
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
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
        Call<DataModel> call = ApiServices.getRetrofitService().postDeviceInfo(dataModel, mApiKey);
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                Log.v(TAG, String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {

            }
        });
    }
}