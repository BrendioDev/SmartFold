package com.github.brendio.smartfold;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class SmartFoldBundle {
    private static final @NonNls String BUNDLE = "messages.SmartFoldBundle";
    private static final DynamicBundle INSTANCE = new DynamicBundle(SmartFoldBundle.class, BUNDLE);

    private SmartFoldBundle() {}

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
