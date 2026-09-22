package com.bookstore.bookstore_backend.service;

import com.bookstore.bookstore_backend.entity.Book;
import com.bookstore.bookstore_backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setImage(updatedBook.getImage());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setRating(updatedBook.getRating());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setStockQuantity(updatedBook.getStockQuantity());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {

        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }

        bookRepository.deleteById(id);
    }
}