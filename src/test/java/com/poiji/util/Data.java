package com.poiji.util;

import com.poiji.deserialize.model.byid.Employee;
import com.poiji.deserialize.model.byid.Person;
import com.poiji.deserialize.model.byid.Sample;
import com.poiji.deserialize.model.byname.ListHolderByName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hakan on 18/03/2018
 */
public final class Data {

    public static List<Employee> unmarshallingDeserialize() {
        List<Employee> employees = new ArrayList<>(3);

        Employee employee1 = new Employee();
        employee1.setEmployeeId(123923L);
        employee1.setName("Joe");
        employee1.setSurname("Doe");
        employee1.setSingle(true);
        employee1.setAge(30);
        employee1.setBirthday("4/9/1987");
        employees.add(employee1);

        Employee employee2 = new Employee();
        employee2.setEmployeeId(123123L);
        employee2.setName("Sophie");
        employee2.setSurname("Derue");
        employee2.setSingle(false);
        employee2.setAge(20);
        employee2.setBirthday("5/3/1997");
        employees.add(employee2);

        Employee employee3 = new Employee();
        employee3.setEmployeeId(135923L);
        employee3.setName("Paul");
        employee3.setSurname("Raul");
        employee3.setSingle(false);
        employee3.setAge(31);
        employee3.setBirthday("4/9/1986");
        employees.add(employee3);

        return employees;
    }

    public static List<Person> unmarshallingPersons() {
        List<Person> persons = new ArrayList<>(5);
        Person person1 = new Person();
        person1.setRow(1);
        person1.setName("Rafique");
        person1.setAddress("Melbourne");
        person1.setEmail("raf@abc.com");
        person1.setMobile("91701");
        persons.add(person1);
        Person person2 = new Person();
        person2.setRow(2);
        person2.setName("Sam");
        person2.setAddress("Melbourne");
        person2.setEmail("sam@abc.com");
        person2.setMobile("617019");
        persons.add(person2);
        Person person3 = new Person();
        person3.setRow(3);
        person3.setName("Tony");
        person3.setAddress("Melbourne");
        person3.setEmail("tony@abc.com");
        person3.setMobile("617019");
        persons.add(person3);
        Person person4 = new Person();
        person4.setRow(4);
        person4.setName("Michael");
        person4.setAddress("Melbourne");
        person4.setEmail("mic@abc.com");
        person4.setMobile("617019");
        persons.add(person4);
        Person person5 = new Person();
        person5.setRow(5);
        person5.setName("Terry");
        person5.setAddress("Melbourne");
        person5.setEmail("terry@abc.com");
        person5.setMobile("617019");
        persons.add(person5);
        return persons;
    }

    public static List<Sample> unmarshallingSamples() {
        List<Sample> samples = new ArrayList<>(2);

        Sample sample1 = new Sample();
        sample1.setAmount(null);
        sample1.setMonth(null);
        sample1.setOther("foo");

        Sample sample2 = new Sample();
        sample2.setAmount(27.5);
        sample2.setMonth("APR");
        sample2.setOther("bar");


        samples.add(sample1);
        samples.add(sample2);
        return samples;
    }

    public static List<ListHolderByName> unmarshallingListHolders() {
        List<ListHolderByName> holders = new ArrayList<>();

        ListHolderByName holder = new ListHolderByName();

        List<String> items = new ArrayList<>();
        items.add("item 1");
        items.add("item 2");
        items.add("item 3");

        holder.setItems(items);
        holders.add(holder);
        return holders;
    }
}
