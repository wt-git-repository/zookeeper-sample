package com.dev.client;

import com.dev.server.Server;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private static String connectInfo = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    private static int sessionTimeout = 2000;

    private volatile static ZooKeeper zkClient;

    private static void getConnection() throws Exception {
        if (zkClient == null) {
            synchronized (Server.class) {
                if (zkClient == null) {
                    zkClient = new ZooKeeper(connectInfo, sessionTimeout, new Watcher() {
                        public void process(WatchedEvent watchedEvent) {
                            try {
                                getChildren();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    private static void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/servers", true);

        // 服务器的节点主机名称
        List<String> hosts = new ArrayList<String>();

        for (String child : children) {
            byte[] data = zkClient.getData("/servers/" + child, false, null);

            hosts.add(new String(data));
        }

        System.out.println(hosts);
    }

    private static void business() throws Exception {
        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        try {
            // 获取Zookeeper 连接
            getConnection();
            // 注册监听
            zkClient.getChildren("/servers", true);
            // 业务逻辑处理
            business();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
