package wf.spring.justmessenger.service.security;

import java.security.Principal;
import java.util.Map;

public interface SubscribeAccessService {
    boolean subscribeToUserAccess(Map<String, String> arguments, Principal principal);

    boolean subscribeToGroupChatAccess(Map<String, String> arguments, Principal principal);
}
