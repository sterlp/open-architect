package org.sterl.testproject.simple;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "SIMPLE")
@Data
public class SimpleEntityBE {

    @Id
    private Long id;
    private String name;
}
