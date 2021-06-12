package org.sterl.dependency.component.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ComponentManagerUtilTest {

    @ParameterizedTest
    @CsvSource({ 
        "org.sterl, org.sterl.foo.bar, foo", 
        "org.sterl, org.sterl.foo, foo",
        "org.sterl.foo, org.sterl.foo, foo",
    })
    public void test(String base, String packageName, String expected) {
        assertEquals(expected, ComponentManagerUtil.getComponentName(base, packageName));
    }
    
    @Test
    public void testDetermineBestQualified() {
        assertNull(ComponentManagerUtil.determineBestQualified(new ArrayList<>()));
        assertEquals("org.sterl.foo", ComponentManagerUtil
                .determineBestQualified(Arrays.asList("org.sterl", "org.sterl.foo")));
        
        assertEquals("org.sterl.bar", ComponentManagerUtil
                .determineBestQualified(Arrays.asList("org.sterl.bar", "org.sterl.foo")));
        assertEquals("org.sterl.bar.car", ComponentManagerUtil
                .determineBestQualified(Arrays.asList("org.sterl.bar.car", "org.sterl.foo")));
    }
}
