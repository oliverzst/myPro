package com.actec.bsms.backend;

import com.actec.bsms.backend.vo.Receivable;

import java.util.concurrent.BlockingQueue;

/**
 * Created by wdl on 2017/1/7.
 */
public abstract class Task implements Runnable {
    protected boolean running;
    protected BlockingQueue<Receivable> queue;

    public void stopService() {
        this.running = false;
    }


    public abstract void cleanup();

    public Task(BlockingQueue<Receivable> queue) {
//        Thread.currentThread().setDaemon(true);
        this.queue = queue;
    }
}
