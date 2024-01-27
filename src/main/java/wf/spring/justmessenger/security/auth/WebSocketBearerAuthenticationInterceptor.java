package wf.spring.justmessenger.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.DestinationPatternsMessageCondition;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessageTypeMessageCondition;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class WebSocketBearerAuthenticationInterceptor implements ChannelInterceptor {

    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", Pattern.CASE_INSENSITIVE);
    private static final String HEADER = "Authorization";

    private final AuthenticationManager authenticationManager;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Assert.notNull(accessor, "Accessor is null!");

        if(accessor.getCommand() != StompCommand.CONNECT) return message;

        String authorization = accessor.getFirstNativeHeader(HEADER);
        if(authorization == null) return null;
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) return null;
        Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorization);
        if (!matcher.matches()) return null;

        authorization = matcher.group("token");

        try {
            Authentication authentication = authenticationManager.authenticate(new BearerTokenAuthenticationToken(authorization));
            accessor.setUser((Principal) authentication.getPrincipal());
        }
        catch (AuthenticationException e) { return null; }

        return message;
    }




}
