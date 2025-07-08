package com.ameen.graphql.dataloader;

import com.ameen.graphql.model.Book;
import com.ameen.graphql.repository.BookRepository;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BookDataLoader implements MappedBatchLoaderWithContext<Long, Book> {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public CompletionStage<Map<Long, Book>> load(Set<Long> bookIds, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() ->
                bookRepository.findAllById(bookIds)
                        .stream()
                        .collect(Collectors.toMap(Book::getId, Function.identity()))
        );
    }
}