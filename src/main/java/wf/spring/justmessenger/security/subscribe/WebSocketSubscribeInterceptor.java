package wf.spring.justmessenger.security.subscribe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.DestinationPatternsMessageCondition;
import org.springframework.messaging.handler.annotation.support.DestinationVariableMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessageTypeMessageCondition;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.*;

import java.security.Principal;
import java.util.*;

@Getter
@NoArgsConstructor
public class WebSocketSubscribeInterceptor implements ChannelInterceptor {


    private final PathMatcher pathMatcher = new AntPathMatcher();
    private Map<SimpMessageMappingInfo, SubscribeAccessCheck> subscribeAccessCheckMap = new LinkedHashMap<>(32);


    public WebSocketSubscribeInterceptor(Map<SimpMessageMappingInfo, SubscribeAccessCheck> allowedCheckMap) {
        this.subscribeAccessCheckMap = allowedCheckMap;
    }


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Assert.notNull(accessor, "Accessor is null!");

        if(accessor.getCommand() != StompCommand.SUBSCRIBE) return message;
        if(accessor.getUser() == null) return null;

        if(check(message, accessor.getDestination(), accessor.getUser())) return message;
        else return null;
    }



    public boolean check(Message<?> message, String destination, Principal principal) {
        MessageHeaderAccessor headerAccessor = MessageHeaderAccessor.getMutableAccessor(message);
        headerAccessor.setHeader("lookupDestination", destination);

        Map<SimpMessageMappingInfo, SubscribeAccessCheck> matched = addMatchesToCollection(subscribeAccessCheckMap, message);

        if (matched.isEmpty()) return false;
        for(Map.Entry<SimpMessageMappingInfo, SubscribeAccessCheck> entry : matched.entrySet()) {
            if(!mappingAndCheck(entry.getKey(), entry.getValue(), destination, message, principal)) return false;
        }
        return true;
    }



    private boolean mappingAndCheck(SimpMessageMappingInfo mapping, SubscribeAccessCheck subscribeAccessCheck, String destination, Message<?> message, Principal principal) {
        Set<String> patterns = mapping.getDestinationConditions().getPatterns();

        if (CollectionUtils.isEmpty(patterns)) return subscribeAccessCheck.check(Collections.emptyMap(), principal);

        String pattern = patterns.iterator().next();
        Map<String, String> arguments = this.pathMatcher.extractUriTemplateVariables(pattern, destination);
        if (!CollectionUtils.isEmpty(arguments)) {
            MessageHeaderAccessor mha = MessageHeaderAccessor.getAccessor(message, MessageHeaderAccessor.class);
            Assert.state(mha != null && mha.isMutable(), "Mutable MessageHeaderAccessor required");
            mha.setHeader(DestinationVariableMethodArgumentResolver.DESTINATION_TEMPLATE_VARIABLES_HEADER, arguments);
        }
        return subscribeAccessCheck.check(arguments, principal);
    }


    private Map<SimpMessageMappingInfo, SubscribeAccessCheck> addMatchesToCollection(Map<SimpMessageMappingInfo, SubscribeAccessCheck> mappingsToCheck, Message<?> message) {
        Map<SimpMessageMappingInfo, SubscribeAccessCheck> checkMap = new HashMap<>();

        for(Map.Entry<SimpMessageMappingInfo, SubscribeAccessCheck> entry : mappingsToCheck.entrySet()) {
            SimpMessageMappingInfo match = entry.getKey().getMatchingCondition(message);
            if (match != null) {
                checkMap.put(entry.getKey(), entry.getValue());
            }
        }

        return checkMap;
    }




    public static WebSocketSubscribeInterceptorBuilder builder() {
        return new WebSocketSubscribeInterceptorBuilder();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class WebSocketSubscribeInterceptorBuilder {

        private final Map<SimpMessageMappingInfo, SubscribeAccessCheck> subscribeAccessCheckMap = new LinkedHashMap<>(32);

        public WebSocketSubscribeInterceptorBuilder addAccessCheck(SubscribeAccessCheck subscribeAllowedCheck, String... pattern) {
            subscribeAccessCheckMap.put(new SimpMessageMappingInfo(new SimpMessageTypeMessageCondition(SimpMessageType.SUBSCRIBE),
                    new DestinationPatternsMessageCondition(pattern)), subscribeAllowedCheck);

            return this;
        }

        public WebSocketSubscribeInterceptor build() {
            return new WebSocketSubscribeInterceptor(subscribeAccessCheckMap);
        }
    }




}
