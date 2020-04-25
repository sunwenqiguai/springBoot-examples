package com.example.demo.asyncProgram;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 测试feature
 */
public class TestFeature {

    public void testFeatures() throws Exception{
        Future<String> doSomething = Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "success";
        });
        String result = doSomething.get();

        System.out.println(result);
    }

}
