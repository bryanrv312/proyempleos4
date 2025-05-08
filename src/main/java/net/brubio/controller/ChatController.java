package net.brubio.controller;

import net.brubio.service.db.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping("/chat")
    public String handleChatMessage(@RequestParam("message") String userMessage, Model model) {
        String botResponse = chatGPTService.getChatGPTResponse(userMessage);
        model.addAttribute("userMessage", userMessage);
        model.addAttribute("botResponse", botResponse);
        return "chat";
    }
}

