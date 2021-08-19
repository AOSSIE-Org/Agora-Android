# Agora Vote Android

## Student Name: Divyank Lunkad ([@divyank00](https://gitlab.com/divyank00))

## Organization: [AOSSIE](https://aossie.gitlab.io/)

## [Agora Android](https://gitlab.com/aossie/agora-android/)

## [Source Code](https://gitlab.com/aossie/agora-android/-/tree/gsoc-2021)

## Mentors: [Prudhvi Reddy](https://github.com/prudhvir3ddy), [Ritesh Agrawal](https://github.com/Ritesh-Ag), [T Thuvarakan](https://github.com/Thuva4)

## APK: [release.apk](https://drive.google.com/file/d/1Q9Cc902o5bR4XAwSKUHbp9Uz46HWKD4O/view?usp=sharing)
## App will be released on Google Play Store soon!

### Overview of the Application

Android Application for Agora Web that uses Agora: An Electronic Voting Library implemented in Scala. This application uses Agora Web API as backend application.


### Goals

I had identified the following tasks in the project at the starting of the project.

1. Shift codebase to kotlin - **Done**
2. Refresh token without forcing user to log out - **Done**
3. Fix majority of bugs reported on issue page - **Done**
4. Enable deep linking  for cast vote feature - **Done**
5. Add search bar for elections - **Done**
6. Import voters and candidates from .xlxs - **Done**
7. Export result in .xlxs and .png format - **Done**
8. Secure the token using crypto and shifting to datastore - **Done** 
9. Add onboarding/welcome screen - **Done** 
10. Add spotlight to guide first time uers - **Done**
11. Send notifications to invited voters - **Done**
12. Refractor coroutine extention functions - **Done (Suggested by the mentors)**
13. Add new charts to visualise result - **Done**
14. Add unit testing - **Done**


### Summary of whole GSoC journey

As I was already familiar with the codebase, I started testing to find new bugs. Also got in touch with the mentors regarding the codebase and best coding practices.

Coding period begins. Firstly, I started with fixing majority of issues reported on the issue page and moving the remaining codebase to Kotlin. Added new features like importing and exporting data from excel sheets and support to search elections. Also improved security of the app by encrypting the tokens.

During the second coding period, I added new screens like welcome screen and spotlights. Implemented the feature to send notifications to voters and improved the codebase by making use of coroutines in the right manner. Also added bar chart to visualise the result. In the end, started with the documentation the project overview.

I'd want to express my gratitude to my mentors, **Prudhvi Reddy, Ritesh Agrawal, and Thuvarakan Tharmarajasingam**, for their unwavering support. They were constantly available to advise me on best practises and offered some excellent upgrades and features for the project. Special thanks to **Prudhvi** for all the quick merges and for always being so helpful and responsive anytime I faced any problem. Working with you guys was a fantastic experience, and I'd want to stay on as a member of the team even after GSoC.


### Merge Requests 

