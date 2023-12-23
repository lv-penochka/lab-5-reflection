import java.util.Arrays;
import java.util.Properties;
import java.io.*;
public class Injector {
    public Object inject (Object object){
        Class<?> objectClass = object.getClass();
        Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AutoInjectable.class))
                .forEach(field -> {
            field.setAccessible(true);
            String objectFieldName = field.getType().getName();
            Properties property = getProperties();


        });
    }

    private static Properties getProperties() {
        Properties property= new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);

            String host = property.getProperty("db.host");
            String login = property.getProperty("db.login");
            String password = property.getProperty("db.password");

            System.out.println("HOST: " + host
                    + ", LOGIN: " + login
                    + ", PASSWORD: " + password);

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

        property.load();
        return property;
    }
}
