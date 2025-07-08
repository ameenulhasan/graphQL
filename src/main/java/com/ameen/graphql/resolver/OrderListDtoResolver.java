package com.ameen.graphql.resolver;

import com.ameen.graphql.dto.OrderListDto;
import com.ameen.graphql.model.Book;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderListDtoResolver {

    @SchemaMapping(typeName = "OrderListDto", field = "book")
    public CompletableFuture<Book> getBook(OrderListDto order, DataFetchingEnvironment environment) {
        DataLoader<Long, Book> bookLoader = environment.getDataLoader("bookDataLoader");
        return bookLoader.load(order.getBook().getId());
    }

}

