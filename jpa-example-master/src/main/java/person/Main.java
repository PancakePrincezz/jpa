package person;

import com.github.javafaker.Faker;
import person.model.Address;
import person.model.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.Locale;


public class Main {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("person-db");
    private static EntityManager em = emf.createEntityManager();

    private static Faker faker = new Faker(new Locale("en"));

    public static Address randomAddress(){
        var a = new Address();

        a.setCity(faker.address().city());
        a.setCountry(faker.address().country());
        a.setState(faker.address().state());
        a.setStreetAddress(faker.address().streetAddress());
        a.setZip(faker.address().zipCode());

        return a;
    }

    public static Person randomPerson(){
            var p = new Person();

            p.setName(faker.name().name());
            p.setAddress(randomAddress());
            p.setDob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            p.setEmail(faker.internet().emailAddress());
            p.setGender(faker.options().option(Person.Gender.values()));
            p.setProfession(faker.company().profession());

            return p;
    }

    public static void main(String[] args) {

        em.getTransaction().begin();

        for(int i = 0; i < 1000; i++){
            em.persist(randomPerson());
        }

        em.getTransaction().commit();
    }
}
