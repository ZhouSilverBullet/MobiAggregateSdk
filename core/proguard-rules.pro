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
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

## 指定不去忽略非公共的库的类
#-dontskipnonpubliclibraryclasses
#
## 指定不去忽略非公共的库的类的成员
#-dontskipnonpubliclibraryclassmembers
#
# 不做预校验，可加快混淆速度
# preverify是proguard的4个步骤之一
# Android不需要preverify，去掉这一步可以加快混淆速度
-dontpreverify
#
# 不优化输入的类文件
-dontoptimize

# 混淆时生成日志文件，即映射文件
-verbose
#
# 指定映射文件的名称
-printmapping proguardMapping.txt
#
##混淆时所采用的算法
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
# 保护代码中的Annotation不被混淆
#-keepattributes *Annotation*
#
#-dontwarn android.support.annotation.Keep
#-keep @android.support.annotation.Keep class *
#
## 忽略警告
#-ignorewarning
#-ignorewarnings
#-keep class * { public private *;}

#-keepattributes EnclosingMethod
#
# 保护泛型不被混淆
#-keepattributes Signature
#
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#
##-----------需要保留的东西--------------
# 保留所有的本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
## 保留了继承自Activity、Application、Fragment这些类的子类
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View


-keep class com.mobi.core.common.MobiPubSdk {
public <methods>;
}

-keep class com.mobi.core.ConstantValue {*;}
-keep class com.mobi.core.listener.* {*;}

-keep class com.mobi.core.splash.* {*;}
-keep class com.mobi.core.utils.LogUtils {*;}

-keep class com.mobi.core.IAdProvider {*;}

-keepattributes InnerClasses
-keep public class com.mobi.core.AdParams$* {
public <methods>;
}
-keep class com.mobi.core.AdParams {
public <methods>;
}

-keep class com.mobi.core.AdProviderManager$* {
*;
}
-keep class com.mobi.core.AdProviderManager {*;}
-keep class com.mobi.core.BaseAdProvider {*;}
-keep class com.mobi.core.BaseCallbackProvider {*;}


-keep class com.mobi.core.IAdSession {*;}
#-keep class com.mobi.core.FakeAdSession {*;}
-keep class com.mobi.core.strategy.AdRunnable {
<methods>;
}
#-keep class com.mobi.core.strategy.AdRunnable$ExecCallback{*;}
-keep class com.mobi.core.LocalAdParams {
public <methods>;
}

-keep class com.mobi.core.strategy.IShowAdStrategy {*;}

-keep class com.mobi.core.db.AdProvider {*;}
-keep class com.mobi.core.feature.* {*;}

-keep class com.mobi.core.strategy.StrategyError {
public <methods>;
}

-keep class *.R -keepclasseswithmembers class **.R$* { public static <fields>;}
#-keep class com.mobi.** {*;}