package dev.solar.demospring51;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository @Primary
public class SolarBookRepository implements BookRepository{
}
