# Add project specific ProGuard rules here.

# Keep Hilt
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Keep Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Coil
-dontwarn coil.**

# Keep Jsoup
-keep public class org.jsoup.** { *; }

# Keep data classes
-keep class com.fabmax.technews.domain.model.** { *; }
-keep class com.fabmax.technews.data.local.entity.** { *; }
