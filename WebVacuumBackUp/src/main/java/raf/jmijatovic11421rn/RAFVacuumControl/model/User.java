package raf.jmijatovic11421rn.RAFVacuumControl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
public class User implements Serializable {

    public User() {

    }

    @Id
    @Column(unique=true)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column
    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @Column
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnoreProperties("users")
    @Column
    @ManyToMany
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "perm_id"))
    private Set<Permission> permissions;

}
