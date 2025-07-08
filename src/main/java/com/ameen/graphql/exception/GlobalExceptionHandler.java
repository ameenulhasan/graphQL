package com.ameen.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

    if (ex instanceof MethodArgumentNotValidException validationEx) {
            StringBuilder message = new StringBuilder("Validation failed: ");
            validationEx.getBindingResult().getFieldErrors().forEach(error -> {
                message.append("[")
                        .append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("] ");
            });
            return GraphqlErrorBuilder.newError(env)
                    .message(message.toString())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
        }

        if (ex instanceof CustomGraphQLException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
        }
        return GraphqlErrorBuilder.newError(env)
                .message("Internal Server Error: " + ex.getMessage())
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }
}


