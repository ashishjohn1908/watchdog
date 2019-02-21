package com.app.watchdog.api;

import com.google.gson.annotations.SerializedName;

public class DataModel {

    public DataModel(String installationId, String minSdk, String targetSdk, String appName, String versionName,
                     String versionCode, String packageName, String deviceName, DeviceInfo deviceInfo) {

        this.installationId = installationId;
        this.minSdk = minSdk;
        this.targetSdk = targetSdk;
        this.appName = appName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.deviceName = deviceName;
        this.deviceInfo = deviceInfo;
    }

    @SerializedName("installation_uuid")
    public String installationId;

    @SerializedName("minimum_os_version")
    public String minSdk;

    @SerializedName("target_os_version")
    public String targetSdk;

    @SerializedName("name")
    public String appName;

    @SerializedName("version")
    public String versionName;

    @SerializedName("build_number")
    public String versionCode;

    @SerializedName("bundle_identifier")
    public String packageName;

    @SerializedName("device_family")
    public String deviceName;

    @SerializedName("device_info")
    public DeviceInfo deviceInfo;

    public static class DeviceInfo {

        public DeviceInfo(String currencyCode, String locale, String deviceModel, String deviceName, String deviceOsVersion,
                          String deviceRegion, String deviceTimezone) {

            this.currencyCode = currencyCode;
            this.locale = locale;
            this.deviceModel = deviceModel;
            this.deviceName = deviceName;
            this.deviceOsVersion = deviceOsVersion;
            this.deviceRegion = deviceRegion;
            this.deviceTimezone = deviceTimezone;
        }

        @SerializedName("currency_code")
        public String currencyCode;

        @SerializedName("language_code")
        public String locale;

        @SerializedName("model")
        public String deviceModel;

        @SerializedName("name")
        public String deviceName;

        @SerializedName("os_version")
        public String deviceOsVersion;

        @SerializedName("region_code")
        public String deviceRegion;

        @SerializedName("time_zone_identifier")
        public String deviceTimezone;

    }
}
