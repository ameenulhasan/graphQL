package com.ameen.graphql.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLUploadConfig {

    @Bean
    public RuntimeWiringConfigurer wiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(UploadScalar.Upload);
    }

    @Bean
    public RuntimeWiringConfigurer wiringConfigurers() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Url);
    }
}
