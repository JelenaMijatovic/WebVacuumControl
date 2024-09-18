package raf.jmijatovic11421rn.RAFVacuumControl.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Vacuum;

import javax.persistence.LockModeType;
import java.util.List;

@NoRepositoryBean
public interface VacuumRepository extends CrudRepository<Vacuum, Long> {

    List<Vacuum> findAll(Specification<Vacuum> spec);
    List<Vacuum> findAllByAddedBy(String email);
    Vacuum findByVacuumId(long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Vacuum> void update(S entity);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void lock(Vacuum v);
}
