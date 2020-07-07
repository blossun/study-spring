package dev.solar.springapplicationcontext;

import org.springframework.beans.factory.annotation.Autowired;

public class BookService {

    @Autowired
    BookRepository bookRepository;

}
