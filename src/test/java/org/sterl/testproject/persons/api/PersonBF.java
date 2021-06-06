package org.sterl.testproject.persons.api;

import javax.ws.rs.POST;

import org.sterl.testproject.persons.activity.PersonManager;
import org.sterl.testproject.persons.api.PersonConverter.ToPerson;
import org.sterl.testproject.persons.api.PersonConverter.ToPersonBE;
import org.sterl.testproject.persons.model.PersonBE;


public class PersonBF {
    PersonManager personManager;

    @POST
    public Person create(Person p) {
        PersonBE result = personManager.createNew(ToPersonBE.INSTANCE.convert(p));
        return ToPerson.INSTANCE.convert(result);
    }
}
