# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#DataStore
-keep class androidx.datastore.*.** {*;}

#Piccaso
-dontwarn com.squareup.okhttp.**

#Facebook Login
-keepnames class com.facebook.FacebookActivity
-keepnames class com.facebook.CustomTabActivity
-keep class com.facebook.login.Login
-keep class com.facebook.android.*
-keep class android.webkit.WebViewClient
-keep class * extends android.webkit.WebViewClient
-keepclassmembers class * extends android.webkit.WebViewClient {
    <methods>;
}

#LeakCanary
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# Marshmallow removed Notification.setLatestEventInfo()
-dontwarn android.app.Notification
# Mockito
-dontwarn org.mockito.**

## Kotlin Coroutine
-keepnames class kotlinx.** { *; }

# Navigation Architecture
-keep class * extends androidx.fragment.app.Fragment{}
-keepnames class * extends android.os.Parcelable
 -keepnames class * extends java.io.Serializable


#Room DataBase
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**


# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit
# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.-KotlinExtensions

### OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
#-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


# LifeCycle Component
-keep class * implements androidx.lifecycle.LifecycleObserver {
    <init>(...);
}

# ViewModel's empty constructor is considered to be unused by proguard
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

-keepattributes Signature
-keepattributes *Annotation*

