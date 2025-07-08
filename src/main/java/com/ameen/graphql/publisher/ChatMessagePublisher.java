package com.ameen.graphql.publisher;

import com.ameen.graphql.dto.ChatMessageDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Component
public class ChatMessagePublisher {

    private final FluxProcessor<ChatMessageDto, ChatMessageDto> processor;
    private final FluxSink<ChatMessageDto> sink;

    public ChatMessagePublisher() {
        this.processor = DirectProcessor.<ChatMessageDto>create().serialize();
        this.sink = processor.sink();
    }

    public void publish(ChatMessageDto chatMessageDto) {
        sink.next(chatMessageDto);
    }

    public Flux<ChatMessageDto> getMessages() {
        return processor;
    }

}

