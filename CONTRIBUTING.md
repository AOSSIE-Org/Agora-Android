# Contributing to the project

All contributions to the project should follow the following procedure:

## Table of Contents

- [Installation](#installation)
- [Creating an issue](#creating-an-issue)
- [Creating a merge request](#creating-a-merge-request)
- [Set up instructions](#set-up-instructions)
- [Best practices](#best-practices)
- [Some don'ts](#some-donts)
- [Setting up appetize.io](setting-up-appetize.io)
- [Signed apk setup for pipeline](#signed-apk-setup-for-pipeline)

## Installation

Make sure you have the latest version of [Android Studio](https://developer.android.com/studio) installed.

## Creating an issue

If you find a bug in a project you’re using, or has a question about the project – create an issue!

For creating an issue, it is very **important** to follow the [best practices](#best-practices) and [some don'ts](#some-donts)

## Creating a Merge Request

If you’re able to patch a bug or add a feature, make a merge request (to the develop branch) with the code! Once you’ve submitted a merge request (to the develop branch)

Once you’ve opened a merge request a discussion will start around your proposed changes. Other contributors and users may comment on the merge request, but ultimately the decision is made by the maintainer(s). You may be asked to make some changes to your merge request, if so, add more commits to your branch and push them – they’ll automatically go into the existing merge request.

For creating a merge request, it is very **important** to follow the [best practices](#best-practices) and [some don'ts](#some-donts)

## Set up instructions

1. Fork the repo
2. Clone your fork
3. Sync your fork with 'upstream' by following these [instructions](#sync-instructions-local-repository-with-upstream)
4. Add facebook login [configuration](#Running-the-application)
5. Setup your [appetize](#Setting-up-appetize.io)

## Sync Instructions local repository with upstream

1. Connect your local to the original ‘upstream’ repository by adding it as a remote.
2. Pull in changes from ‘upstream’ often so that you stay up to date so that when you submit your merge request, merge conflicts will be less likely.

## Best practices

1. Try to do a root cause analysis for the issue, if applicable.
2. Reference the issue being fixed in the corresponding MR.
3. Use meaningful commit messages in a MR.
4. Use one commit per task. Do not over commit (add unnecessary commits for a single task) or under commit (merge 2 or more tasks in one commit).
5. Add screenshot/short video in case the changes made in the MR, are being reflected in the UI of the application.
6. add appetize link to MRs

## Some don'ts

1. Send a MR without an existing issue.
2. Fix an issue assigned to somebody else and submit a MR before the assignee does.
3. Report issues which are previously reported by others. (Please check both the open and closed issues).
4. Suggest unnecessary or completely new features in the issue list.
5. Add unnecessary spacing or indentation to the code.

If you face **any** problems, feel free to ask our community at [Gitter](https://gitter.im/AOSSIE/Agora-Android)

## Firebase API key
1. Go to Firebase console and add a new project.
2. download and paste google-services.json file inside  /app
3. Go to the Crashlytics dashboard in the Firebase console.
4. Make sure your app is selected from the dropdown next to Crashlytics at the top of the page.
5. Click Enable Crashlytics.


## Running the application
1) Update your local.properties with this variables
      1.1) "secretKey" and enter your personal secret key
      1.2) "serverKey" and enter the server key provided by firebase
      
2) CI/CD variables
      2.1) "SECRET_KEY" and enter your personal secret key
      2.2) "FIREBASE_JSON" and enter your google-services.json file content
      2.3) "SERVER_KEY" and enter the server key provided by firebase
      
To use Facebook login, get your fb_api_key and fb_api_scheme from [here](https://developers.facebook.com/apps/).And then add these following variables.
   1) Update your local.properties with these two variables
      1.1) "fbApiKey" and enter your fb api key
      1.2) "fbApiScheme" and enter your fb api scheme

   2) CI/CD variables
      2.1) "FB_API_KEY" and enter your fb api key
      2.2) "FB_API_SCHEME" and enter your fb api scheme

**Note:** Don't forget to add "fb" before your fb_login_protocol_scheme. <br>

( As of now a contributer can also use random values of keys.)

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
    Make sure to make both the variables protected and make your branch protected too. Follow this guide: https://docs.gitlab.com/ee/user/project/protected_branches.html#configuring-protected-branches

    This is a one time setup, subsequent changes you make in your repository will be reflected in your link you got in the response automatically.

## Signed apk setup for pipeline

Follow the following steps :-

   1) Generate a keystore.jks file using keytool. keytool -genkeypair -v -keystore keystore.jks -alias YOUR-KEY-ALIAS -keyalg RSA -keysize 2048 -validity 10000 Replace YOUR-KEY-ALIAS with your key alias.
      or you can generate a keystore file using android studio.
   2) run this command in your terminal and copy the output  "base64 -w 0 KEY-NAME.jks" Replace KEY-NAME with the your key name.
   3) Add the following CI/CD variables:-
      3.1) "KEYSTORE" and paste the copied output.
      3.2) "KEY_PASSWORD" and enter you key password.
      3.3) "KEY_ALIAS" and enter you key alias.
      3.4) "KEYSTORE_PASSWORD" and enter your keystore password.

   Make these CI variables Protected also don't forget to make your branch Protected.