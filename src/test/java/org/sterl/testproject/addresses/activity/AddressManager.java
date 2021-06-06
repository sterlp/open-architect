package org.sterl.testproject.addresses.activity;

import org.sterl.testproject.addresses.dao.AddressDao;
import org.sterl.testproject.addresses.model.AddressBE;

public class AddressManager {

    AddressDao adressDao;
    
    public AddressBE addOrUpdate(AddressBE adress) {
        return adressDao.save(adress);
    }
}
