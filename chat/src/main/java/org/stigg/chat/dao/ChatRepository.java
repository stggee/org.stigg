package org.stigg.chat.dao;

import org.springframework.stereotype.Repository;
import org.stigg.chat.pojo.Chat;
import org.stigg.chat.pojo.User;

import java.util.*;

@Repository
public class ChatRepository {
    Set<Chat> chats = new HashSet<>();

    public void addChat(Chat chat){
        this.chats.add(chat);
    }

    public Chat findChatWithParticipantsOrInit(User user1, User user2) {
        for (Chat chat : chats) {
            if (chat.getParticipants().contains(user1) && chat.getParticipants().contains(user2)) return  chat;
        }

        Chat chat = new Chat(new Random().nextInt(), new ArrayList<>(), new HashSet<>(Set.of(user1,user2)));
        chats.add(chat);
        user1.getChats().add(chat);
        user2.getChats().add(chat);
        return chat;
    }
}
