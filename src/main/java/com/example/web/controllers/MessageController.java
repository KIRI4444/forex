package com.example.web.controllers;


import com.example.domain.Message.Message;
import com.example.domain.response.ApiResponse;
import com.example.service.MessageService;
import com.example.web.dto.message.MessageDTO;
import com.example.web.mappers.message.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @ResponseBody
    @GetMapping("/chat/{chatId}")
    public ApiResponse<?> getMessagesFromChat
            (@PathVariable Long chatId,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size)
    {
        return messageService.getMessagesFromChat(chatId, page, size);
    }

    @MessageMapping("/sendMessage")
    public MessageDTO sendMessageToChat(@RequestBody MessageDTO messageDTO) {
        System.out.println("Марк умочка");
        Message savedMessage = messageService.saveMessage(messageDTO);
        MessageDTO response = messageMapper.toDTO(savedMessage);
        simpMessagingTemplate.convertAndSend("/chat/" + messageDTO.getChatId(), response);
        return response;
    }
}
