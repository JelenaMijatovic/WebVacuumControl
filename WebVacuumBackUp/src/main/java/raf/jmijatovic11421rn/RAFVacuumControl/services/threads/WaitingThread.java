package raf.jmijatovic11421rn.RAFVacuumControl.services.threads;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class WaitingThread extends Thread {

    Long id;
    String action;

    private final Set<WaitingThreadListener> listeners = new CopyOnWriteArraySet<>();

    public WaitingThread(Long id, String action) {
        super();
        this.id = id;
        this.action = action;
    }

    public void addListener(final WaitingThreadListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (WaitingThreadListener listener : listeners) {
            listener.notify(this, id, action);
        }
    }

    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }

    public abstract void doRun();
}
