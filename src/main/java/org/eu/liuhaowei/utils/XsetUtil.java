package org.eu.liuhaowei.utils;

import lombok.SneakyThrows;

/**
 * @author JavierHouse
 */
public class XsetUtil {

    @SneakyThrows
    public static void forceOff() {
        Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "xset dpms force off"});
    }

}