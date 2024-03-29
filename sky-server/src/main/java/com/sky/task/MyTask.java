package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class MyTask {

    /*@Scheduled(cron = "* 0/1 * * * *")
    public void doTask() {
        log.info("执行定时任务：{}", new Date());
    }*/
}
