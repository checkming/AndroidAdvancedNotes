// -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
//
// This file is util codes for android developmen

package com.arch.demo.core.utils;

import android.annotation.TargetApi;
import android.content.SharedPreferences.Editor;
import android.os.Build;

public class EditorUtils {

    public static void fastCommit(final Editor editor) {
        // edit.apply could not commit your preferences changes in time on
        // Android 4.3
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            GingerbreadCompatLayer.fastCommit(editor);
        } else {
            // FIXME: there's no fast commit below GINGERBREAD.
            editor.commit();
        }
    }

    @TargetApi(9)
    private static class GingerbreadCompatLayer {
        public static void fastCommit(final Editor editor) {
            editor.apply();
        }
    }
}
