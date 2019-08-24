# Agora Vote Android

## Student - Mukul Kumar

## Links
- Project : https://gitlab.com/aossie/agora-android
## Agora Frontend
The goal of this project is to develop an android application which uses Agora Web API so that we can scale up and reach more users.

### Use case modeling 

I have identified the following tasks/features in the project at the starting of the project:
1. Splash Screen with animation - **Done**
2. User signup and login with email base accounts - **Done**
3. User login using social media account - **Done**
4. Contact us throught the application - **Done**
5. Report a bug through the application - **Done**
6. Change password for email based accounts - **Done** 
7. Share with others - **Done** 
8. Logout from the application - **Done**
9. Forgot Password - **Done**
10. Create and schedule Election.  - **Done** 
11. Dashboard - **Done** 
12. Profile - **Done**
13. Display the details of elections - **Done** 
14. Delete the Election. - **Done** 
15. Invite the voters to vote for the Election. - **Done** 
16. Interface for voters to vote the Election. - **Pending** 
17. Interface for for election results - **Done** 


### Deep view into the technology. 

This project is created using [JavaSE](https://www.oracle.com/technetwork/java/javase/downloads/index.html) on [Android Studio](https://developer.android.com/studio).

Libraries used in this project are listed below:

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

We started working on the project on time but I was a bit uncomfortable with the Agora Web API and this make me behind my timeline. We decided to go feature by feature and the first features that we implemented were login and signup with email baed accounts. We left the implementation of splash screen because it will slow down the process of debugging the application on real device.

After that we worked on specific action on by one, we created navigation drawer using jetpack's navigation commponent and displayed he number of elections on it. Then we worked on all the fragments assosiated with the Dashbpard which are "About agora", "Profile", "Report bug", "Contact us", "Share with others".

Then we worken on the most important feature of the application i.e., create election and then we worked on change password and forgot password. Then we moved forward to the manage the application state(login and logout). After fixing some bugs related to input validation and backward compatibility we worked on displaying the details of elections into 4 parts(Total elections, Active elections, Pending elections, Finished elections).

After that we implemented Splash screen, share with others feature facebook login and delete election.
During the end of coding period we worked on invite voters, view ballot, view result, view voters and managed login session. Due to some time constraints one feature "Cast votes" is pending but still we can vote through the website using android devices(thanks to agora web). I would like to complete it as soon as possible and love to mentor students if given a chance in future.

I would like to thank every AOSSIE member, especially my mentors, **Thuvarakan Tharmarajasingam**, **Abanda Ludovic**, **Prudhvi Reddy** and **Bruno Woltzenlogel Paleo** for being so nice and helpful. I have learnt a lot in the past 3 months and it has been a great experience to be a part of this wonderful community. 
A special thanks to **Prudhvi** again because whenever I need help he was there every single time and mentored me, I learned a ton of things from Prudhvi and Abanda. 

### Merge Requests 

1. [Merge Request !1](https://gitlab.com/aossie/agora-android/merge_requests/2)- Project Initialisation- status:-**Merged**
* Added Icon to the app.
* Created Main, Signup, Signin screens.

2. [Merge Request !2](https://gitlab.com/aossie/agora-android/merge_requests/3)-Signup and Solved compatibility Issues- status:**Merged**
* Completed Signup using email base accounts
* Resolved backward compatibility issues

3. [Merge Request !3](https://gitlab.com/aossie/agora-android/merge_requests/4)-Completed Login-status:**Merged**
* Implemented login feature and covered all the test cases possible.

4. [Merge Request !4](https://gitlab.com/aossie/agora-android/merge_requests/5)-Completed Navigation Drawer and Home Activity-status:**Merged**
* Created Dashboard
* Implemnted "view profile"
* Implemented "Contact us" feature
* Implemented "Report a bug" feature

5. [Merge Request !5](https://gitlab.com/aossie/agora-android/merge_requests/6)-Created activities for "Create Election Process"-status:**Merged**
* Created 4 activities for the "create election" process.
* Used Recycler View for adding candidate during the create election process, saved all the names into an arraylist of type string.
* Instead of creating a button we can swipe left/right to delete any candidate from the list.
* Used Date and Time picker to select the date and time to start or end any election, the user can open it by clicking on the calendar icon.

6. [Merge Request !6](https://gitlab.com/aossie/agora-android/merge_requests/7)-Completed "Create Election Process" and "Change Password" feature-status:**Merged**
* Implemented Create Election process
* Implemented Change Password feature 

7. [Merge Request !7](https://gitlab.com/aossie/agora-android/merge_requests/8)- Implemented Forgot Password Feature-status:**Merged**
* Implemented forgot password

8.  [Merge Request !8](https://gitlab.com/aossie/agora-android/merge_requests/9)-Created Splash Screen, Implemented Logout and managed App state for login and logout- status:**Merged**
* Created Splash Screen which is having an animation of rotating agora icon which fades out after rotation.
* Managed app state: User will stay Logged In until he/she clicks on logout.
* Implemented a Logout feature.

9. [Merge Request !9](https://gitlab.com/aossie/agora-android/merge_requests/10)-Fix "Unable to login due to blank spaces before or after the username or password"-status:**Merged**
* fix  "user is not able to log in if he/she put blank spaces before of after username or password by mistake"

10. [Merge Request !10](https://gitlab.com/aossie/agora-android/merge_requests/11)-Displayed number of elections and displayed complete info of elections categorized in Total, Active, Pending and Finished elections-status:**Merged**
* GET the election data and categorized that election data into Total, Active, Pending and Finished elections based on "Current Date", "Starting Date" and "Ending Date".
* Display the number of elections of each type on their corresponding card view on the home fragment.
* Created 4 activities viz, ActiveElectionsActivity, TotalElectionsActivity, PendingElectionsActivity,FinishedElectionsActivity
* Created a RecyclerView Adapter and an election item to which all the details will be displayed.

11. [Merge Request !11](https://gitlab.com/aossie/agora-android/merge_requests/12)-Implemented "Facebook Login", "Share with others", "delete election", managed login session and updated UI for fragments-status:**Merged**
* Implemented Login Via Facebook: Users can log in directly using their facebook account.
* Implemented Share with others: User can share the link of application through any medium, the link will be active after publishing the app on play store.
* Implemented delete election: The user can click on any election and view more details about it and delete the election from the button provided below the details of the election.
* Managed login session: After the expiration of the token, whenever the application is opened we get a new token to continue the login session and the user does not have to log in again.
* Updated UI for report bug fragment to make it similar to other fragments.



