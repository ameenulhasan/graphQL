package com.ameen.graphql.dataloader;

import com.ameen.graphql.model.User;
import com.ameen.graphql.repository.UserRepository;
import org.dataloader.BatchLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserDataLoader implements BatchLoader<Long, User> {

    private final UserRepository userRepository;

    public UserDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletionStage<List<User>> load(List<Long> userIds) {
        return CompletableFuture.supplyAsync(() ->
                userRepository.findAllById(userIds)
        );
    }
}
