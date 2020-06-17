package top.hellooooo.jobsubmission.util;


import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorUtil {

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void submit(Runnable runnable){
        executorService.submit(runnable);
    }
}
