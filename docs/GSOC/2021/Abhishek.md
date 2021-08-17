# Agora Vote Android

## Student Name: Abhishek Agarwal ([@abhi165](https://gitlab.com/abhi165))

## Organization: [AOSSIE](https://aossie.gitlab.io/)

## [Agora Android](https://gitlab.com/aossie/agora-android/)

## [Source Code](https://gitlab.com/aossie/agora-android/-/tree/gsoc-2021)

## Mentors: [Prudhvi Reddy](https://github.com/prudhvir3ddy), [Ritesh Agrawal](https://github.com/Ritesh-Ag), [T Thuvarakan](https://github.com/Thuva4)

## APK: [release.apk]()


### Overview of the Application

Android Application for Agora Web that uses Agora: An Electronic Voting Library implemented in Scala. This application uses Agora Web API as backend application.


### Goals

I had identified the following tasks in the project at the starting of the project.

1.  Making Data class for sending and receiving from and to api - **Done**
2.  Adding Header Interceptor.- **Done**
3.  Build Signed apk in the pipeline .- **Done**
4.  Changing the observer to viewlifecycleowner instead of activity - fragments .- **Done**
5.  Implementing Fastlane.- **Done**
6.  Replacing multiple interfaces with a single Kotlin generic class for communicating from a viewmodel.- **Done**
7.  Replacing nested livedata observers. **Done**
8.  Replacing explicitly declared Dispatchers while doing db operations . **Done**
9.  Migrating to new kotlin code style. **Done**
10. Implementation of firebase crashlytics, fcm. **Done**
11. Adding Badges and screenshots in README.md file **Done**
12. Enabling R8. **Done**
13. Adding biometric security while opening the app and adding on/off functionality in settings. **Done**
14. Migrate to kotlin new code style. **Done**



### Summary of whole GSoC journey

As I was already familiar with the codebase, I started with setting up the initial boilerplate code and the architecture for smooth flow of work. Firstly, I started making data classes for the different api responses.

Coding period begins. Firstly, I started replacing the json objects that were created manually with the data classes. After completely shifting to data classes, I added header interceptor in order to add header to the api calls and also implemented fastlane and modified the existing pipeline to build signed apk in the  pipeline itself in order to make the deployment process smooth. I also worked on some important fixes like changing the livedata observer to viewlifecycleowner instead of activity in order to prevent livedata leaks,removing nested observer with mediator live data and replacing multiple interfaces with a single response class in order to establish communication between a viewmodel and an activity or a fragment.

During the second coding period, I firstly removed all the explicitly declared dispatchers while doing db operations and migrated to kotlin new code style. After that I added a base fragment in order to observe network connectivity in realtime and notify user about it. To make the final apk smaller in size, I enabled R8 in the project. I also implemented firebase crashlytics,firebase messaging and added badges as well as some screenshots in README.md file.  In the end, started with the documentation the project overview.

I would love to thank my mentors especially **Prudhvi Reddy, Ritesh Agrawal, and Thuvarakan Tharmarajasingam** for always providing me constant support, they were always there for guiding me to follow better practices and suggested some very good enhancements and features for the project. Special thanks to **Prudhvi** for broadening my knowledge in android domain and sharing some cool stuffs and also for all the immediate merges and being so helpful and **Ritesh** for being so responsive whenever I faced a challenge.

### Merge Requests

