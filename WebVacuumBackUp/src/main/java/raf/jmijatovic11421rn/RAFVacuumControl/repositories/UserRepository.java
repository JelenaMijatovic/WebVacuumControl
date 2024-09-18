package raf.jmijatovic11421rn.RAFVacuumControl.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import raf.jmijatovic11421rn.RAFVacuumControl.model.User;

import javax.persistence.LockModeType;
import java.util.List;

@NoRepositoryBean
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends User> S update(S entity);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

}
