package org.example;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(originalOut);
    }
    @Test
    public void injectionForRealExample() {

        SomeBean someBean = (new Injector()).inject(new SomeBean(),"src/main/resources/example.properties");
        someBean.foo();
        assertEquals("AC",output.toString() );
    }
    @Test
    public void testDependenciesAfterInjection() {
        // Arrange
        Injector injector = new Injector();
        SomeBean someBean = new SomeBean();
        SomeBean result = injector.inject(someBean,"src/main/resources/example.properties");
        assertNotNull(result);
        assertNotNull(result.field1);
        assertNotNull(result.field2);
        assertTrue(result.field1 instanceof SomeInterface);
        assertTrue(result.field2 instanceof SomeOtherInterface);
    }
    @Test
    public void testCreateInstance_WithInvalidFile_ShouldThrowRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            SomeBean someBean = (new Injector().inject(new SomeBean(),"src/main/resources/example.properties_broken.properties"));
        });
        assertEquals("Instance can not be created", exception.getMessage());
    }
    @Test
    public void testInjectionInvalidFile() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            SomeBean someBean = (new Injector().inject(new SomeBean(),"src/main/resources/dummy.properties"));
            someBean.foo();
        });
    }

}