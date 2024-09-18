package raf.jmijatovic11421rn.RAFVacuumControl.services.threads;

public class StatusChangeThread extends WaitingThread {

    public StatusChangeThread(Long id, String action) {
        super(id, action);
    }

    @Override
    public void doRun() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
