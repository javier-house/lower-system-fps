package org.eu.liuhaowei;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import org.junit.Test;

/**
 * @author JavierHouse
 */
public class GlobalListenerFpsTest {
    @Test
    public void registerNativeHook() throws NativeHookException {
        GlobalScreen.registerNativeHook();
    }
}