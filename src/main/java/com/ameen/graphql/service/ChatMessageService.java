package com.ameen.graphql.service;

import com.ameen.graphql.dto.ChatMessageDto;
import com.ameen.graphql.dto.EditMessageDto;
import com.ameen.graphql.response.SuccessResponse;

import java.util.List;

public interface ChatMessageService {

    ChatMessageDto saveMessage(ChatMessageDto chatMessageDto);

    SuccessResponse<List<ChatMessageDto>> getChatMessages(Long sender, Long receiver);

    SuccessResponse<Object> editMessages(EditMessageDto editMessageDto);

}
