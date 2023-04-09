package com.zyd.ddz.event;

public abstract class GameTask implements Runnable{

    @Override
    public void run() {
        doAction();
    }

    protected abstract void doAction();
}
