# Agora Vote Android

## Student Name: Amit Kumar ([@hackeramitkumar](https://gitlab.com/hackeramitkumar))

## Organization: [AOSSIE](https://aossie.gitlab.io/)

## [Agora Android](https://gitlab.com/aossie/agora-android/)

[Source Code](https://gitlab.com/aossie/agora-android/-/tree/gsoc-2022)

## Mentors: [Abhishek Kumar](https://github.com/https://gitlab.com/ABHI165), [Divyank Lunkad](https://github.com/divyank00)

### Overview of the Application

Android Application for Agora Web that uses Agora: An Electronic Voting Library implemented in Scala. This application uses Agora Web API as backend application.
And its android version is written in KOTLIN.

### Goals

During community bonding period,  We did modification in my original proposal. And decided to do the following tasks.

1. Move to layered clean architecture - **Done**
2. Implementation of KOTLIN FLOW- **Done**
3. Handel status bar color in settings fragment  - **Done**
4. Remove empty election text from election recycler view  - **Done**
5. Add an option to remove profile pic - **Done**
6. Android 12 splash screen API - **Done**
7. Add a time checker for creating election- **Done**
10. Add a restriction on password size - **Done**
11. Confusing toast when user register by same email or username - **Done**
12. Fix android 12 migration error - **Done**

### Summary of whole GSoC journey

As I was already familiar with the codebase. So I started to create some figma design for POLL feature during community bonding period. And I learnt some new features. But after that I and my mentors decided to do some modification in my proposal. So we decided to move the whole code base in Clean Architecture and Implementation of kotlin flow. And I have also decided to fix some bugs.

Coding period begins. Since I have never worked on clean architecture before. So this was challenging for me. So we planned to fix bugs in initial phase and explore the clean architecture and kotlin flow as well as.

So firstly I solved the settings fragment status bar color issue. And after that Added a option for deleting profile pic. And then Added a restriction on password size. After that removed a unnecessary text from election recycler view.

After that I have created a PR for module setup of clean architecture. And then I have picked up login and sign Up fragment and move it to the clean architecture. And after that two factor authentication and forgot password fragments. And then profile fragment , create election , home fragment etc. And after completing clean architecture I have picked firstly UserRepository and implemented kotlin flow in it. And then Election repository and Implemented kotlin flow in it. And after that picked up viewmodels one by one and replaced livedata by StateFlow.

I would love to thank my mentors especially **Abhishek kumar, Divyank Lunkad** for always providing me constant support, they were always there for guiding me to follow better practices and suggested some very good enhancements and features for the project. Special thanks to **Abhishek** for all the immediate merges and being so responsive whenever I faced a challenge. It was a wonderful experience working with you guys and I would love to be a part of the organization after GSoC. And I will constantly give my contribution to the project in the future.

The major next step for the project is UNIT TESTING and automate android release which me and my mentors are preparing for. And new students can contribute toward adding more features and adding more unit tests.

### Merge Requests

1. [Merge Request !1](https://gitlab.com/aossie/agora-android/-/merge_requests/421)- hide the empty election text view when the election list is non empty  (**Merged**)
* I have made the empty text TV hide when the list is not empty otherwise the text view will be shown.
* Earlier we were handling only showing the text view and not hiding the text view.

2. [Merge Request !2](https://gitlab.com/aossie/agora-android/-/merge_requests/426)- Clean architecture module and package setup  (**Merged**)
* created three layer data, domain and UI
* Data layer have local db, remote db , adapters, Api , mappers, repositories implementation.
* Domain layer will have model ,use cases and repository interface.
* UI layer will have activities , fragments and DI module.

3. [Merge Request !3](https://gitlab.com/aossie/agora-android/-/merge_requests/418)- Added a time check during creating new election    (**Merged**)
* Added a comparison between the current time and the election start time. So when user click on submit button then a comparison take place.
* if the current time is greater than start time of election then there will be a error and as he will select a time grater than current time then the error will be gone.


4. [Merge Request !4](https://gitlab.com/aossie/agora-android/-/merge_requests/416)-Settings fragment status bar color (**Merged**)
* Handled settings fragment status bar color separately.
* When Fragment Id will be equal to the settings fragment then window.addFlags(.....) will not be used.


5. [Merge Request !5](https://gitlab.com/aossie/agora-android/-/merge_requests/427)-Log in fragment migration to clean architecture   (**Merged**)
* Created UserRepository interface in domain layer. And added its implementation in data layer.
* Added DI module for this interface.
* Added domain model for User , autoresponder, LoginDto and also mapper function for them.
* Added a mapper function interface which will be implemented by all the mapper functions.
* Added use cases for logIn fragment. And their DI module also.
* Added these all use cases in a wrapper class and injected that in logIn viewmodel.
* Implementation of useCases in logIn viewModel.

6. [Merge Request !6](https://gitlab.com/aossie/agora-android/-/merge_requests/428)-Signup fragment to clean architecture    (**Merged**)

* Added SignUp Fragment Use Cases. And Kept them all in a wrapper class.
* Added DI module function for SignUpUseCases wrapper class.
* Injected wrapper class in SignUp ViewModel.
* Implementation of signUp use cases.

7. [Merge Request !7](https://gitlab.com/aossie/agora-android/-/merge_requests/438)- Change in the toast message of the registration conflict response  (**Merged**)
* Did modification in the toast message of the registration response.
* When we were getting conflict then we can not distinguish between the email and username. We were getting error code 412. So its a backend dependency. So I modify message.

8.  [Merge Request !8](https://gitlab.com/aossie/agora-android/-/merge_requests/417)-Added a restriction on password length - status   (**Merged**)
* Previously user can create a account by using a less then 6 length of password. This was not giving good experience.
* Therefore, Added a restriction on password length, and now user can not   create a password less than 6 length.
* And If he will try to do then he will get a error message.

9. [Merge Request !9](https://gitlab.com/aossie/agora-android/-/merge_requests/429)- Two factor authentication  -status   (**Merged**)
* Added a mapper class
* Added verifyOtpDto class
* Added use cases for two factor viewmodel
* Implemented use cases in two factor auth viewmodel

10. [Merge Request !10](https://gitlab.com/aossie/agora-android/-/merge_requests/430)- Forgot password page migration -status   (**Merged**)
* Added a DI module function
* Added use cases for forget password viewmodel
* Implemented use cases in forget password viewmodel

11. [Merge Request !11](https://gitlab.com/aossie/agora-android/-/merge_requests/431)- Profile fragment migration -status   (**Merged**)
* Added UserEntityMapper in data layer
* Added updateUserDtoEntityMapper in data layer
* Added updateUserDtoModel in domain layer
* Added change avatar usecase ,changePasswordUseCase, getUserDataUseCase, ToggleTwoFactorAuthUseCase
* Added all the use cases in a wrapper class, And injected it in ProfileViewModel

12. [Merge Request !12](https://gitlab.com/aossie/agora-android/-/merge_requests/433/diffs)- Home frgment migraton to clean architecture-status   (**Merged**)
* Added election repository interface and implementation of it in data layer
* Added electionDtoEntityMapper
* Added electionDtoModel in domainModel
* Added create_election and home_fragment use_cases
* kept them in a wrapper class and injected that in respective viewModels.
* added implementation of use cases in viewmodels

13. [Merge Request !13](https://gitlab.com/aossie/agora-android/-/merge_requests/447)- Android 12 migration dependency error fix -status   (**Merged**)
* Added exported as true for the activities having intent filter
* Updated leak canary version from 2.6 to 2.7

14. [Merge Request !14](https://gitlab.com/aossie/agora-android/-/merge_requests/435)- Election details fragments -status   (**Merged**)
* Replaced adapter data entity by domain models.
* Added BallotDtoEntityMapper, ElectionEntityMapper, VotersDtoEntityMapper, WinnerDtoEntityMapper
* Added ballotDtoModel, ElectionModel, VotersDtoModel, WinnerDtoModel
* removed underscore from the package names
* Added use cases and kept them all the a wrapper class
* Injected Wrapper class in Election Details viewmodels

15. [Merge Request !15](https://gitlab.com/aossie/agora-android/-/merge_requests/434/diffs)- Display elections fragment , calender view fragments migration  -status   (**Merged**)
* Replaced ElectionAdapter data entity by domain models.
* Added ElectionEntityMapper in data layer
* Added ElectionModel in domain layer
* removed underscore from the package names
* Added use cases and kept them all the a wrapper class
* Injected Wrapper class in  DisplayElection and calender view view models.

16. [Merge Request !16](https://gitlab.com/aossie/agora-android/-/merge_requests/436)- Main activity migration to clean architecture  (**Merged**)
* Added delete user use case
* Added DI module function for deleteUserUseCase
* Injected Delete user-useCase in mainActivity viewModel.
* Implemented use case in main activity

17. [Merge Request !17](https://gitlab.com/aossie/agora-android/-/merge_requests/445)- Remove TODO , wrapper class variable rename, Inject mappers (**Merged**)
* Removed entityMapper interface and create all the mapper class simple.
* Renamed all the useCases wrapper class variable name
* Kept all the mapper classes in wrapper class
* Injected mapper wrapper class in all the use cases instead of manually


18. [Merge Request !18](https://gitlab.com/aossie/agora-android/-/merge_requests/420/diffs)-Added a remove profile pic option -status   (**Merged**)
* Added a local drawable which will be used as placeholder
* Added a delete icon
* Made modification in delete profile pic dialog box
* Added a delete pic function in profile fragment

19. [Merge Request !19](https://gitlab.com/aossie/agora-android/-/merge_requests/437)-Cast vote activity migration  -status   (**Ready to merge**)
* Added election dto entity mapper
* added CastVoteActivity UseCases and kept them all in a wrapper class
* Injected that class in castVoteViewModel
* Implemented use cases in castVoteActivityViewModel

20. [Merge Request !20](https://gitlab.com/aossie/agora-android/-/merge_requests/440)- UserRepository kotlinFlow migration -status   (**Ready to merge**)
* Changed return type of getUser function in UserDao
* Removed mapLiveData from userEntityMapper
* Changed return type of getUserUseCase
* collected flow in respective fragments

21. [Merge Request !21](https://gitlab.com/aossie/agora-android/-/merge_requests/441)- Login and SignUp fragments Stateflow implementation -status   (**Ready to merge**)
* Added mutableStateFlow in logIn and Signup viewmodels
* Collected them in their respected fragments


22. [Merge Request !22](https://gitlab.com/aossie/agora-android/-/merge_requests/442)-  Two factorauth and forgot password Stateflow migration
 -status   (**Ready to merge**)
* Added mutableStateFlow in TwoFactorAuth and Forgot Password viewmodels
* Collected them in their respected fragments


23. [Merge Request !23](https://gitlab.com/aossie/agora-android/-/merge_requests/443)- Create Election and home fragment Stateflow migration -status   (**Ready to merge**)
* * Added mutableStateFlow in createElection and homeFragment viewModel
* Collected them in their respected fragments

24. [Merge Request !24](https://gitlab.com/aossie/agora-android/-/merge_requests/444)- Election repository kotlin flow implementation -status:**Ready to merge**
* Changed return type of all the election function in ElectionDao
* Removed mapLiveData from ElectionEntityMapper
* Changed return type of getActiveElectionUSeCase, getPendingElectionUSeCase,getFinishedElectionUseCase, getElectionsUseCase, getElectionByIdUseCase.
* collected all these flows in their respective fragments

### Future Scope
* Remaining Unit Testing
* Migrate to jetpack compose
* CI/CD PipeLine implementation to automate android release.
* Adding support for Android 12 Material-You theme engine "Monet"
* Adding Functionality to remind user before the election timing
