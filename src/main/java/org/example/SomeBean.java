package org.example;

public class SomeBean {
    @AutoInjectable
    SomeInterface field1;
    @AutoInjectable
    SomeOtherInterface field2;

    public void foo(){
        field1.doSomething();
        field2.doSomeOther();
    }
}