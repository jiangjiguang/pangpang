package com.pangpang.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 *
 * @author zhengbaiyun
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
    private static Log logger = LogFactory.getLog(SpringContextHolder.class);
    private static CountDownLatch latch = new CountDownLatch(1);
    private static Lock lock = new ReentrantLock();

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            if (lock.tryLock()) {
                try {
                    if (applicationContext == null) {
                        logger.warn("Waiting for applicaitonContext to inject ...");
                        while (applicationContext == null) {
                            Threads.sleep(1);
                        }
                    }
                } finally {
                    latch.countDown();
                    lock.unlock();
                }
            }
            try {
                latch.await();
//                if (!latch.await(60, TimeUnit.SECONDS)) {
//                    throw new RuntimeException("Waited for applicationContext to inject, but timed out (60s).");
//                }
            } catch (InterruptedException ex) {
                // ignored
            }
        }
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        return getApplicationContext().getBean(requiredType);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        logger.debug("Clean applicationContext: " + applicationContext);
        applicationContext = null;
    }

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        logger.warn("applicationContext is injected, " + applicationContext);
        if (SpringContextHolder.applicationContext != null) {
            logger.warn("applicationContext already exists.");
        }
        SpringContextHolder.applicationContext = applicationContext;
    }
}
