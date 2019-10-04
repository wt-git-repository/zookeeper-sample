package com.dev.server;

import org.apache.zookeeper.*;

public class Server {

    private static String connectInfo = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    private static int sessionTimeout = 2000;

    private volatile static ZooKeeper zkClient;

    private static void getConnection() throws Exception{
        if (zkClient == null) {
            synchronized (Server.class) {
                if (zkClient == null) {
                    zkClient = new ZooKeeper(connectInfo, sessionTimeout, new Watcher() {
                        public void process(WatchedEvent watchedEvent) {

                        }
                    });
                }
            }
        }
    }

    private static String regiest(String hostname) throws Exception{
        // 服务器退出，会自动删除节点
        String path = zkClient.create("/servers/test", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname  + " is online");
        return path;
    }

    private static void business() throws Exception {
        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        try {
            // 1、连接Zookeeper 集群
            getConnection();
            // 2、注册节点
            String path = regiest("server");

            // 3、业务逻辑处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
