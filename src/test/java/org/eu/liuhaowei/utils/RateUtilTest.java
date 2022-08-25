package org.eu.liuhaowei.utils;

/**
 * @author JavierHouse
 */
public class RateUtilTest {

    @org.junit.Test
    public void getResolution() {
        System.out.println("当前分辨率:" + RateUtil.getResolution());
    }

    @org.junit.Test
    public void getRate() {
        System.out.println("当前设备刷新率:"+ RateUtil.getRate());
    }

    @org.junit.Test
    public void getMaxRate() {
        System.out.println("最大刷新率:" + RateUtil.getMaxRate());
    }

    @org.junit.Test
    public void getMinRate() {
        System.out.println("最小刷新率:" + RateUtil.getMinRate());
    }
}