package raf.jmijatovic11421rn.RAFVacuumControl.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import raf.jmijatovic11421rn.RAFVacuumControl.model.User;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
@Repository("userRepository")
public class UserCustomRepository extends SimpleJpaRepository<User, Long> implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserCustomRepository(EntityManager em) {
        super(User.class, em);
        this.entityManager = em;
    }

    public UserCustomRepository(Class<User> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public User findUserByEmail(String email) {
        return entityManager.find(this.getDomainClass(), email);
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        entityManager.remove(entityManager.find(this.getDomainClass(), email));
    }

    @Override
    public List<User> findAll() {
        return (List<User>) entityManager.createQuery("SELECT u FROM User u").getResultList();
    }

    @Override
    public Page<User> findAll(Pageable page) {
        List<User> users;
        users = entityManager.createQuery("SELECT u FROM User u")
                    .setFirstResult(page.getPageNumber() * page.getPageSize())
                    .setMaxResults(page.getPageSize()).getResultList();
        return new PageImpl<>(users);
    }

    @Override
    @Transactional
    public <S extends User> S save(S entity) {
        if (!entityManager.contains(entity)) {
            entityManager.persist(entity);
        }
        return entity;
    }

    @Override
    @Transactional
    public <S extends User> S update(S entity) {
        return entityManager.merge(entity);
    }


}
