package com.ekfans.pub.util.transaction;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.ShopPurview;
import com.ekfans.base.system.service.IShopPurviewService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.todo.model.ToDoModel;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;

/**
 * WebSocke工具类  用户处理web前后端的及时通信
 * @author pp
 * @date 2017年9月7日10:41:53
 */
@ServerEndpoint(value="/websocket",configurator=GetHttpSessionConfigurator.class)
public class WebSocketUtil {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放通过验证的客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketUtil> webSocketSet = new CopyOnWriteArraySet<WebSocketUtil>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //用于存储每个客户端所需要的数据  考虑到不同客户端数据需求的多样性  故使用Object作为通用数据载体
    private Object data;
    
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @throws Exception 
     */
    @OnOpen
    public void onOpen(Session session,EndpointConfig config) throws Exception{
    	//获取HttpSession
    	HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    	//通过HttpSession获取后台管理员信息
    	Manager manager =(Manager) httpSession.getAttribute(SystemConst.MANAGER);
    	//如果管理员信息不为空  
    	if(manager!=null){
    		//获取管理人权限
    		IShopPurviewService shopPurviewService = SpringContextHolder.getBean(IShopPurviewService.class);
    		List<ShopPurview> purviewList = shopPurviewService.getManagerAllShopPurview(manager);
    		//保存管理人权限
    		manager.setShopPurviewList(purviewList);
    		this.session = session;
    		//保存管理员信息
    		this.data = manager;
            webSocketSet.add(this);     //加入set中
            addOnlineCount();           //在线数加1
            System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    	}
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        for(WebSocketUtil item: webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
    /**
     * 三分地后台管理发送待处理事项提示消息
     * @param purview 权限信息
     * @param message 提示消息
     * @throws IOException
     */
    public static void sendSystemMessage(Purview purview,String message) throws IOException{
    	//遍历当前在线的管理员
    	for(WebSocketUtil web:webSocketSet){
    		//获取当前在线的管理员信息
        	Manager manager = (Manager) web.data;
        	//遍历管理员的权限
        	for(ShopPurview shop: manager.getShopPurviewList()){
        		//如果管理员有这项待处理事项的处理权限  发送后台提示
        		if(shop!=null&&shop.getId().equals(purview.getPurviewID())){
        			//包装提示消息
        			ToDoModel todo = new ToDoModel();
        			todo.setExplain(message);
        			todo.setNum(1);
        			todo.setPurview(purview);
        			//使用json字符串的形式发送消息
        			JSONObject jsonObject = new JSONObject(todo);
        			//发送消息
        			web.session.getBasicRemote().sendText(jsonObject.toString());
        			break;
        		}
        	}
    	}
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
    	WebSocketUtil.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
    	WebSocketUtil.onlineCount--;
    }

	public static CopyOnWriteArraySet<WebSocketUtil> getWebSocketSet() {
		return webSocketSet;
	}

	public static void setWebSocketSet(CopyOnWriteArraySet<WebSocketUtil> webSocketSet) {
		WebSocketUtil.webSocketSet = webSocketSet;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    public static void setOnlineCount(int onlineCount) {
		WebSocketUtil.onlineCount = onlineCount;
	}
}