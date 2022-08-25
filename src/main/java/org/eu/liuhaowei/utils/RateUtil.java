package org.eu.liuhaowei.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author JavierHouse
 */
public class RateUtil {

    /**
     * 配置刷新率
     */
    @SneakyThrows
    public static void setRate(String rate) {
        Runtime.getRuntime()
                .exec(new String[]{"/bin/bash", "-c", StrFormatter.format("DISPLAY=:0 xrandr --output eDP-1 --mode {} --rate {}", getResolution(), rate)});
    }

    @SneakyThrows
    private static List<String> getRates(boolean b) {
        return getRowRateList()
                .map(splitTrim -> {
                    splitTrim.removeFirst();
                    if (!b) {
                        return splitTrim.stream()
                                .map(s -> StrUtil.removeAll(s, '*', '+'))
                                .collect(Collectors.toList());
                    }
                    return splitTrim;
                })
                .orElse(new ArrayList<>());
    }

    /**
     * 获取当前分辨率
     */
    public static String getResolution() {
        return getRowRateList().map(LinkedList::getFirst).orElseThrow(() -> new RuntimeException("读取分辨率失败"));
    }

    /**
     * 获取当前刷新率集合
     *
     * @return
     */
    private static Optional<LinkedList<String>> getRowRateList() {
        return getRowRate().map(string -> new LinkedList<>(StrSplitter.splitTrim(string, " ", Boolean.TRUE)));
    }

    /**
     * 获取当前刷新率
     *
     * @return
     */
    private static Optional<String> getRowRate() {
        return getAllRates()
                .stream().filter(string -> StrUtil.contains(string, "*"))
                .findFirst();
    }

    /**
     * 获取所有的刷新率数据
     *
     * @return
     */
    private static List<String> getAllRates() {
        final Process exec = getProcess();
        final LinkedList<String> lines = IoUtil.readUtf8Lines(exec.getInputStream(), new LinkedList<>());
        final List<String> list = new ArrayList<>();
        boolean flag = Boolean.FALSE;
        for (String line : lines) {
            if (line.contains("eDP-1")) {
                flag = Boolean.TRUE;
                continue;
            }
            if (flag) {
                if (StrUtil.startWith(line, " ")) {
                    list.add(line);
                }
                else {
                    break;
                }

            }
        }
        return list;
    }

    @SneakyThrows
    private static Process getProcess() {
        return Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "DISPLAY=:0 xrandr -q"});
    }

    /**
     * 获取当前设备刷新率
     *
     * @return
     */
    public static String getRate() {
        return getRates(Boolean.TRUE)
                .stream()
                .filter(s -> StrUtil.contains(s, "*"))
                .map(s -> StrUtil.removeAll(s, '*', '+'))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("读取刷新率失败"));
    }

    /**
     * 获取当前设备最大刷新率
     *
     * @return
     */
    public static String getMaxRate() {
        return getRates(Boolean.FALSE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("读取刷新率失败"));
    }

    /**
     * 获取当前设备最小刷新率
     *
     * @return
     */
    public static String getMinRate() {
        return getRates(Boolean.FALSE)
                .stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new RuntimeException("读取刷新率失败"));
    }


}