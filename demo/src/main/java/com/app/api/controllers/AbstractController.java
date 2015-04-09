/** AbstractController.java
 **/

/**
 * package
 */
package com.app.api.controllers;

/**
 * imports
 */
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@Controller
/**
 * Abstract!
 * @author David Romero Alcaide
 *
 */
public abstract class AbstractController {

	/**
	 * Log
	 */
	private static final Logger LOGGER = Logger
			.getLogger(AbstractController.class);

	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	/**
	 * panic
	 * @author David Romero Alcaide
	 * @param oops
	 * @return
	 */
	public ModelAndView panic(Throwable oops) {
		LOGGER.error(oops);
		ModelAndView result;

		result = new ModelAndView();
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exceptionMessage", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

	// Transaction management -------------------------------------------------

	@Autowired
	/**
	 * Manager
	 */
	private PlatformTransactionManager transactionManager;
	/**
	 * State of transaction
	 */
	protected TransactionStatus txStatus;

	/**
	 * Begin a transaction with the database
	 * 
	 * @author David Romero Alcaide
	 */
	protected void beginTransaction() {
		assert txStatus == null;
		beginTransaction(false);
	}

	/**
	 * Begin a transaction with the database
	 * 
	 * @author David Romero Alcaide
	 * @param readOnly
	 */
	protected void beginTransaction(boolean readOnly) {
		assert txStatus == null;

		DefaultTransactionDefinition definition;

		definition = new DefaultTransactionDefinition();
		definition
				.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
		definition
				.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		definition.setReadOnly(readOnly);
		txStatus = transactionManager.getTransaction(definition);
	}

	/**
	 * Commit a transaction
	 * 
	 * @author David Romero Alcaide
	 * @throws Throwable
	 */
	protected void commitTransaction() throws TransactionException {
		assert txStatus != null;

		try {
			transactionManager.commit(txStatus);
			txStatus = null;
		} catch (TransactionException oops) {
			throw oops;
		}
	}

	/**
	 * Rollback a transaction
	 * 
	 * @author David Romero Alcaide
	 * @throws Throwable
	 */
	protected void rollbackTransaction() throws TransactionException {
		assert txStatus != null;
		try {
			if (!txStatus.isCompleted()) {
				transactionManager.rollback(txStatus);
			}
			txStatus = null;
		} catch (TransactionException oops) {
			throw oops;
		}
	}

}
