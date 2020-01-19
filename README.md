# Agora Android Application

## Readme

_Android Application for Agora Web that uses [Agora](https://gitlab.com/aossie/Agora/): An Electronic Voting Library implemented in Scala. This application uses [Agora Web API](https://gitlab.com/aossie/Agora-Web) as backend application_

This project is created using [JavaSE](https://www.oracle.com/technetwork/java/javase/downloads/index.html) on [Android Studio](https://developer.android.com/studio).

To run the development environment for this frontend, you need [Git](https://git-scm.com/) installed.

## Table of contents

- [Agora-Web Frontend](#agora-web-frontend)
    - [Readme](#readme)
    - [Table of contents](#table-of-contents)
    - [Installation](#installation)
    - [Running the application](#running-the-application)
    - [Troubleshooting your local environment](#troubleshooting-your-local-environment)
    - [Setting up appetize.io](#setting-up-appetize.io)
    - [Best Practices](#best-practices)
    - [Some Don'ts](#some-donts)
    - [Further Reading / Useful Links](#further-reading-useful-links)



## Installation

Make sure you have the latest version of [Android Studio](https://developer.android.com/studio) and [JavaSE](https://www.oracle.com/technetwork/java/javase/downloads/index.html) installed. We strongly recommend you to update all the libraries and sync the project(might take a minute).



## Running the application
To use Facebook login you will need to change "XXXXXXXXXXXXXXX" with your app id in app/src/main/res/values/string.xml
    here
    ```  <string name="facebook_app_id">XXXXXXXXXXXXXXX</string> 
    ``` and here
    ```
         <string name="fb_login_protocol_scheme">fbXXXXXXXXXXXXXXX</string>
    ```

**Note:** Don't forget to add "fb" before your fb_login_protocol_scheme. 


## Troubleshooting your local environment

Always `git pull` and get the latest from master. [Google](https://www.google.com) and [Stackoverflow](https://stackoverflow.com/) are your friends. You can find answers for most technical problems there. If you run into problems you can't resolve, feel free to open an issue.

## Setting up appetize.io

Follow these steps to deploy your app to appetize.io:-

1. Get an API token from here: https://appetize.io/docs#request-api-token.   
2. Create a CI/CD variable for api token named "APPETIZE_API".     
    Follow this guide to learn how to add CI/CD variables to your gitlab repository: https://docs.gitlab.com/ee/ci/variables/#creating-a-custom-environment-variable  
3. Run the following command once to upload the app.    
    ```curl https://APITOKEN@api.appetize.io/v1/apps -F "file=@file_to_upload.apk" -F "platform=android"```    
    Replace API_TOKEN with the api token you got in step 1.  
    Replace file_to_upload.apk with your apk file.   
4. Command in step 3 will return a response. Note the public key from your response and add  a CI/CD varible named "APPETIZE_KEY" and enter this public key as value.  
    Make sure to make both the varibales protected and make your branch protected too. Follow this guide: https://docs.gitlab.com/ee/user/project/protected_branches.html#configuring-protected-branches  
    
    This is a one time setup, subsequent changes you make in your repository will be reflected in your link you got in the response automatically.  

## Best practices

1. Try to do a root cause analysis for the issue, if applicable.
2. Reference the issue being fixed in the corresponding PR.
3. Use meaningful commit messages in a PR.
4. Use one commit per task. Do not over commit (add unnecessary commits for a single task) or under commit (merge 2 or more tasks in one commit).
5. Add screenshot/short video in case the changes made in the PR, are being reflected in the UI of the application.
6. Close the issue as soon as the corresponding PR is accepted/closed.
  

## Some don'ts

1. Send a MR without an existing issue.
2. Fix an issue assigned to somebody else and submit a PR before the assignee does.
3. Report issues which are previously reported by others. (Please check both the open and closed issues).
4. Suggest unnecessary or completely new features in the issue list.
5. Add unnecessary spacing or indentation to the code.


## Further Reading / Useful Links

* [Retrofit](https://square.github.io/retrofit/)-A type-safe HTTP client for Android and Java.
* [Material Design For Android](https://developer.android.com/guide/topics/ui/look-and-feel)-Material design is a comprehensive guide for visual, motion, and interaction design across platforms and devices.
* [Shimmer For Android](https://facebook.github.io/shimmer-android/)-Shimmer is an Android library that provides an easy way to add a shimmer effect to any view in your Android app.
* [gson](https://github.com/google/gson)-A Java serialization/deserialization library to convert Java Objects into JSON and back.
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)-  Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
* [Recycler View](https://developer.android.com/guide/topics/ui/layout/recyclerview)- To display a scrolling list of elements based on large data sets (or data that frequently changes).
* [Card View](https://developer.android.com/guide/topics/ui/layout/cardview)- To create card views.
* [Design support](https://developer.android.com/topic/libraries/support-library/packages)- The Android Support Library contains several library packages that can be included in your application. Each of these libraries supports a specific range of Android platform versions and set of features.
* [Pie Chart](https://github.com/PhilJay/MPAndroidChart)- A powerful & easy to use chart library for Android.
* [Facebook Login](https://developers.facebook.com/docs/facebook-login/)- To implement User login using social media account.
* [Load Toast](https://github.com/code-mc/loadtoast)-Pretty material design toasts with feedback animations.


