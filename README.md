# zookeeper-sample
zookeeper、docker、Java


# 集群启动命令
```bash
COMPOSE_PROJECT_NAME=zk_test docker-compose up
```

# docker 作为客户端连接

```bash
docker run -it --rm \
>         --link zoo1:zk1 \
>         --link zoo2:zk2 \
>         --link zoo3:zk3 \
>         --net zktest_default \
>         --name zkClient \
>         zookeeper
```

进入容器执行
```bash
zkCli.sh -server zk1:2181,zk2:2181,zk3:2181
```
