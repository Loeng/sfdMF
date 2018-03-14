package com.ekfans.plugin.cache.base;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.ekfans.plugin.cache.engine.CacheEngine;
import com.ekfans.plugin.cache.engine.DefaultCacheEngine;
import com.ekfans.plugin.cache.engine.MemCachedEngine;
import com.ekfans.pub.util.DateUtil;

public class CacheTest {
	public static void test1() {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));

		MemcachedClient memcachedClient;
		try {
			memcachedClient = builder.build();

			String startDate = DateUtil.getSysDateTimeString();
			for (int i = 0; i < 10000; i++) {
				// memcachedClient.add("a" + i, 0, "aaaa11111");

				//System.out.println(memcachedClient.get("a" + i));
			}
			memcachedClient.shutdown();

			String endDate = DateUtil.getSysDateTimeString();
			System.out.println(startDate + "==========" + endDate);

			// // close memcached client
			//
			//
			// MemcachedClientBuilder builder2 = new
			// XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));
			//
			// MemcachedClient memcachedClient2;
			// memcachedClient = builder.build();
			//
			// String valueA = memcachedClient.get("a");
			// String valueB = memcachedClient.get("b");
			// String valueC = memcachedClient.get("c");
			//
			// System.out.println("111111111111111");
			// System.out.println("hello=" + valueA);
			// System.out.println("111111111111111");
			//
			// System.out.println("22222222222222222");
			// System.out.println("hello=" + valueB);
			// System.out.println("22222222222222222");
			//
			// System.out.println("3333333333333333333");
			// System.out.println("hello=" + valueC);
			// System.out.println("3333333333333333333");

		} catch (IOException e) {

			System.err.println("Shutdown MemcachedClient fail");

			e.printStackTrace();

		}

	}

	public static void test2() {
		CacheEngine engine = null;
		try {
			engine = new MemCachedEngine();
			engine.init();
			System.out.println("11111111");
			engine.stop();
		} catch (Exception e) {
			engine = new DefaultCacheEngine();
			System.out.println("222222222");
		}
	}

	public static void test3() {
		CacheEngine engine = null;
		try {
			engine = new MemCachedEngine();
			engine.init();
			engine.set("test1", "测试缓存");
			System.out.println("11111111");
			engine.stop();
		} catch (Exception e) {
			engine = new DefaultCacheEngine();
			System.out.println("222222222");
		}
	}

	public static void test4() {
		CacheEngine engine = null;
		try {
			engine = new MemCachedEngine();
			engine.init();
			System.out.println("=========" + engine.get("test1") + "=============");
			engine.remove("test1");
			engine.removeAll();
			engine.stop();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		String aa = "    asda    aaa      ";
		System.out.println("++" + aa.trim() + "++");
	}
}
