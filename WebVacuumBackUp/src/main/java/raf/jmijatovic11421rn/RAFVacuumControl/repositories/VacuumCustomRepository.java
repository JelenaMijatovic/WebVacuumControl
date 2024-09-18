package raf.jmijatovic11421rn.RAFVacuumControl.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Vacuum;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
@Repository("vacuumRepository")
public class VacuumCustomRepository extends SimpleJpaRepository<Vacuum, Long> implements VacuumRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public VacuumCustomRepository(EntityManager em) {
        super(Vacuum.class, em);
        this.entityManager = em;
    }

    public VacuumCustomRepository(Class<Vacuum> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public List<Vacuum> findAllByAddedBy(String email) {
        return (List<Vacuum>) entityManager.createQuery("SELECT v FROM Vacuum v WHERE v.addedBy = ?1").setParameter(1, email).getResultList();
    }

    @Override
    public Vacuum findByVacuumId(long id) {
        return this.entityManager.find(Vacuum.class, id);
    }

    @Override
    @Transactional
    public <S extends Vacuum> S save(S entity) {
        if (!entityManager.contains(entity)) {
            entityManager.persist(entity);
        }
        return entity;
    }

    @Override
    @Transactional
    public <S extends Vacuum> void update(S entity) {
        entityManager.merge(entity);
    }
    @Override
    @Transactional
    public void lock(Vacuum v) {
        v.setDirty(!v.getDirty());
        entityManager.merge(v);
    }
}
