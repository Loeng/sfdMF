package com.ekfans.plugin.webService.monitor.util;

import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.CarUser;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.store.service.ICarUserService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.webService.monitor.MonitorDataConvert;
import com.ekfans.plugin.webService.monitor.MonitorSyncMain;
import com.ekfans.plugin.webService.monitor.util.MonitorSyncConst.PushStatus;

/**
 * 将全部基础数据push到服务端,服务器端收到后会执行添加或更新
 * 这里数据按一条一次的方式是因为传输的数据中包含图片，避免了单次任务量过大
 * 因为该方法可能执行时间很长，所以这里在application对象中加了一个状态值，用于判断当前任务的进度
 */
public class MonitorSyncPushAll {

	// 是否发生异常
	private static boolean flagError = false;
	// web应用层对象
	private static ServletContext app = null;

	// 获取单例
	public static ServletContext getAppInstans(HttpServletRequest request) {
		if (app == null) {
			app = request.getServletContext();
		}
		return app;
	}

	/**
	 * 初始化任务状态
	 */
	public static void initPush() {
		flagError = false;
		app.setAttribute(MonitorSyncConst.PUSH_FLAG, PushStatus.PUSH_ING);
	}

	/**
	 * 获取当前状态的静态方法，初始化状态为
	 * @param request
	 * @return
	 */
	public static PushStatus getCurrPushStatus(HttpServletRequest request) {
		// application对象
		app = getAppInstans(request);
		// 得到application中状态的值,由该值来判断下一步执行的操作
		Object pushFlagObject = app.getAttribute(MonitorSyncConst.PUSH_FLAG);
		if (pushFlagObject == null) {
			// 初始化状态
			app.setAttribute(MonitorSyncConst.PUSH_FLAG, PushStatus.PUSH_NOTTODO);
		}
		// 返回当前状态
		return (PushStatus) app.getAttribute(MonitorSyncConst.PUSH_FLAG);

	};

	/**
	 * 主方法，推送所有数据任务
	 * @param request
	 * @return
	 */
	public static void push(HttpServletRequest request) {
		PushStatus pushStatus = getCurrPushStatus(request);
		// 当为推送完成或推送中，返回不执行任务
		if (PushStatus.PUSH_ING == pushStatus || PushStatus.PUSH_OVER == pushStatus) {
			return;
		}

		// 执行前，初始化该任务
		initPush();

		IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);
		ICarInfoService carInfoService = SpringContextHolder.getBean(ICarInfoService.class);
		ICarUserService carUserService = SpringContextHolder.getBean(ICarUserService.class);

		// 执行器
		// 等价于Executors.newSingleThreadExecutor(); 构造需要的参数直接复制源码的
		ExecutorService executor = new MySyncThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		// ==========公司，所有状态正常的==========
		List<Store> stores = storeService.getAllNormalStoreList();
		for (Store store : stores) {
			Thread thread = new MonitorSyncMain(MonitorDataConvert.initStore(store), "001");
			executor.submit(thread);
		}

		// ==========车辆，所有审核通过的==========
		List<CarInfo> carInfos = carInfoService.getAllCheckedList();
		for (CarInfo carInfo : carInfos) {
			Thread thread = new MonitorSyncMain(MonitorDataConvert.initCarInfo(carInfo), "101");
			executor.submit(thread);
		}

		// ==========车辆人员，所有审核通过的==========
		List<CarUser> carUsers = carUserService.getAllCheckedList();
		for (CarUser carUser : carUsers) {
			Thread thread = new MonitorSyncMain(MonitorDataConvert.initCarUser(carUser), "201");
			executor.submit(thread);
		}

		// 启动监听
		new PushStatusListener(executor).start();
		// 关闭
		executor.shutdown();
	}

	/**
	 * 推送状态监听，用于检查状态
	 * @author 成都易科远见有限公司
	 *
	 */
	static class PushStatusListener extends Thread {
		private ExecutorService exe = null;

		public PushStatusListener(ExecutorService exe) {
			this.exe = exe;
		}

		@Override
		public void run() {
			while (true) {
				// 延迟检查，减少系统开销
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// 中途发生错误，结束所有任务
				if (flagError) {
					// 调用后，线程池处于STOP状态，此时线程池不能接受新的任务，并且会去尝试终止正在执行的任务
					exe.shutdownNow();
					app.setAttribute(MonitorSyncConst.PUSH_FLAG, PushStatus.PUSH_ERROR);
					break;
				} else if (exe.isTerminated()) {// 判断是否结束
					app.setAttribute(MonitorSyncConst.PUSH_FLAG, PushStatus.PUSH_OVER);
					break;
				}
			}
		}
	}

	/**
	 * 定制线程池执行器，记录异常
	 * @author 成都易科远见有限公司
	 *
	 */
	static class MySyncThreadPoolExecutor extends ThreadPoolExecutor {
		public MySyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			if (r instanceof Future<?>) {
				Future<?> f = (Future<?>) r;
				try {
					f.get();
				} catch (Exception e) {
					// 标示错误已发生
					if (!flagError) {
						flagError = true;
					}
					// 记录异常
					if (e.getMessage().indexOf(ConnectException.class.getName()) != -1) {
						app.setAttribute(MonitorSyncConst.PUSH_FLAG, PushStatus.PUSH_NETERROR);
					} else {
						app.setAttribute(MonitorSyncConst.PUSH_FLAG, PushStatus.PUSH_ERROR);
					}
					System.out.println("Sync .. ing has:" + e.getMessage());
				}
			}
		}
	}
}