1. [Merge Request !1](https://gitlab.com/aossie/agora-android/-/merge_requests/373)- Making Data class and adding Header Interceptor  (**Merged**)
* Added kotlin data class and the Moshi library for sending and receiving data during api calls instead of converting data to json manually
* Added a HeaderInterceptor for adding auth token to the api calls
* Updated Test cases

2. [Merge Request !2](https://gitlab.com/aossie/agora-android/-/merge_requests/377)- Fixed app crash while tapping on account settings   (**Merged**)
* Added functionality to load avatar from a url in  Profile Frgment
* Made Some Extension Function For frequently used codes

3. [Merge Request !3](https://gitlab.com/aossie/agora-android/-/merge_requests/378)- Changing the observer to viewlifecycleowner   (**Merged**)
* Changed livedata observer from activity to viewlifecycleowner in ActiveElectionsFragment,FinishedElectionsFragment,PendingElectionsFragment,BallotFragment,ResultFragment,VotersFragment

4. [Merge Request !4](https://gitlab.com/aossie/agora-android/-/merge_requests/379)- Build Signed apk in the pipeline   (**Merged**)
* Updated gitlab-ci.yml file in order to make signed apk in the pipeline
* Updated CONTRIBUTING.md
* Updated build.gradle (app level) to read credentials required for making signed apk
* Updated .gitignore file

5. [Merge Request !5](https://gitlab.com/aossie/agora-android/-/merge_requests/382)-Implementing Fastlane   (**Merged**)
* Changed gitlabci.yml
* Added Gemfile,Fastfile,Appfile

6. [Merge Request !6](https://gitlab.com/aossie/agora-android/-/merge_requests/383)- Replacing multiple interfaces with a single Kotlin generic class   (**Merged**)
* Removed all the interfaces that were being used only for communication between viewmodel and activity
* Made a common class ResponseUI.kt in order to send states and data from viewmodel

7. [Merge Request !7](https://gitlab.com/aossie/agora-android/-/merge_requests/387)- Replacing nested livedata observers   (**Merged**)
* Replaced nested live data observer with mediator live data.
* Changed HomeFragment.kt,HomeViewmodel.kt and fragment_home.xml

8. [Merge Request !8](https://gitlab.com/aossie/agora-android/-/merge_requests/391)- Removed explicitly declared Dispatchers   (**Merged**)
* Removed explicitly declared Dispatchers in various functions in ElectionRepository.kt

9. [Merge Request !9](https://gitlab.com/aossie/agora-android/-/merge_requests/392)- Migrated to new kotlin code style   (**Merged**)
* Updated Project.xml

10. [Merge Request !10](https://gitlab.com/aossie/agora-android/-/merge_requests/395)- Added Badges and screenshots to README.md file   (**Merged**)
* Added badges and screenshots in README.md

11. [Merge Request !11](https://gitlab.com/aossie/agora-android/-/merge_requests/397)- Added firebase crashlytics   (**Merged**)
* Added firebase crashlytics
* Updated gitlabci.yml to make the firebase json file in ci/cd
* Updated CONTRIBUTING.md

12. [Merge Request !12](https://gitlab.com/aossie/agora-android/-/merge_requests/396)- Added FirebaseMessanging Service   (**Not Merged**)
* Added Firebase Messaging dependency
* Added FcmService.kt
* Updated AndroidManifest.xml

13. [Merge Request !13](https://gitlab.com/aossie/agora-android/-/merge_requests/390)- Added a BaseFragment   (**Merged**)
* Added a Basefragment.kt for observing internet connectivity and having some commonly used function
* Added InternetManger.kt  for checking the current status of network and updating it to BaseFragment using kotlin flow
* Updated all the fragments to inherit BaseFragment
* Updated MainActivity.kt to show snackbar whenever the internet connection is established or loosed.
* Update CastVoteActivity.kt to first check for internet connectivity and then allow to cast vote
* Deleted InternetConnectivity.kt file

14.  [Merge Request !14](https://gitlab.com/aossie/agora-android/-/merge_requests/394)- Enabled R8 in order to obfuscate code   (**Not Merged**)
* Enabled R8 in build.gradle file for release builds
* Added Proguard rules

15. [Merge Request !15](https://gitlab.com/aossie/agora-android/-/merge_requests/400)- Added biometric authentication   (**Not Merged**)
* Added androidx biometric dependency
* Updated PreferenceProvider.kt
* Updated MainActivity.kt to ask for biometric authentication both fingerprint and face unlock
* Updated ProfileFragment.kt and fragment_profile.xml to provide the option to enable or disable biometric authentication