package raf.jmijatovic11421rn.RAFVacuumControl.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Vacuum {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long vacuumId;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String addedBy;

    @Column
    private Date creationDate;

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    private Integer cycles;

    @Column(columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean dirty;
}
