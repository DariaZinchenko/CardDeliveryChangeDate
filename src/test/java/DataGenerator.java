import java.util.Locale;

import com.github.javafaker.Faker;

public class DataGenerator {

    private DataGenerator() {}

    public static class Generator {

        private Generator() {}

        public static Person generatePerson(String locale) {
            Faker faker = new Faker(new Locale("ru"));

            return new Person(faker.name().firstName() + " " + faker.name().lastName(), faker.numerify("+79#########"), faker.address().cityName());
        }
    }
}