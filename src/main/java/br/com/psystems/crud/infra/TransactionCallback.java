/**
 * 
 */
package br.com.psystems.crud.infra;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.psystems.crud.exception.DAOException;
import br.com.psystems.crud.exception.SystemException;

/**
 * @author developer
 *
 */
public interface TransactionCallback {

	public void execute(Connection connection) throws SQLException, DAOException, SystemException;
}
