package dev.solar.demospring51;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!prod & test")
public class TestBookRepository implements BookRepository{
}
