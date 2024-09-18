package raf.jmijatovic11421rn.RAFVacuumControl.services.threads;

public interface WaitingThreadListener {
    void notify(final Thread thread, Long id, String action);
}