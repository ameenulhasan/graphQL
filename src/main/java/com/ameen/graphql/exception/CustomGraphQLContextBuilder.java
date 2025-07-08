package com.ameen.graphql.exception;

import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.Optional;

@Component
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

    private final DataLoaderRegistry dataLoaderRegistry;

    public CustomGraphQLContextBuilder(DataLoaderRegistry dataLoaderRegistry) {
        this.dataLoaderRegistry = dataLoaderRegistry;
    }

    @Override
    public GraphQLContext build(HttpServletRequest request, HttpServletResponse response) {
        return new GraphQLContext() {
            @Override
            public Optional<Subject> getSubject() {
                return Optional.empty();
            }

            @Override
            public DataLoaderRegistry getDataLoaderRegistry() {
                return dataLoaderRegistry;
            }
        };
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
        return new GraphQLContext() {
            @Override
            public Optional<Subject> getSubject() {
                return Optional.empty();
            }

            @Override
            public DataLoaderRegistry getDataLoaderRegistry() {
                return dataLoaderRegistry;
            }
        };
    }

    @Override
    public GraphQLContext build() {
        return new GraphQLContext() {
            @Override
            public Optional<Subject> getSubject() {
                return Optional.empty();
            }

            @Override
            public DataLoaderRegistry getDataLoaderRegistry() {
                return dataLoaderRegistry;
            }
        };
    }
}
