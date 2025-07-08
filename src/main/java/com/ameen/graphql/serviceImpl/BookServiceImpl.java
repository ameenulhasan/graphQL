package com.ameen.graphql.serviceImpl;

import com.ameen.graphql.dto.BookDto;
import com.ameen.graphql.exception.CustomGraphQLException;
import com.ameen.graphql.model.Book;
import com.ameen.graphql.repository.BookRepository;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.response.UserContextHolder;
import com.ameen.graphql.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BookServiceImpl implements BookService {

    private  final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public SuccessResponse<Object> createBook(BookDto bookDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Book book;
        boolean isUpdate = false;
        if (bookDto.getId() != null) {
            book = bookRepository.findById(bookDto.getId())
                    .orElseThrow(() -> new CustomGraphQLException("Book not found"));
            book.setIsActive(true);
            book.setDeletedFlag(false);
            isUpdate = true;
        } else {
            book = new Book();
        }
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setIsActive(true);
        book.setDeletedFlag(false);
        bookRepository.save(book);
        successResponse.setStatusMessage(isUpdate ? "Book updated successfully" : "Book created successfully");
        successResponse.setData(book);
        successResponse.setStatusCode(200);
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> uploadFileBook(BookDto bookDto, MultipartFile uploadFile) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Book book;
        if (bookDto.getId() != null) {
            book = bookRepository.findById(bookDto.getId())
                    .orElseThrow(() -> new CustomGraphQLException("Book not found"));
        } else {
            book = new Book();
        }
        if (uploadFile != null && !uploadFile.isEmpty()) {
            try {
                byte[] fileBytes = uploadFile.getBytes();
                book.setUploadFile(fileBytes);
            } catch (IOException e) {
                throw new CustomGraphQLException("Failed to read file: " + e.getMessage());
            }
        }
        bookRepository.save(book);
        successResponse.setStatusMessage("Book file uploaded successfully");
        successResponse.setData("Book ID: " + book.getId());
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> deleteBook(Long bookId) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomGraphQLException("Book not found with id: " + bookId));
        book.setIsActive(false);
        book.setDeletedFlag(true);
        bookRepository.save(book);
        successResponse.setStatusCode(200);
        successResponse.setStatusMessage("Book deleted successfully (soft delete)");
        return successResponse;
    }

}
