Map Wars
========

Android artillery game played on a Google Maps board.

TODO list can be found in the TODO.diff file.

# Project Setup

## Preparing the Environment

In this step, we're going to download and setup Android Development Tools and some essential packages.

1. Download **Android Developer Tools** here: http://developer.android.com/sdk/index.html.

2. Open ADT, go to Window -> Android SDK Manager.

3. Select and install the **latest version of the Android API** (as of 2014-02-14, it's Android 4.4.2 (API 19)).

4. Select and install the **Android Support Library**. It's located inside Extras -> Android Support Library.

5. Select and install the **Google Play Services** package. It's located inside Extras -> Google Play services.

## Importing Google Play Services into your workspace

Since the project depends on Google Play Services, you need to import it into your workspace and reference it in the project. If you already have the Google Play Services in your workspace, then you can skip these steps.

1. In ADT, go to File -> Import -> Android -> Existing Android Code into Workspace

2. In the "root directory" field, click "Browse" and select the following dir: <android-sdk>/extras/google/google_play_services/libproject/google-play-services_lib. The <android-sdk> dir is usually found inside the ADT package you downloaded.

3. Click finish. The Google Play Services project will be created inside your workspace.

## Installing the Map Wars project

1. Git clone or otherwise download the Map Wars repository into your computer.

2. In ADT, go to File -> Import -> General -> Existing Projects into Workspace.

3. In the "root directory" field, click "Browse" and select the folder: <map-wars-base-dir>/Map Wars

4. Click "Finish".

## Referencing Google Play Services in the project

The Map Wars project needs to know where the Google Play Services library is. To do that, follow these steps:

1. In ADT, right click the MapWars project and go to Properties.

2. In the left hand corner menu, select "Android".

3. In the "Library" section, click "Add...".

4. Select the Google Play Services project you previously imported (google-play-services_lib).

5. Click "Ok" to close the Project Properties.

6. This action will make Eclipse modify the <map-wars-base-dir>/Map Wars/project.properties file. Google advises for keeping this file in version control systems, but this is a change that should not have to be commited, as it'll have a different value on each computer. If you're using a version control system, **untrack the project.properties file to avoid generating unecessary commits**.

In git, you can run:
    git update-index --assume-unchanged Map\ Wars/project.properties

## Setting up the Maps API

Map Wars runs on Google Maps. To make Google Maps work, you need at least a debug API key. To get one, follow the steps below:

1. Open your terminal and enter the following command:

    keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

2. In the output, copy the SHA1 hash. We'll use it later.

3. Go to the Google API Console: https://code.google.com/apis/console/

4. Create a new Project. If it's your first one, it'll be named "API Project".

5. Click your API Project to open it and on the left menu, select "APIs & auth".

6. The "APIs" submenu will show you a list of Google APIs available. Find "Google Maps Android API v2" and change its status to "ON".

7. In the left submenu, go to "Credentials".

8. In the "Public API access" section, click "CREATE NEW KEY".

9. Paste your SHA1 hash copied on step 2 and add the package name at the end, separating them with a semicolon and then click "Update". Example:

XX:XX:XX:...:XX:XX;com.lucasdnd.mapwars

10. In the "Credentials" page, Google will now give you an API key. Copy it.

## Entering the API Key in the Project

1. In ADT, create a new file in <map-wars-base-dir>/Map Wars/res/values named "api-keys.xml".

2. In the "api-keys.xml" file, enter the following content:

    <?xml version="1.0" encoding="UTF-8"?>
    <resources>
        <string name="GoogleMapsKey">GOOGLE_MAPS_API_KEY_HERE</string>
    </resources>

3. Replace the string "GOOGLE_MAPS_API_KEY_HERE" with the API Key provided by Google in step 10 of the previous section.

4. This file is gitignored, so you don't have to worry about accidentally commiting it.

5. Run the Project.