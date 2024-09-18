package raf.jmijatovic11421rn.RAFVacuumControl.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Timestamp date;

    @Column
    private Long vacuumId;

    @Column
    private String action;

    @Column
    private String errorText;

    public ErrorMessage() {

    }

    public ErrorMessage(Timestamp date, Long vacuumId, String action, String errorText) {
        this.date = date;
        this.vacuumId = vacuumId;
        this.action = action;
        this.errorText = errorText;
    }
}
