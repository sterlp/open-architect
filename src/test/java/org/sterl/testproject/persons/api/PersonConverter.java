package org.sterl.testproject.persons.api;

import org.sterl.testproject.addresses.model.AddressBE;
import org.sterl.testproject.persons.model.PersonBE;

class PersonConverter {

    enum ToPerson {
        INSTANCE;
        
        Person convert(PersonBE v) {
            return new Person();
        }
    }
    enum ToPersonBE {
        INSTANCE;
        PersonBE convert(Person v) {
            return new PersonBE();
        }
    }
    
    enum ToAdress {
        INSTANCE;
        
        Adress convert(AddressBE v) {
            return new Adress();
        }
    }
    enum ToAdressBE {
        INSTANCE;
        AddressBE convert(Adress v) {
            return new AddressBE();
        }
    }
}
