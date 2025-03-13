package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.model.Book;
import es.readtoowell.api_biblioteca.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Page<Book> getAllBooks(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "añoPublicacion")));
    }

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book deleteBook(Book book) {
        return bookRepository.save(book);
    }
}
