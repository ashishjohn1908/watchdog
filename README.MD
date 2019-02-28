# WatchDog
WatchDog is library that can be added in any project, so as to get basic device details in which the app is installed.

## How to use
1. Add these lines in your project level gradle file:
>allprojects {
		repositories {			
			maven { url 'https://jitpack.io' }
		}
	}

2. Add this in dependencies in you app level gradle:
	>*debugImplementation 'com.github.ashishjohn1908:watchdog:v1'*

3. Register your package name to get the key.

4. Pass context & appKey as parameter.
	>WatchDog.watch({context}, {key});

## Permissions Required
### uses-permission android:name="android.permission.INTERNET
