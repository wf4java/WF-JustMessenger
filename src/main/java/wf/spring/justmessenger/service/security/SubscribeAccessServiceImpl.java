package wf.spring.justmessenger.service.security;

import org.springframework.messaging.simp.user.DestinationUserNameProvider;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;

@Service
public class SubscribeAccessServiceImpl implements SubscribeAccessService {

    @Override
    public boolean subscribeToUserAccess(Map<String, String> arguments, Principal principal) {
        String id = arguments.get("id");
        if(id == null) return false;

        if(principal instanceof DestinationUserNameProvider destinationUserNameProvider)
            return destinationUserNameProvider.getDestinationUserName().equals(id);

        return principal.getName().equals(id);
    }

    @Override
    public boolean subscribeToGroupChatAccess(Map<String, String> arguments, Principal principal) {
        String id = arguments.get("id");
        if(id == null) return false;

        return true;
    }

}
