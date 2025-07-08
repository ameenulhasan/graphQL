package com.ameen.graphql.config;

import graphql.schema.GraphQLScalarType;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.springframework.web.multipart.MultipartFile;

public class UploadScalar {
    public static final GraphQLScalarType Upload = GraphQLScalarType.newScalar()
            .name("Upload")
            .description("A file part in a multipart request")
            .coercing(new Coercing<MultipartFile, Void>() {
                @Override
                public Void serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    throw new CoercingSerializeException("Upload is input-only");
                }

                @Override
                public MultipartFile parseValue(Object input) throws CoercingParseValueException {
                    if (input instanceof MultipartFile) {
                        return (MultipartFile) input;
                    }
                    throw new CoercingParseValueException("Expected a file upload.");
                }

                @Override
                public MultipartFile parseLiteral(Object input) {
                    throw new CoercingParseValueException("Must use variable to upload file");
                }
            }).build();
}
