package com.hrodberaht.inject.extensions.transaction.manager.util;

import com.hrodberaht.inject.extensions.transaction.TransactionManager;
import com.hrodberaht.inject.extensions.transaction.manager.internal.AspectJTransactionHandler;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import org.hrodberaht.injection.core.InjectContainer;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-27 02:12:40
 * @version 1.0
 * @since 1.0
 */
public class TransactionManagerUtil {

    public static void registerTransactionManager(InjectContainer theContainer) {
        if (System.getProperty("transaction.aspectj.disable") == null) {
            AspectJTransactionHandler aspectJTransactionHandler =
                    org.aspectj.lang.Aspects.aspectOf(AspectJTransactionHandler.class);
            TransactionManager transactionManager = theContainer.get(TransactionManager.class);
            TransactionLogging.log("Connecting the aspect {0} "
                    + " to transaction manager {1}", aspectJTransactionHandler.toString(), transactionManager);
            theContainer.injectDependencies(aspectJTransactionHandler);
        }
    }


}
