fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew install fastlane`

# Available Actions
## Android
### android lint
```
fastlane android lint
```
KtLint Check
### android test
```
fastlane android test
```
Runs all the tests
### android build_debug
```
fastlane android build_debug
```
Build with debug apk
### android build_release
```
fastlane android build_release
```
Build with release apk

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
