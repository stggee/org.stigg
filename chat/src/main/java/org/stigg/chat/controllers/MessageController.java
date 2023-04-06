package org.stigg.chat.controllers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.stigg.chat.dao.ChatRepository;
import org.stigg.chat.dao.UserRepository;
import org.stigg.chat.pojo.Chat;
import org.stigg.chat.pojo.Message;
import org.stigg.chat.pojo.User;


@Controller
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    public MessageController(SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, ChatRepository chatRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    @MessageMapping("/send_to")
    public void sendTo(@Payload Message message){
        if (message.getTo().equals("all")){
            simpMessagingTemplate.convertAndSend("/topic/messages", message);
            return;
        }

        User from = userRepository.findUserByUsername(message.getFrom());
        User to = userRepository.findUserByUsername(message.getTo());

        //get or init chat and renew users chats
        Chat from_to_chat = chatRepository.findChatWithParticipantsOrInit(from,to);
        from_to_chat.addMessage(message);

        System.out.println(message.getId());
        simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/messages", message);

    }

    @MessageMapping("/get_chat")
    @SendToUser("/queue/chats")
    public Chat getChat(@Payload Message message){

        if (message.getTo().equals("all")){
            simpMessagingTemplate.convertAndSend("/topic/messages", message);
            return null;
        }

        User from = userRepository.findUserByUsername(message.getFrom());
        User to = userRepository.findUserByUsername(message.getTo());

        //get or init chat and renew users chats

        return chatRepository.findChatWithParticipantsOrInit(from,to);
    }
}
