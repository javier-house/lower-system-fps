package org.eu.liuhaowei;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import lombok.SneakyThrows;
import org.eu.liuhaowei.utils.RateUtil;
import org.eu.liuhaowei.utils.XsetUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author JavierHouse
 */
public class GlobalListenerFps {


    private static final boolean forceOffSwitch = Boolean.FALSE;

    private static final long touchSlackMs = 5;
    private static final long forceOffMs = 30;

    private static final String maxRate = RateUtil.getMaxRate();
    private static final String minRate = RateUtil.getMinRate();

    private static final AtomicLong longTime = new AtomicLong(System.currentTimeMillis());

    private static final ThreadPoolExecutor touchSlackExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());
    private static final ThreadPoolExecutor forceOffExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());


    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception ex) {
            System.out.println("There was a problem registering the native hook.");
            System.out.println(ex.getMessage());

            System.exit(1);
        }
        // Add the appropriate listeners.
        final NativeMouseInputListener listener = new NativeMouseInputListener() {

            public void nativeMouseClicked(NativeMouseEvent e) {
                addExecutor();
            }

            public void nativeMousePressed(NativeMouseEvent e) {
                addExecutor();
            }

            public void nativeMouseReleased(NativeMouseEvent e) {
                addExecutor();
            }

            public void nativeMouseMoved(NativeMouseEvent e) {
                addExecutor();
            }

            public void nativeMouseDragged(NativeMouseEvent e) {
                addExecutor();
            }

        };
        GlobalScreen.addNativeMouseListener(listener);
        GlobalScreen.addNativeMouseMotionListener(listener);

        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            public void nativeKeyPressed(NativeKeyEvent e) {
                addExecutor();
            }

            public void nativeKeyReleased(NativeKeyEvent e) {
                addExecutor();
            }

            public void nativeKeyTyped(NativeKeyEvent e) {
                addExecutor();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                addExecutor();
            }
        }, 1000);
    }

    @SneakyThrows
    private static void addExecutor() {
        longTime.set(System.currentTimeMillis());
        touchSlackExecutor.execute(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                if (!StrUtil.equals(RateUtil.getRate(), maxRate)) {
                    RateUtil.setRate(maxRate);
                    System.out.println(DateUtil.now() + " " + StrFormatter.format("调整fps->{}", maxRate));
                }
                TimeUnit.SECONDS.sleep(touchSlackMs);
                if (System.currentTimeMillis() - 10 > longTime.get() + TimeUnit.SECONDS.toMillis(touchSlackMs) && !StrUtil.equals(RateUtil.getRate(), minRate)) {
                    RateUtil.setRate(minRate);
                    System.out.println(DateUtil.now() + " " + StrFormatter.format("调整fps->{}", minRate));
                    if (forceOffSwitch) {
                        //进入屏幕
                        forceOffExecutor.execute(new Runnable() {
                            @Override
                            @SneakyThrows
                            public void run() {
                                TimeUnit.SECONDS.sleep(forceOffMs);
                                if (System.currentTimeMillis() - 10 > longTime.get()
                                        + TimeUnit.SECONDS.toMillis(touchSlackMs)
                                        + TimeUnit.SECONDS.toMillis(forceOffMs)
                                ) {
                                    XsetUtil.forceOff();
                                    System.out.println(DateUtil.now() + " " + StrFormatter.format("熄屏"));
                                }
                            }
                        });
                    }

                }
            }
        });

    }



}