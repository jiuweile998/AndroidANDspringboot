package com.ch.selfTip.WebSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ch.selfTip.mqtt.MqttSign;

@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {
	String productKey = "a1W4dzPF0g6";
    String deviceName = "wifi";
    String deviceSecret = "b348beb1493051ccb812dcd10c6b6f80";
    MqttSign sign = new MqttSign();
    MqttClient sampleClient = null;
    
   
    /**
     * 当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        System.out.print("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
        
        //sign.calculate(productKey, deviceName, deviceSecret);

        //System.out.println("username: " + sign.getUsername());
       //System.out.println("password: " + sign.getPassword());
        //System.out.println("clientid: " + sign.getClientid());

        //使用Paho连接阿里云物联网平台
        //String port = "443";
        //String broker = "ssl://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;
        String localbroker = "tcp://127.0.0.1:61613";
        MemoryPersistence persistence = new MemoryPersistence();
        try{
            //Paho Mqtt 客户端
            //sampleClient = new MqttClient(broker, sign.getClientid(), persistence);
        	sampleClient = new MqttClient(localbroker, "springserver"+userId, persistence);

            //Paho Mqtt 连接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(180);
            //connOpts.setUserName(sign.getUsername());
            //connOpts.setPassword(sign.getPassword().toCharArray());
            connOpts.setUserName("admin");
            connOpts.setPassword("password".toCharArray());
            sampleClient.connect(connOpts);
            System.out.println("broker: " + localbroker + " Connected");
        }catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            e.printStackTrace();
            System.out.println("excep " + e); 
        }
           try {
            sendMessage("连接成功！");
        } catch (IOException e) {
        	System.out.print("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        try {
   		 sampleClient.disconnect();
   	} catch (MqttException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();}
        System.out.print("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    	System.out.print("用户消息:" + userId + ",报文:" + message);
    	JSONObject jsonObject = JSON.parseObject(message);
        if (!StringUtils.isEmpty(message)) {
            try {
            	
            	if(jsonObject.getString("context").equals("subscribe")) {
            		//String topicReply = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post";
            		String topicReply = "test";
                    try {
            			sampleClient.subscribe(topicReply, new Mqtt3PostPropertyMessageListener());
            		} catch (MqttException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                    System.out.println("subscribe: " + topicReply);
            	}else if(jsonObject.getString("context").equals("publish")) {
            		//Paho Mqtt 消息发布
            	    //String topic = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post";
            		String topic ="test";
            	    String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"body_temp\":"+jsonObject.getInteger("body_temp") +
            	    		",\"wearornot\":"+ jsonObject.getInteger("wearornot")+
            	    		",\"pulse\":"+jsonObject.getInteger("pulse")+
            	    		",\"blood_oxygen_concentration\":"+jsonObject.getDouble("blood_oxygen_concentration") +
            	    		",\"GeoLocation\":{\"longitude\":"
            	    		+jsonObject.getDouble("alongitude")+
            	    		",\"latitude\":"+ jsonObject.getDouble("latitude")+ 
            	    		",\"altitude\":"+jsonObject.getDouble("altitude")+ "}}}";
            	    MqttMessage message1 = new MqttMessage(content.getBytes());
            	    message1.setQos(0);
            	    try {
            			sampleClient.publish(topic, message1);
            		} catch (MqttPersistenceException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		} catch (MqttException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	    System.out.println("publish: " + content);
            	}
                //jsonObject.put("fromUserId", this.userId);
                //String toUserId = jsonObject.getString("toUserId");
               // if (!StringUtils.isEmpty(toUserId) && webSocketMap.containsKey(toUserId)) {
                    //webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                //} else {
                	//System.out.print("请求的 userId:" + toUserId + "不在该服务器上");
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
    	System.out.print("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
    		this.session.getBasicRemote().sendText(message);
    }
    
    class Mqtt3PostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String var1, MqttMessage var2) throws Exception {
            System.out.println("reply topic  : " + var1);
            System.out.println("reply payload: " + var2.toString());
            sendMessage(var2.toString());
        }
    }


    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }
}