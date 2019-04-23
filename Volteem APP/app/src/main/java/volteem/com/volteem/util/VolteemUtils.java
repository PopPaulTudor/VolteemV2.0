package volteem.com.volteem.util;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class VolteemUtils extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static Uri parseUri(int ID) {
        Resources resources = mContext.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + resources.getResourcePackageName(ID)
                + '/' + resources.getResourceTypeName(ID) + '/' + resources.getResourceEntryName
                (ID));
    }
}
