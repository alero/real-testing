package org.hrodberaht.inject.extensions.junit.demo.service;

import javax.ejb.Local;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:08
 * @created 1.0
 * @since 1.0
 */
@Local
public interface AccountingService {

    void addMoney(double money, long customerAccountId);

}
