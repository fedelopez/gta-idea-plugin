package org.gta;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

/** @author fede lopez */
public enum GTARunTestType {
    RUN_UNIT_TESTS, RUN_FUNCTION_TESTS, RUN_LOAD_TESTS;

    public static GTARunTestType valueFromPackageName(@NotNull String packageName) {
        if (StringUtil.isEmptyOrSpaces(packageName)) {
            return null;
        }
        if (packageName.endsWith(".functiontest")) {
            return RUN_FUNCTION_TESTS;
        } else if (packageName.endsWith(".loadtest")) {
            return RUN_LOAD_TESTS;
        } else {
            return RUN_UNIT_TESTS;
        }
    }
}
