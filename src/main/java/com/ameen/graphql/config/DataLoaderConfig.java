package com.ameen.graphql.config;

import com.ameen.graphql.dataloader.BookDataLoader;
import com.ameen.graphql.model.Book;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    public DataLoaderRegistry dataLoaderRegistry(BookDataLoader bookDataLoader) {
        DataLoaderRegistry registry = new DataLoaderRegistry();
        DataLoader<Long, Book> bookLoader = DataLoader.newMappedDataLoader(
                (MappedBatchLoaderWithContext<Long, Book>) bookDataLoader::load
        );
        registry.register("bookDataLoader", bookLoader);
        return registry;
    }

}