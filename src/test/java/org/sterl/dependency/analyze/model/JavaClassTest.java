package org.sterl.dependency.analyze.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.sterl.dependency.analyze.repository.ClassRepository;
import org.sterl.testproject.persons.api.Person;
import org.sterl.testproject.persons.model.PersonBE;

import com.github.javaparser.JavaParser;

class JavaClassTest {

    @Test
    void test() throws Exception {
        final Path p = new File("./src/test/java/org/sterl/testproject/persons/api/PersonConverter.java").toPath();
        final JavaClass c = ClassRepository.parseFile(new JavaParser(), p);
        
        assertEquals(PersonBE.class.getName(), 
                c.resolveType(PersonBE.class.getSimpleName()));
        
        assertEquals(Person.class.getName(), 
                c.resolveType(Person.class.getSimpleName()));
    }

}
