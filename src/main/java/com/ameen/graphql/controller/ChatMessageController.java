package com.ameen.graphql.controller;

import com.ameen.graphql.dto.ChatMessageDto;
import com.ameen.graphql.dto.EditMessageDto;
import com.ameen.graphql.publisher.ChatMessagePublisher;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.ChatMessageService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatMessagePublisher publisher;

    public ChatMessageController(ChatMessageService chatMessageService, ChatMessagePublisher publisher) {
        this.chatMessageService = chatMessageService;
        this.publisher = publisher;
    }

    @MutationMapping
    public ChatMessageDto sendMessage(@Valid @Argument ChatMessageDto chatMessageDto) {
        return chatMessageService.saveMessage(chatMessageDto);
    }

    @SubscriptionMapping
    public Flux<ChatMessageDto> messageReceived() {
        return publisher.getMessages();
    }

    @QueryMapping
    public SuccessResponse<List<ChatMessageDto>> getChatMessages(@Argument Long senderId,
                                                                 @Argument Long receiverId) {
        return chatMessageService.getChatMessages(senderId, receiverId);
    }

    @MutationMapping
    public SuccessResponse<Object> editMessage(@Valid @Argument EditMessageDto editMessageDto) {
        return chatMessageService.editMessages(editMessageDto);
    }

}
