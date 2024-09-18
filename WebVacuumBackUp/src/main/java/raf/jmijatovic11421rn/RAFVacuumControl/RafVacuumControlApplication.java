package raf.jmijatovic11421rn.RAFVacuumControl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("raf.jmijatovic11421rn.RAFVacuumControl.*")
@ComponentScan(basePackages = { "raf.jmijatovic11421rn.RAFVacuumControl.*" })
@EntityScan("raf.jmijatovic11421rn.RAFVacuumControl.model")
public class RafVacuumControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(RafVacuumControlApplication.class, args);
	}

}
