package com.ekfans.plugin.webService.monitor.util;

import org.springframework.web.context.ContextLoader;

import com.ekfans.plugin.cache.base.Cache;

public interface MonitorSyncConst {
	// 资源磁盘路径
	public static final String REAL_PATH = InitPath.realPath;
	// request业务主体名
	public static final String REQ_BODY = "reqBody";
	// request接口号参数名
	public static final String REQ_SOURCE = "reqSource";

	// 请求地址
	public static final String REQ_URL = Cache.getResource("monitor.url.prefx") + "/data/monitorSync";
	// 获取orgId
	public static final String ORG_ID = Cache.getResource("orgId");
	// 用户密钥
	public static final String KEY = Cache.getResource("key");

	// 返回状态
	public static final boolean SUCCESS = true;
	public static final boolean FAILED = false;

	// 在application对象中保存的状态名
	public static final String PUSH_FLAG = "monitorPushAllFlag";
	// 推送状态
	public static enum PushStatus {
		PUSH_NOTTODO("任务未开始"),
		PUSH_NETERROR("网络错误"),
		PUSH_ERROR("未知错误"),
		PUSH_OVER("推送完毕"),
		PUSH_ING("推送中...");
		private final String state;

		private PushStatus(String state) {
			this.state = state;
		}
		@Override
		public String toString() {
			return state;
		}

	}

	class InitPath {
		public static String realPath = "";
		static {
			realPath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
		}
	}
}
