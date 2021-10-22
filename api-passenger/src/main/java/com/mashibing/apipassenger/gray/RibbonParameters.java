package com.mashibing.apipassenger.gray;

import org.springframework.stereotype.Component;

@Component
public class RibbonParameters {

    private static final ThreadLocal local = new ThreadLocal();

    public static <T> T get(){
        return (T)local.get();
    }

    public static <T> void set(T t){
        local.set(t);
    }
}
