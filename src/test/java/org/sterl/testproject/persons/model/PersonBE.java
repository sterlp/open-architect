package org.sterl.testproject.persons.model;

import org.sterl.testproject.addresses.model.AddressBE;

import lombok.Data;

@Data
public class PersonBE {
    private Long id;
    private AddressBE adress;
}
