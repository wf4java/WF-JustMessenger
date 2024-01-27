package wf.spring.justmessenger.security.subscribe;

import java.security.Principal;
import java.util.Map;

public interface SubscribeAccessCheck {

    public static final SubscribeAccessCheck ALLOW_ALL = ((m, p) -> true);

    public boolean check(Map<String, String> map, Principal principal);


}
