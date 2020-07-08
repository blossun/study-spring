package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BookService {

    @Autowired
    BookRepository myBookRepository;

    @PostConstruct
    public void setUp() {
        System.out.println(myBookRepository.getClass());
    }
}
