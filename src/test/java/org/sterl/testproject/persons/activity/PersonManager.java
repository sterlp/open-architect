package org.sterl.testproject.persons.activity;

import org.sterl.testproject.addresses.activity.AddressManager;
import org.sterl.testproject.persons.dao.PersonDao;
import org.sterl.testproject.persons.model.PersonBE;

public class PersonManager {

    AddressManager adressManager;
    PersonDao personDao;
    
    
    public PersonBE createNew(PersonBE e) {
        adressManager.addOrUpdate(e.getAdress());
        PersonBE result = personDao.save(e);
        return result;
    }
}
