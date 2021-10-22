package com.mashibing.apipassenger.gray;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;

public class GrayRibbonConfiguration {

    @Bean
    public IRule ribbonRule(){
        return new GrayRule();
    }
}
