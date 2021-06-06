package org.sterl.testproject.simple;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

@Stateless
class BarActivity {

    @PersistenceUnit
    private EntityManager entityManager;

    public SimpleEntityBE delegateStuff() {
        SimpleEntityBE r = new SimpleEntityBE();
        entityManager.persist(r);
        return r;
    }
}
