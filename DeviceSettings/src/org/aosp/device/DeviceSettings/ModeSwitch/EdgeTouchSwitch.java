/*
 * Copyright (C) 2021 aosp-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aosp.device.DeviceSettings.ModeSwitch;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import org.aosp.device.DeviceSettings.Utils.Utils;

public class EdgeTouchSwitch implements OnPreferenceChangeListener {

    private static final String FILE = "/proc/touchpanel/tpedge_limit_enable";

    private Context mContext;

    public EdgeTouchSwitch(Context context) {
        mContext = context;
    }

    public static String getFile() {
        if (Utils.fileWritable(FILE)) {
            return FILE;
        }
        return null;
    }

    public static boolean isSupported() {
        return Utils.fileWritable(getFile());
    }

    public static boolean isCurrentlyEnabled(Context context) {
        return Utils.getFileValueAsBoolean(getFile(), true);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final boolean isSystemGamingModeActived = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.GAMING_MODE_ACTIVE, 0) == 1;
        final boolean isUnlimitTouchEnabled = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.GAMING_MODE_EDGE_TOUCH, 0) == 1;
        if (!isSystemGamingModeActived || !isUnlimitTouchEnabled) {
            Boolean enabled = (Boolean) newValue;
            Utils.writeValue(getFile(), enabled ? "1" : "0");
        }
        return true;
    }
}
