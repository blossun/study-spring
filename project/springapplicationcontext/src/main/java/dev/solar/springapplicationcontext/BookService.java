package dev.solar.springapplicationcontext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    public BookRepository bookRepository;

}
