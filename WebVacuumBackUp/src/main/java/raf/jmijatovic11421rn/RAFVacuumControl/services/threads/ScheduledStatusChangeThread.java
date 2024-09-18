package raf.jmijatovic11421rn.RAFVacuumControl.services.threads;

public class ScheduledStatusChangeThread extends WaitingThread implements Runnable {

    public ScheduledStatusChangeThread(Long id, String action) {
        super(id, action);
    }

    @Override
    public void doRun() {
        System.out.println("Performing scheduled " + action + " on vacuum " + id + "...");
    }
}
