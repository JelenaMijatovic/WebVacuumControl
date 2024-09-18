package raf.jmijatovic11421rn.RAFVacuumControl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ScheduledTask {

    private Timestamp time;
    private String action;
    private long vacuumId;
}
