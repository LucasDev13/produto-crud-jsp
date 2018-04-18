/**
 * 
 */
package br.com.psystems.crud.web.controller.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.psystems.crud.exception.DAOException;
import br.com.psystems.crud.exception.SystemException;
import br.com.psystems.crud.infra.util.Constants;
import br.com.psystems.crud.model.UnitMeasurement;
import br.com.psystems.crud.service.CrudService;
import br.com.psystems.crud.service.UnitMeasurementService;
import br.com.psystems.crud.web.controller.Controllable;

/**
 * @author Rafael.Saldanha
 *
 */
public class ListUnitMeasurementAction extends AbstractCrudAction<UnitMeasurement> implements Controllable {

	public ListUnitMeasurementAction(UnitMeasurementService service) {
		super(service);
	}

	private static final long serialVersionUID = 7904027167987556427L;
	private static Logger logger = Logger.getLogger(ListUnitMeasurementAction.class);

	private UnitMeasurementService service;


	/* (non-Javadoc)
	 * @see br.com.psystems.crud.command.Command#executar(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String execute(HttpServletRequest request) {
		try {
			List<UnitMeasurement> units = service.getAll();
			setEntityList(request, units);
			
		} catch (DAOException | SystemException e) {
			logger.error(e.getMessage());
			setException(request, e);
		}
		return Constants.PAGE_UNIT_LIST;
	}


	@Override
	protected void setService(CrudService<UnitMeasurement> service) {
		this.service = (UnitMeasurementService) service;
	}

}
