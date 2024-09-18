package raf.jmijatovic11421rn.RAFVacuumControl.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Getter
@Setter
public class Permission {

    @Id
    @Column(unique=true)
    private String permission;

    @JsonIgnoreProperties("permissions")
    @ManyToMany(mappedBy = "permissions")
    private Set<User> users;

    public Permission() {

    }

    public Permission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Permission) && (permission.equals(((Permission) o).permission));
    }

    @Override
    public int hashCode() {
        return permission.hashCode();
    }
}
