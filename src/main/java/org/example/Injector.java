package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/** The Injector class is responsible for injecting dependencies into an
 *  object based on the AutoInjectable annotation present on its fields.**/
public class Injector {
    /**
    Injects dependencies into the provided object based on the AutoInjectable annotation present on its fields.

    @param object   the object into which the dependencies will be injected
    @param filePath the file path of the properties file containing the mapping of dependency names to their implementations
    @param <T>   generic type of the object
    @return the object with injected dependencies
    @throws RuntimeException If the field values can`t be set
     */
    public <T> T inject(T object,String filePath) {
        Class<?> objectClass = object.getClass();
        Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AutoInjectable.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    String objFieldNameAsProperty = field.getType().getName();
                    Object objFieldWithValue= createInstance(objFieldNameAsProperty,filePath);
                    try {
                        field.set(object, objFieldWithValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to set field value",e);
                    }
                });
        return object;
    }
/**
    Creates an instance of a class based on the provided class name from the properties file.

    @param objFieldNameAsProperty the name of the class as specified in the properties file
    @param filePath the file path of the properties file containing the mapping of class names to their implementations
    @return an instance of the specified class
    @throws  RuntimeException If an instance cannot be created
     **/
    private Object createInstance ( String objFieldNameAsProperty,String filePath) {
        Properties property = inputProperties(filePath);
        String implementationName = property.getProperty(objFieldNameAsProperty);
        Object implemented;
        try {
            implemented = Class.forName(implementationName).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Instance can not be created",e);
        }
        return implemented;
    }

 /**
    Reads the properties from the specified properties file.

    @param filePath the file path of the properties file
    @return Properties for object that were written in file
    @throws RuntimeException If the properties file cannot be loaded
     **/
    private static Properties inputProperties(String filePath) {
        Properties property = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            property.load(fis);
        } catch (IOException e) {
            System.err.println("File with properties doesn`t exist");
        }
        return property;
    }

}
