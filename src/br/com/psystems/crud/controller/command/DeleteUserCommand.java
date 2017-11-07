/**
 * 
 */
package br.com.psystems.crud.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.psystems.crud.exception.DAOException;
import br.com.psystems.crud.exception.SystemException;
import br.com.psystems.crud.model.dao.VendorDAO;

/**
 * @author rafael.saldanha
 *
 */
public class DeleteUserCommand extends AbstractCommand {

	public DeleteUserCommand(VendorDAO dao) {
		this.dao = dao;
	}

	private static final long serialVersionUID = -813686630847825683L;
	private static Logger logger = Logger.getLogger(DeleteUserCommand.class);

	private String pagina = PAGE_FORNECEDOR_LISTA;
	private VendorDAO dao;

	@Override
	public String execute(HttpServletRequest request) {
		try {
			dao.delete(getID(request));
			addSuccessMessage(request, DELETE_SUCCESS_MESSAGE);
			
		} catch (DAOException | ServletException | SystemException e) {
			logger.error(e.getMessage());
			setException(request, e);
		}
		return pagina;
	}

}
