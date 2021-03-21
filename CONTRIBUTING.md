# Contributing to the project

All contributions to the project should follow the following procedure:

- Create an issue. Check out [best practices](#best-practices) and [some don'ts](#some-donts)
- Create a merge request (MR). Check out [best practices](#best-practices) and [some don'ts](#some-donts)

## Table of Contents

- [Installation](#installation)
- [Creating an issue](#creating-an-issue)
- [Creating a merge request](#creating-a-merge-request)
- [Set up instructions](#set-up-instructions)
- [Sync instructions - local repository with upstream](#sync-instructions)
- [Best practices](#best-practices)
- [Some don'ts](#some-donts)

## Installation

Make sure you have the latest version of [Android Studio](https://developer.android.com/studio) and [JavaSE](https://www.oracle.com/technetwork/java/javase/downloads/index.html) installed. We strongly recommend you to update all the libraries and sync the project (might take a minute).

## Creating an issue

If you find a bug in a project you’re using, or has a question about the project – create an issue!

For creating an issue, it is very **important** to follow the [best practices](#best-practices) and [some don'ts](#some-donts)

## Creating a Merge Request

If you’re able to patch a bug or add a feature, make a merge request (to the develop branch) with the code! Once you’ve submitted a merge request (to the develop branch) the maintainer(s) can compare your branch to the existing one and decide whether or not to incorporate (pull in) your changes.

Once you’ve opened a merge request a discussion will start around your proposed changes. Other contributors and users may comment on the merge request, but ultimately the decision is made by the maintainer(s). You may be asked to make some changes to your merge request, if so, add more commits to your branch and push them – they’ll automatically go into the existing merge request.

For creating a merge request, it is very **important** to follow the [best practices](#best-practices) and [some don'ts](#some-donts)

## Set up instructions

1. Fork the repo
2. Clone your fork
3. Sync your fork with 'upstream' by following these [instructions](#sync-instructions-local-repository-with-upstream)
4. Create a branch
5. Push your changes to your fork with `git push`
6. Create a merge request.
7. Iterate on the solution.
8. Get merged! 🎉 🎊

## Sync Instructions local repository with upstream

1. Connect your local to the original ‘upstream’ repository by adding it as a remote.
2. Pull in changes from ‘upstream’ often so that you stay up to date so that when you submit your merge request, merge conflicts will be less likely.

## Best practices

1. Try to do a root cause analysis for the issue, if applicable.
2. Reference the issue being fixed in the corresponding MR.
3. Use meaningful commit messages in a MR.
4. Use one commit per task. Do not over commit (add unnecessary commits for a single task) or under commit (merge 2 or more tasks in one commit).
5. Add screenshot(s)/a short video in case the changes made in the MR result in change in the UI of the application (Video Rec./GIF/Screenshot should of resolution around 360-540pixels).
6. The author must follow the templates defined by the maintainers of the repository while opening any issue/MR.
7. Adhere to the code styles of the project (we use Square Android for our project coding style).
8. Close the issue as soon as the corresponding MR is merged.
  
## Some don'ts

1. Send a MR without an existing issue.
2. Fix an issue that is assigned to somebody else and submit a MR before the assignee does.
3. Report issues which are previously reported by others. (Please check both the open and closed issues).
4. Suggest unnecessary or completely new features in the issue list.
5. Add unnecessary spacing or indentation to the code.

If you face **any** problems, feel free to ask our community at [Gitter](https://gitter.im/AOSSIE/Agora-Android) 
