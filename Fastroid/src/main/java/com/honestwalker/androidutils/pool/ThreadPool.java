package com.honestwalker.androidutils.pool;

import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池工具
 * @author honestwalker
 *
 */
public class ThreadPool {
	
	/**
	 * 主线程池，自动增长
	 */
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	/**
	 * 定长全程线程池，主要用于优先级不高，频繁加载的后台线程
	 */
	private static ExecutorService backgroundExecutorService = Executors.newFixedThreadPool(20);

	public static void threadPool(Runnable runnable) {
		threadPool.submit(runnable);
	}

	public static void threadPool(Object id , Runnable runnable) {
		threadPool.submit(runnable);
	}
	
	public static void threadBackgroundPool(Runnable runnable) {
		backgroundExecutorService.submit(runnable);
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	public static <V> V threadPool(Callable<V> task) {
		Future<V> future = threadPool.submit(task);
		try {
			return future.get();
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return null;
	}
	
}
