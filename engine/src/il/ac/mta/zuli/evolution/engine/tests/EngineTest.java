package il.ac.mta.zuli.evolution.engine.tests;

import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class EngineTest {
    Engine engine = new TimeTableEngine();

    @BeforeEach
    void setUp() {

    }

    @Test
    void load() {
        engine = new TimeTableEngine();
        engine.loadXML("src/resources/EX1-small.xml");

        assertTrue(engine.isXMLLoaded());
    }
}