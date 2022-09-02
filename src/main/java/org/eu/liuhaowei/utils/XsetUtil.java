package org.eu.liuhaowei.utils;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;

/**
 * @author JavierHouse
 */
public class XsetUtil {

    @SneakyThrows
    public static void forceOff() {
        Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "xset dpms force off"});
    }

    /**
     * 监视器打开状态
     * @return
     */
    @SneakyThrows
    public static boolean monitorStatus() {
        return !StrUtil.containsIgnoreCase(RuntimeUtil.execForStr("/bin/bash", "-c", "DISPLAY=:0 xset -q |grep Monitor"), "off");
    }

}