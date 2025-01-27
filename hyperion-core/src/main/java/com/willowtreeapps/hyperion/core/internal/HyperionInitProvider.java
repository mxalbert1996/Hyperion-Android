package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.core.Hyperion;

public class HyperionInitProvider extends EmptyContentProvider {
    private static final Boolean DEFAULT_INIT = true;

    @Override
    public boolean onCreate() {
        try {
            final Context context = requireContextInternal();
            Hyperion.setApplication(context);

            if(readEnableOnStartMetadata(context)) {
                Hyperion.enable();
            }

            return true;
        } catch (Exception e) {
            Log.e("Hyperion", "Init failed.", e);
            return false;
        }
    }

    private boolean readEnableOnStartMetadata(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;

            if(metaData != null) {
                return metaData.getBoolean(Hyperion.ENABLE_ON_START_METADATA_KEY, DEFAULT_INIT);
            } else {
                return DEFAULT_INIT;
            }
        } catch (PackageManager.NameNotFoundException exception) {
            Log.e("Hyperion", "Init failed.", exception);
            return true;
        }
    }


    @NonNull
    private Context requireContextInternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) return requireContext();
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        return context;
    }
}
