package com.dev;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class App {

    private static String connectInfo = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    // 2 秒
    private static int sessionTimeout = 2000;

    private ZooKeeper zkClient;

    @Before
    public void init() throws Exception {
        zkClient = new ZooKeeper(connectInfo, sessionTimeout, new Watcher() {

            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("--------------------------");
                    // 第一个参数是路径，第二个参数是是否监听
                    List<String> children = zkClient.getChildren("/", true);

                    for (String child : children) {
                        System.out.println(child);
                    }
                    System.out.println("--------------------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建节点
     */
    @Test
    public void createNode() throws Exception {

        System.out.println("enter");

        String path = zkClient.create("/dev", "data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(path);
    }

    /**
     * 获取子节点，并监控节点的变化
     */
    @Test
    public void getNode() throws Exception {
        // 第一个参数是路径，第二个参数是是否监听
        List<String> children = zkClient.getChildren("/", true);

        for (String child : children) {
            System.out.println(child);
        }

        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 判断节点是否存在
     */
    @Test
    public void exit() throws Exception {
        Stat stat = zkClient.exists("/dev", false);

        System.out.println(stat == null ? "not exist" : "exist");
    }
}
