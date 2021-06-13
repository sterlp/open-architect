package org.sterl.dependency.graph.facade;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@EnableWeld
class GraphDotPrinterFacadeTest {
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.performDefaultDiscovery();
    
    @Inject GraphDotPrinterFacade subject;
    JsonWriterFactory writerFactory;
    
    @BeforeEach
    void before() {
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        writerFactory = Json.createWriterFactory(config);
    }

    @Test
    void test() throws Exception {
        final JsonObject result = subject.print("./src/test/java/org/sterl",
                "org.sterl.dependecy", "org.sterl.testproject");
        
        writerFactory.createWriter(System.out).write(result);
        System.out.println();
    }

}
