package wf.spring.justmessenger.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;
import wf.spring.justmessenger.security.auth.JwtAuthenticationManager;
import wf.spring.justmessenger.security.subscribe.SubscribeAccessCheck;
import wf.spring.justmessenger.service.security.SubscribeAccessService;
import wf.spring.justmessenger.security.auth.WebSocketBearerAuthenticationInterceptor;
import wf.spring.justmessenger.security.subscribe.WebSocketSubscribeInterceptor;




@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final SubscribeAccessService subscribeAccessService;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/user");
        registry.setApplicationDestinationPrefixes("/messenger");
        registry.setUserDestinationPrefix("/user");
    }

    @Bean
    public WebSocketSubscribeInterceptor webSocketSubscribeInterceptor() {
         return WebSocketSubscribeInterceptor.builder()
                 .addAccessCheck(SubscribeAccessCheck.ALLOW_ALL, "/topic/user/{id}/update")
                 .addAccessCheck(subscribeAccessService::subscribeToUserAccess, "/user/{id}/queue")
                 .addAccessCheck(subscribeAccessService::subscribeToUserAccess, "/user/{id}/queue/reply")
                 .addAccessCheck(subscribeAccessService::subscribeToUserAccess, "/user/{id}/queue/error")
                 .addAccessCheck(subscribeAccessService::subscribeToUserAccess, "/user/{id}/queue/message")
                 .addAccessCheck(subscribeAccessService::subscribeToGroupChatAccess, "/topic/group_chat/{id}")
                 .build();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketJwtAuthInterceptor(), webSocketSubscribeInterceptor());
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024 * 1024);
    }

    @Bean
    public WebSocketBearerAuthenticationInterceptor webSocketJwtAuthInterceptor() {
        return new WebSocketBearerAuthenticationInterceptor(jwtAuthenticationManager);
    }




}
