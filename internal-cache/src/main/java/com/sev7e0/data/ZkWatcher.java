package com.sev7e0.data;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ZkWatcher {
    private static final String CONNECTSTRING = "localhost:2181";
    private static final int SESSIONTIMEOUT = 200;
    private static final String PARENTNODE = "/rootNode";
    private static final String LOCKNODE = PARENTNODE+"/lock";

    private CuratorFramework client;

    @PostConstruct
    private void connect(){
        ExponentialBackoffRetry backoffRetry = new ExponentialBackoffRetry(SESSIONTIMEOUT, 3);

        client = CuratorFrameworkFactory.newClient(CONNECTSTRING, backoffRetry);
        client.start();
    }




}
