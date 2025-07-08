package com.ameen.graphql.service;

import com.ameen.graphql.dto.BookDto;
import com.ameen.graphql.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {

    SuccessResponse<Object> createBook(BookDto bookDto);

    SuccessResponse<Object> uploadFileBook(BookDto bookDto, MultipartFile uploadFile);

    SuccessResponse<Object> deleteBook(Long bookId);

}
