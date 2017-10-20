package org.hrodberaht.injection.extensions.junit.ejb.internal;

import org.mockito.Mockito;

import javax.ejb.SessionContext;
import java.security.Principal;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-04-04 23:15
 * @created 1.0
 * @since 1.0
 */
public class SessionContextCreator {
    public static SessionContext create() {
        SessionContext sessionContext = Mockito.mock(SessionContext.class);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("JUnitUser");
        Mockito.when(sessionContext.getCallerPrincipal()).thenReturn(principal);
        return sessionContext;
    }
}
