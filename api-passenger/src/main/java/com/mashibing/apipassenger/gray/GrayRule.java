package com.mashibing.apipassenger.gray;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import java.util.List;
import java.util.Map;

public class GrayRule extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        Server service = null;
        while (service == null){
            // 获取所有可达的服务
            List<Server> reachableServers = getLoadBalancer().getReachableServers();
            // 获取当前线程的参数 用户id version=1
            Map<String, String> map = RibbonParameters.get();
            String version = "";
            if (map != null && map.containsKey("version")){
                version = map.get("version");
            }
            System.out.println("当前rule version：" + version);
            // 根据用户选服务
            for (int i = 0; i < reachableServers.size(); i++){
                service = reachableServers.get(i);
                // 用户的version我知道了，服务的自定义metadata我不知道
                Map<String, String> metadata = ((DiscoveryEnabledServer) service).getInstanceInfo().getMetadata();
                String version1 = metadata.get("version");
                // 服务的metadata也有了，用户的version也有了
                if (version.trim().equals(version1)){
                    return service;
                }
            }
        }
        return service;
    }
}
