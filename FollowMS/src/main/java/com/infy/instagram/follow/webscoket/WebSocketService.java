package com.infy.instagram.follow.webscoket;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.infy.instagram.follow.dto.FollowerUpdateDTO;

@Service
@RequiredArgsConstructor
public class WebSocketService {

   private final SimpMessagingTemplate
           messagingTemplate;

   public void sendFollowerUpdate(
           FollowerUpdateDTO dto
   ) {

       messagingTemplate.convertAndSend(
               "/topic/followers",
               dto
       );
   }
}


