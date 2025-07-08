package com.ameen.graphql.serviceImpl;

import com.ameen.graphql.constant.Constant;
import com.ameen.graphql.dto.ChatMessageDto;
import com.ameen.graphql.dto.EditMessageDto;
import com.ameen.graphql.exception.CustomGraphQLException;
import com.ameen.graphql.model.ChatMessage;
import com.ameen.graphql.model.User;
import com.ameen.graphql.publisher.ChatMessagePublisher;
import com.ameen.graphql.repository.ChatMessageRepository;
import com.ameen.graphql.repository.UserRepository;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.response.UserContextHolder;
import com.ameen.graphql.service.ChatMessageService;
import com.ameen.graphql.util.CommonUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    public static final String RECEIVER_NOT_FOUND = "Receiver not found";
    public static final String SENDER_NOT_FOUND = "Sender not found";
    private final UserRepository userRepository;
    private final ChatMessageRepository  chatMessageRepository;
    private final CommonUtil commonUtil;
    private final ChatMessagePublisher publisher;

    public ChatMessageServiceImpl(UserRepository userRepository, ChatMessageRepository chatMessageRepository,
                                  CommonUtil commonUtil, ChatMessagePublisher publisher) {
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.commonUtil = commonUtil;
        this.publisher = publisher;
    }

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        User user = userRepository.findByIdIsActive(chatMessageDto.getReceiver())
                .orElseThrow(() -> new CustomGraphQLException(RECEIVER_NOT_FOUND));
        User user1 = userRepository.findByIdIsActive(chatMessageDto.getSender())
                .orElseThrow(() -> new CustomGraphQLException(SENDER_NOT_FOUND));
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setReceiver(user);
        chatMessage.setSender(user1);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setContent(commonUtil.encryptMessage(chatMessageDto.getContent()));
        chatMessageRepository.save(chatMessage);
        chatMessageDto.setId(chatMessage.getId());
        chatMessageDto.setTimestamp(chatMessage.getTimestamp());
        publisher.publish(chatMessageDto); // Real-time push
        return chatMessageDto;
    }

    @Override
    public SuccessResponse<List<ChatMessageDto>> getChatMessages(Long sender, Long receiver) {
        SuccessResponse<List<ChatMessageDto>> successResponse = new SuccessResponse<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderReceiver(sender, receiver);
        List<ChatMessageDto> chatMessageDto = new ArrayList<>();
        for (ChatMessage chat : chatMessages){
            ChatMessageDto messageDto = new ChatMessageDto();
            messageDto.setId(chat.getId());
            messageDto.setSender(chat.getSender().getId());
            messageDto.setReceiver(chat.getReceiver().getId());
            messageDto.setTimestamp(chat.getTimestamp());
            messageDto.setContent(commonUtil.decryptMessage(chat.getContent()));
            chatMessageDto.add(messageDto);
        }
        successResponse.setData(chatMessageDto);
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> editMessages(EditMessageDto editMessageDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Optional<ChatMessage> message = chatMessageRepository.findById(editMessageDto.getMessageId());
        if(message.isPresent() && Objects.equals(message.get().getSender().getId(),userId)){
            ChatMessage chatMessage = message.get();
            LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
            if (chatMessage.getTimestamp().isBefore(tenMinutesAgo)) {
                throw new CustomGraphQLException(Constant.EDITED_TIME_EXCEEDED);
            }
            chatMessage.setContent(commonUtil.encryptMessage(editMessageDto.getContent()));
            chatMessageRepository.save(chatMessage);
        } else {
            throw new CustomGraphQLException(Constant.MESSAGE_NOT_FOUND);
        }
        successResponse.setStatusMessage(Constant.MESSAGE_UPDATED);
        return successResponse;
    }

}
