# Agora Vote Android

## Student Name: Tushar Singh ([@tusharr-98](https://gitlab.com/tusharr-98))

## Organization: [AOSSIE](https://aossie.gitlab.io/)

## [Agora Android](https://gitlab.com/aossie/agora-android/)

[Source Code](https://gitlab.com/aossie/agora-android/-/tree/gsoc-2020)

## Mentors: [Abanda Ludovic](https://github.com/icemc), [Bruno Woltzenlogel Paleo](https://github.com/ceilican), [Mukul Kumar](https://github.com/mukulCode), [Thuvarakan](https://github.com/Thuva4)

### Overview of the Application

Android Application for Agora Web that uses Agora: An Electronic Voting Library implemented in Scala. This application uses Agora Web API as backend application.

### Goals

I have identified the following tasks in the project at the starting of the project.

1. Move codebase to kotlin - **Done**
2. Move to MVVM Architecture - **Done**
3. Move to Single Activity Architecture using android navigation architecture components - **Done**
4. Offline support - **Done**
5. Refactor architecture with Dependecy Injection Framework - **Done**
6. Add new retrofit instance for handling custom exceptions - **Done**
7. Redesigning the whole application - **Done** 
10. Add dark mode support - **Done** 
11. Add Calendar View to show elections - **Done  (Suggested by the mentors during community bonding period)** 
12. Add Two Factor Authentication Feature - **Done**
13. Add Deep links for cast vote feature - **Done** 
14. Add Cast vote feature - **Done** 
15. Add change avatar feature - **Done  (Not mentioned in proposal, I added it as a necessary feature)**

### Deep view into the technology. 

This project is created using [JavaSE](https://www.oracle.com/technetwork/java/javase/downloads/index.html) on [Android Studio](https://developer.android.com/studio).

Libraries used in this project are listed below:

* [Dagger](https://github.com/google/dagger)-A fast dependency injection framework by Google.
* [Room](https://developer.android.com/topic/libraries/architecture/room)-To implement offline support for the user.
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)-Library support for Kotlin coroutines with multiplatform support.
* [Horizontal Calendar](https://github.com/Mulham-Raee/Horizontal-Calendar)-A material horizontal calendar view for Android based on RecyclerView.
* [Tachyon](https://github.com/linkedin/Tachyon)-An Android library that provides a customizable calendar day view.
* [Picasso](https://square.github.io/picasso/)-A beautiful library for image loading from Sqaure.
* [Retrofit](https://square.github.io/retrofit/)-A type-safe HTTP client for Android and Java.
* [Material Design For Android](https://developer.android.com/guide/topics/ui/look-and-feel)-Material design is a comprehensive guide for visual, motion, and interaction design across platforms and devices.
* [Design support](https://developer.android.com/topic/libraries/support-library/packages)- The Android Support Library contains several library packages that can be included in your application. Each of these libraries supports a specific range of Android platform versions and set of features.
* [Shimmer For Android](https://facebook.github.io/shimmer-android/)-Shimmer is an Android library that provides an easy way to add a shimmer effect to any view in your Android app.
* [gson](https://github.com/google/gson)-A Java serialization/deserialization library to convert Java Objects into JSON and back.
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)-  Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
* [Recycler View](https://developer.android.com/guide/topics/ui/layout/recyclerview)- To display a scrolling list of elements based on large data sets (or data that frequently changes).
* [Card View](https://developer.android.com/guide/topics/ui/layout/cardview)- To create card views.
* [Pie Chart](https://github.com/PhilJay/MPAndroidChart)- A powerful & easy to use chart library for Android.
* [Facebook Login](https://developers.facebook.com/docs/facebook-login/)- To implement User login using social media account.


### Summary of whole GSoC journey

As I was already familiar with the codebase, I started with setting the initial boilerplate code and setting up the architecture for smooth flow of work. Firstly, I started with moving architecture of the project to Single Activity Architecture using Android Navigation Architecture Components, and moving the codebase from java to kotlin.

Coding period begins. This phase is the most important one as the whole architecture of the project is going under a lot of changes, and new features like offline support and addition of dependency injection frameworks was about to take place.

Now with project following a clean and efficient architecture, I started working towards redesigning the application according to the mockups. Then the most challenging part was to implement calendar view for election fragment. After this, as I was ahead of my timeline, I started working on two factor authentication feature mentioned in 3rd phase.

During this period, I worked on some major features like Adding Deep links, Verify Voter, Cast Vote, Testing. As Agora Vote is almost ready for deployment, I added change avatar feature as one of the common features of a android application. This being officially the final phase, I also had to take care of all the smooth functioning of app and clean up code, improve wherever necessary and document the project overview at the end.

I would love to thank my mentors especially **Thuvarakan Tharmarajasingam, Abanda Ludovic, Mukul Kumar and Bruno Woltzenlogel Paleo** for always providing me constant support, they were always there for guiding me to follow better practices and suggested some very good enhancements and features for the project. Special thanks to **Mukul** for all the immediate merges and being so responsive whenever I faced a challenge. It was a wonderful experience working with you guys and I would love to be a part of the organization after GSoC.

The major next step for the project is deployment which me and my mentors are preparing for. And new students can contribute toward adding more features from the Web API and adding more unit tests, I will open some new issues on these topics for beginners.


### Merge Requests 

1. [Merge Request !1](https://gitlab.com/aossie/agora-android/-/merge_requests/263)- Authentication screens to Kotlin fragments   (**Merged**)
* Removed Splash Activity and added a splash theme with vector drawable.
* Added navigation graph for the authentication feature.
* Converted Authentication Screen(Login, Signup, HomeLogin, forgotPassword) to kotlin fragments.
* Use of navigation components and animations to move between fragments.

2. [Merge Request !2](https://gitlab.com/aossie/agora-android/-/merge_requests/264)- Added Bottom Navigation Bar, Custom Toolbar along and Some Changes- status   (**Merged**)
* Added Bottom Navigation bar and custom toolbar in the Main activity.
* Handled the visibility of the navbar and custom toolbar.
* Override onBackPressed method for correct behavior.
* Added Elections and More Fragments.
* Migrated Home Fragment to kotlin.
* Added AuthListener to handle success Events.
* Added View utility to show and hide the support action bar.
* Added some icons and animations for the above functionalities.

3. [Merge Request !3](https://gitlab.com/aossie/agora-android/-/merge_requests/266)- Added More options fragment   (**Merged**)
* Migrated home fragments to kotlin.
* Created layout for More Options Fragment.
* Added selectable drawable for textviews in options.
* Added option screens to navigation graph.

4. [Merge Request !4](https://gitlab.com/aossie/agora-android/-/merge_requests/268)-Converted all display elections screens to Kotlin fragments-status   (**Merged**)
* Developed Elections Fragment.
* Converted Active, Pending, and Finished screens to kotlin fragments.
* Linked Fragments to navigation Graph.

5. [Merge Request !5](https://gitlab.com/aossie/agora-android/-/merge_requests/269)-Added Adapter Callbacks and Converted Ballot, voters and Election Details screen to kotlin fragments.-status   (**Merged**)
* Added adapter callback to handle onItemTouchListener for recycler view.
* Used live data in display election ViewModel and converted it to kotlin.
* Migrated Voter, ballot, and Election details kotlin fragments.

6. [Merge Request !6](https://gitlab.com/aossie/agora-android/-/merge_requests/270)-Configured create election module for single activity architecture and migrated it to kotlin-status   (**Merged**)
* Complete create election module migrated to kotlin
* Follows Single Architecture
* Added listeners.

7. [Merge Request !7](https://gitlab.com/aossie/agora-android/-/merge_requests/271)- Invite voters module - Single Activity and Kotlin migration-status   (**Merged**)
* Configured invite voter module for single activity architecture and moved to kotlin codebase.

8.  [Merge Request !8](https://gitlab.com/aossie/agora-android/-/merge_requests/272)-Setup project for room database- status   (**Merged**)
* Added room dependencies
* Setup database builder
* Added User entity
* Setup preference provider

9. [Merge Request !9](https://gitlab.com/aossie/agora-android/-/merge_requests/273)- Dagger Configuration -status   (**Merged**)
* Added dagger utilities, modules and components.

10. [Merge Request !10](https://gitlab.com/aossie/agora-android/-/merge_requests/274)- Configured login module for MVVM architecture,  added repository layer -status   (**Merged**)
* Added dependencies for kotlin coroutines.
* Added network interceptor.
* Configured retrofit client for MVVM architecture.
* Added user repository.
* Configured login fragment for MVVM architecture.
* Added custom exceptions.
* Added Textwatcher for login fragment fields to disable/enable button accordingly.

11. [Merge Request !11](https://gitlab.com/aossie/agora-android/-/merge_requests/275)- Save User -status   (**Merged**)
* Added User Dao
* save user after login
* Added different messages for different API or internet exceptions
* Removed Toast with Snackbar in the login module.
* Removed load toast with progress bar in login fragment.

12. [Merge Request !12](https://gitlab.com/aossie/agora-android/-/merge_requests/276)- Refactoring login module with dagger -status   (**Merged**)
* Injected various dependencies
* Added Fragment Key and Fragment Factory
* Refactored login, welcome fragment, and Main Activity with the dagger.

13. [Merge Request !13](https://gitlab.com/aossie/agora-android/-/merge_requests/277)- Refactoring home module -status   (**Merged**)
* Added Election Dao, election entity.
* Models for ballot, candidates, score, voters, and winner.
* API implementation to get user's elections if fetching required.
* Added Election Repository.
* Use of live data to get all Elections from the Election Repository.
* Refactored home module for MVVM.
* Refactored home and more options module with Dagger.
* Added type converters and Coroutine extension functions

14. [Merge Request !14](https://gitlab.com/aossie/agora-android/-/merge_requests/278)- Fix - Refresh token -status   (**Merged**)
* Refactoring update token function according to current retrofit instance

15. [Merge Request !15](https://gitlab.com/aossie/agora-android/-/merge_requests/280)- (Compiling all the work for Phase 1 in one MR) Move to MVVM Architecture, Dagger Configuration, Offline support, Repository layer for managing elections, New UI for election fragment, Added validations for dates, Added fb authentication and Manage App State accordingly -status   (**Merged**)
* Implemented logout feature, now all the user data and preferences gets deleted on logout.
* The new item list and election adapter with data binding feature(new) for election fragment recycler view are created.
* Live data is fetched in election fragment and bounded to the UI.
* Signup, forgot password, create election, display all elections, invite voters, profile, and election details modules are configured with MVVM, dagger, and new retrofit instance.
* Added offline support for the app in case the user is not connected.
* Added the Repository layer to follow MVVM architecture.
* Use of live data and data binding to update UI.
* New UI for recycler view items.
* Validator added for start and end date during create election feature.
* Added an Authentication interceptor to handle the case if the token gets expired while the app is running.
* Added facebook authentication and configured app accordingly for FB user
* Removed Tiny DB lib
* Removed Load Toast lib and replace it from android's progress bar
* Cleaned architecture and fixed bugs.

16. [Merge Request !16](https://gitlab.com/aossie/agora-android/-/merge_requests/282)- Redesign Authentication and welcome screens    (**Merged**)
* Redesigned Welcome, login, signup and Forgot Password
* Dark Mode is supported for the above screens
* Used material components theme and widgets
* Tested for smaller screens and lower API level
* Changed MinSdkVesrion to API level 22

17. [Merge Request !17](https://gitlab.com/aossie/agora-android/-/merge_requests/283)- Redesign home and create Election Screens    (**Merged**)
* Home and Create election screens are redesigned and compatible with dark mode.

18. [Merge Request !18](https://gitlab.com/aossie/agora-android/-/merge_requests/284)- Redesign Settings, profile and other screens -status   (**Merged**)
* Settings, Account Settings, About Us, Contact Us, Share screens are redesigned and compatible with dark mode.

19. [Merge Request !19](https://gitlab.com/aossie/agora-android/-/merge_requests/285)- Add Calendar view to the election fragment -status   (**Merged**)
* Setup Calendar fragment.
* Added horizontal calendar view.
* Added Day view.
* Added custom backgrounds for the pending, active, and finished elections.
* Added Custom hour label.
* Handle the case for events for more than 1 day.
* Now events are added to the calendar view after getting election response from API.

20. [Merge Request !20](https://gitlab.com/aossie/agora-android/-/merge_requests/286)- Adding swipe refresh and Android calendar view to Calendar View Fragment -status   (**Merged**)
* Added swipe refresh feature to a calendar view to update the elections in the DB
* Progress bar to show the progress of loading of events
* Swipe down android calendar view to move from one month to another
* Integrated android calendar view with horizontal calendar view and day view.
* Hide app bar view on scrolling down.

21. [Merge Request !21](https://gitlab.com/aossie/agora-android/-/merge_requests/287)- feat: Two Factor Authentication -status   (**Merged**)
* Implemented 2 factor authentication toggle in profile settings
* Made two factor authentication screen
* Implemented verify voter feature.
* Manged application state and database accordingly.

22. [Merge Request !22](https://gitlab.com/aossie/agora-android/-/merge_requests/288)- Bug fixes and Some UI Changes -status   (**Merged**)
* Bug fixes related to the two-factor authentication feature
* Some UI changes to the Elections list and Election Details Screen.

23. [Merge Request !23](https://gitlab.com/aossie/agora-android/-/merge_requests/289)- feat: Resend OTP -status   (**Merged**)
* Implemented resend OTP feature.

24. [Merge Request !24](https://gitlab.com/aossie/agora-android/-/merge_requests/291)- feat : Deep Links and Verify Voter -status:**Merged**
* Added deep links to cast vote activity
* Resolved the sendgrid.net URL to retrieve election id and passcode
* Implemented verify voter feature to fetch election data.
* Added default animation for fragment transitions.
* Fixed some bugs.

25. [Merge Request !25](https://gitlab.com/aossie/agora-android/-/merge_requests/292)- feat: Cast Vote -status   (**Merged**)
* Cast vote screen
* Data Binding to bind election data to UI
* Implemented Cast Vote Feature.
* Redesigned Invite voter Feature.

26. [Merge Request !26](https://gitlab.com/aossie/agora-android/-/merge_requests/289)- Refactor tests for new Retrofit instance and added some test suites -status   (**Merged**)
* Refactor API tests for new retrofit instance.
* Added Coroutines in tests.
* Added test suites.

27. [Merge Request !27](https://gitlab.com/aossie/agora-android/-/merge_requests/294)- feat: change avatar -status   (**Merged**)
* Picasso for image loading
* Added Permission for camera and storage
* Handled rotation and sampling of the image.
* Implemented change avatar API.

28. [Merge Request !28](https://gitlab.com/aossie/agora-android/-/merge_requests/295)- Code cleaning and bug fixes -status   (**Merged**)
* Cleanup Code
* Remove warnings
* Rearrange architecture
* Bug fixes

28. [Merge Request !28](https://gitlab.com/aossie/agora-android/-/merge_requests/296)- Library Updated and Fix memory leaks -status   (**Merged**)
* Test App for memory leaks.
* Updated dependencies in apps build.gradle
* Made changes accordingly.  


### Screenshots


**Calendar View Events:**
 
 
<img src="https://www.linkpicture.com/q/Screenshot_20200827-081839.png" width=200/>       <img src="https://www.linkpicture.com/q/Screenshot_20200827-082304.png" width=200/>      <img src="https://www.linkpicture.com/q/Screenshot_20200827-080931.png" width=200/>


**Change Avatar Feature:**


<img src="https://gitlab.com/aossie/agora-android/uploads/46fd6fb29f317bbfbf36ab84fcce8e1d/Screenshot_20200819-234802.jpg" width=200/>       <img src="https://gitlab.com/aossie/agora-android/uploads/1043a51c7e94ea15222ed68dd83c2119/Screenshot_20200819-234426_1.jpg" width=200/>      <img src="https://gitlab.com/aossie/agora-android/uploads/d9f2d6c75052e6f7dbb1126de22b5f52/Screenshot_20200819-234421.jpg" width=200/> 


**Two Factor Authentication:**

 
<img src="https://www.linkpicture.com/q/Screenshot_20200827-080828.png" width=200/>


**Cast Vote Feature:**


<img src="https://www.linkpicture.com/q/Screenshot_20200826-222223.png" width=200/>       <img src="https://www.linkpicture.com/q/Screenshot_20200826-222227.png" width=200/>      <img src="https://www.linkpicture.com/q/Screenshot_20200826-222057.png" width=200/>        <img src="https://www.linkpicture.com/q/Screenshot_20200826-222236.png" width=200/>      <img src="https://www.linkpicture.com/q/Screenshot_20200826-222244.png" width=200/> 


**Authentication Screens:**
 
 
 <img src="https://gitlab.com/aossie/agora-android/uploads/fe6ce7486335869dbff81eae4ef23872/Screenshot_20200709-005201.jpg" width=200/>   <img src="https://gitlab.com/aossie/agora-android/uploads/73cdaf2cb7f62c859a9dc9b43e9e76e0/Screenshot_20200709-005142.jpg" width=200/>     <img src="https://gitlab.com/aossie/agora-android/uploads/b0228b5231d19f60e01faf6976b100e6/Screenshot_20200709-005146.jpg" width=200/>     
 <img src="https://gitlab.com/aossie/agora-android/uploads/446ba07a0a2e4c33ee5cfb317cac42f2/Screenshot_20200709-005152.jpg" width=200/>      <img src="https://gitlab.com/aossie/agora-android/uploads/a36b25cddc3216a2334e18a00e5452ff/Screenshot_20200709-005156.jpg" width=200/>


**Home and Create Election Screens:**


<img src="https://gitlab.com/aossie/agora-android/uploads/5a93c51d28eb19be0be8b27c94f8c860/Screenshot_20200711-163017.jpg" width=200/>     <img src="https://gitlab.com/aossie/agora-android/uploads/06cc7c47a65cbbfbd54fcacf2c6556ea/Screenshot_20200711-163032.jpg" width=200/>


**Cast Vote Feature:**


<img src="https://www.linkpicture.com/q/Screenshot_20200823-222910.png" width=200/>       <img src="https://www.linkpicture.com/q/Screenshot_20200823-222931.png" width=200/>      <img src="https://www.linkpicture.com/q/Screenshot_20200823-222934.png" width=200/> 


**Settings and Profile Screens:**


<img src="https://gitlab.com/aossie/agora-android/uploads/fff70a5b24c974328b7e214dc6f5f56b/Screenshot_20200713-160047.jpg" width=200/>      <img src="https://gitlab.com/aossie/agora-android/uploads/d456bf090fa7d19ca702e066c9597024/Screenshot_20200713-160055.jpg" width=200/>


**Other Screens:**


<img src="https://gitlab.com/aossie/agora-android/uploads/5c906ca0e9ef61f648ce3e7e1d665873/Screenshot_20200713-171842.jpg" width=200/>       <img src="https://gitlab.com/aossie/agora-android/uploads/855c43c6547e9144283d50bd43d043a0/Screenshot_20200713-171855.jpg" width=200/>      <img src="https://gitlab.com/aossie/agora-android/uploads/988008a0e70011d1930d3d1e3c9ac752/Screenshot_20200713-171829.jpg" width=200/>