1. [Merge Request !1](https://gitlab.com/aossie/agora-android/-/merge_requests/365)- Shift codebase to kotlin  (**Merged**)
* Changed APIService.java, CandidateRecyclerAdaper.java and VoterRecylcerAdapter.java to kotlin 

2. [Merge Request !2](https://gitlab.com/aossie/agora-android/-/merge_requests/366)- Make date's edit text completely clickable   (**Merged**)
* Add click listener to parent of edit text

3. [Merge Request !3](https://gitlab.com/aossie/agora-android/-/merge_requests/367)- Fix typos and UI bugs   (**Merged**)
* Replaced 'Send Link' with 'Verify' in fragment_two-factor-auth.xml
* Renamed 'Ballot Fragment' to 'Ballot' in nav_graph.xml
* Added bottom margin to log out button

4. [Merge Request !4](https://gitlab.com/aossie/agora-android/-/merge_requests/368)- Fix scrolling issue due to keyboard   (**Merged**)
* Replaced the value of 'android:windowSoftInputMode' from 'adjustPan' to 'adjustResize|stateVisible' in AndroidManifest.xml

5. [Merge Request !5](https://gitlab.com/aossie/agora-android/-/merge_requests/369)- Disable facebook button once clicked   (**Merged**)
* Disabled facebook login button once user clicks it

6. [Merge Request !6](https://gitlab.com/aossie/agora-android/-/merge_requests/370)- Add swipe feature to calenderView   (**Merged**)
* Added SwipeDetector.kt with right and left swipes
* Attached touch listener to calendarLayout to detect swipe gesture with the help of SwipeDetector.kt

7. [Merge Request !7](https://gitlab.com/aossie/agora-android/-/merge_requests/371)- Fix duplication of data due to observer   (**Merged**)
* Cleared the map which has existing data, before new data is fetched

8. [Merge Request !8](https://gitlab.com/aossie/agora-android/-/merge_requests/374)- Refresh token without forcing user to log out    (**Merged**)
* Stored refresh token received from backend
* If api response code is 401, refreshed the secure token with help of refresh token
* In case refresh token is also expired, the user is logged out, and refirected to login screen

9. [Merge Request !9](https://gitlab.com/aossie/agora-android/-/merge_requests/375)- Enable deep linking for cast vote feature   (**Merged**)
* Changed the host name in AndroidManifest.xml
* Replaced ElectionAdapterCallback with lamda function

10. [Merge Request !10](https://gitlab.com/aossie/agora-android/-/merge_requests/380)- Add search bar for elections   (**Merged**)
* Added material search bar to filter elections
* Converted RecyclerView.Adapter to ListAdapter

11. [Merge Request !11](https://gitlab.com/aossie/agora-android/-/merge_requests/381)- Import voters and candidates from .xlxs   (**Merged**)
* Added poi dependency
* Added import icon to pick excel sheet from phone storage
* Process the excel sheet to fetch useful data

12. [Merge Request !12](https://gitlab.com/aossie/agora-android/-/merge_requests/384)- Export result in .xlxs and .png format   (**Merged**)
* Added provider_paths.xml
* Setup provider in AndroidManifest.xml
* Added export icon to export data in excel format
* Added share icon to share the result as image to external apps

13. [Merge Request !13](https://gitlab.com/aossie/agora-android/-/merge_requests/385)- Secure the token using crypto and shifting to datastore   (**Merged**)
* Added datastore dependency and upgraded kotlin version
* Shifted complete codebase to view binding
* Added SecurityUtil to encrypt token using cipher and secretkey
* Replaced SharedPreferences with DataStore
* Updated .gitlab-ci.yml and CONTRIBUTING.md to notify contributor about the secrey key

14.  [Merge Request !14](https://gitlab.com/aossie/agora-android/-/merge_requests/389)- Add onboarding/welcome screen   (**Merged**)
* Added ViewPager and 4 fragments which shows the use case of the app 
* Converted all .jpg and .png to .webp

15. [Merge Request !15](https://gitlab.com/aossie/agora-android/-/merge_requests/393)- Add spotlight to guide first time uers   (**Merged**)
* Added spotlight dependency
* Created a dynamic layout for spotlight
* Added OverlayView and click listeners to the layout

16. [Merge Request !16](https://gitlab.com/aossie/agora-android/-/merge_requests/398)- Send notifications to invited voters   (**Merged**)
* Added dependencies for fcm
* Created FCMApi.kt with an api to send notification
* Updated .gitlab-ci.yml and CONTRIBUTING.md to notify contributor about the server key

17. [Merge Request !17](https://gitlab.com/aossie/agora-android/-/merge_requests/399)- Refractor coroutine extention functions   (**Merged**)
* Used Dispatchers.Default for heavy computations
* Refractored Coroutines.kt with viewmodelscope.launch

18. [Merge Request !18](https://gitlab.com/aossie/agora-android/-/merge_requests/403)- Add new charts to visualise result   (**Merged**)
* Added bar chart to show votes for each candidate
* Improved existing bar chart

19. [Merge Request !19](https://gitlab.com/aossie/agora-android/-/merge_requests/404)- Add unit testing   (**Merged**)
* Added dependency for coroutine testing
* Added unit tests for pending apis

### Future Scope
1. Show all elections which are left to vote to the user
2. Add an option to remind a user to vote