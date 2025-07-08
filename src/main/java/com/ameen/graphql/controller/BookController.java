package com.ameen.graphql.controller;

import com.ameen.graphql.dto.BookDto;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.BookService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @MutationMapping
    public SuccessResponse<Object> createBook(@Valid @Argument BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @MutationMapping
    public SuccessResponse<Object> uploadFileBook(@Valid @Argument BookDto bookDto,
                                                  @Argument("uploadFile") MultipartFile uploadFile) {
        return bookService.uploadFileBook(bookDto, uploadFile);
    }

    @MutationMapping
    public SuccessResponse<Object> deleteBook(@Argument Long bookId) {
        return bookService.deleteBook(bookId);
    }

}
