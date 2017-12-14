/**
 * 
 */
package br.com.psystems.crud.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.psystems.crud.infra.ConnectionManager;
import br.com.psystems.crud.infra.TransactionCallback;
import br.com.psystems.crud.infra.exception.DAOException;
import br.com.psystems.crud.infra.exception.SystemException;
import br.com.psystems.crud.model.User;
import br.com.psystems.crud.model.dao.UserDAO;
import br.com.psystems.crud.model.enums.RoleEnum;

/**
 * @author rafael.saldanha
 *
 */
public class UserDAOImpl extends AbstractDAO implements UserDAO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2437227275966264997L;

	public UserDAOImpl() throws DAOException {
		super();
	}
	
	public UserDAOImpl(ConnectionManager connectionManager) throws DAOException {
		super(connectionManager);
	}

	private static Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	public static final String TABLE_NAME = "tb_user";
	protected static final String SQL_FIND_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (name, email, password, role) VALUES (?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
	private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
	private static final String SQL_FIND_ALL = "SELECT * FROM " + TABLE_NAME + "";
	private static final String SQL_FIND_BY_NOME = "SELECT * FROM " + TABLE_NAME + " WHERE UPPER(name) like UPPER(?)";
	
	
	@Override
	public void save(User entity) throws DAOException, SystemException {

		connectionManager.doInTransaction(new TransactionCallback() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement ps = getPreparedStatement(connection, SQL_INSERT);
				ps.setString(1, entity.getName());
				ps.setString(2, entity.getEmail());
				ps.setString(3, entity.getPassword());
				ps.setString(4, entity.getRole().name());
				ps.execute();
				
				logger.info("Salvo com sucesso!");
			}
		});
	}

	@Override
	public User update(User entity) throws DAOException, SystemException {

		connectionManager.doInTransaction(new TransactionCallback() {
			
			@Override
			public void execute(Connection connection) throws SQLException {

				PreparedStatement ps = getPreparedStatement(connection, SQL_UPDATE);
				ps.setString(1, entity.getName());
				ps.setString(2, entity.getEmail());
				ps.setString(3, entity.getPassword());
				ps.setString(4, entity.getRole().name());
				ps.setLong(5, entity.getId());

				int qtdLinhas = ps.executeUpdate();

				if (0 >= qtdLinhas) {
					logger.info("Nenhum registro alterado.");
				} else {
					logger.info("Atualizado com sucesso!\n ".concat(entity.toString()));
				}
			}
		});

		return findById(entity.getId());
	}

	@Override
	public void delete(Long id) throws DAOException, SystemException {

		connectionManager.doInTransaction(new TransactionCallback() {
			
			@Override
			public void execute(Connection connection) throws SQLException, DAOException, SystemException {

				PreparedStatement ps = getPreparedStatement(connection, SQL_DELETE);
				ps.setLong(1, id);

				int qtdLinhas = ps.executeUpdate();

				if (0 >= qtdLinhas) {
					logger.info("Nenhum registro apagado.");
				} else {
					logger.info("Usuário apagado com sucesso!");
				}
			}
		});
	}

	@Override
	public User findById(Long id) throws DAOException, SystemException {

		Connection con = null;
		
		try {
			con = connectionManager.getConnection();
			
			PreparedStatement ps = getPreparedStatement(con, SQL_FIND_BY_ID);
			ps.setLong(1, id);
			
			return (User) getEntity(ps.executeQuery());
			
		} catch (Exception e) {
			set(e);
			return null;
		} finally {
			connectionManager.close(con);
		}
	}

	@Override
	public List<User> findByName(String name) throws DAOException, SystemException {

		Connection con = null;
		
		try {
			con = connectionManager.getConnection();
			
			PreparedStatement ps = getPreparedStatement(con, SQL_FIND_BY_NOME);
			ps.setString(1, "%"+name+"%");
			
			return getEntityList(ps.executeQuery());
			
		} catch (Exception e) {
			set(e);
			return null;
		} finally {
			connectionManager.close(con);
		}
	}

	@Override
	public List<User> getAll() throws DAOException, SystemException {
		
		Connection con = null;
		
		try {
			con = connectionManager.getConnection();
			
			PreparedStatement ps = getPreparedStatement(con, SQL_FIND_ALL);
		
			return getEntityList(ps.executeQuery());
			
		} catch (Exception e) {
			set(e);
			return null;
		} finally {
			connectionManager.close(con);
		}
	}
	
	@Override
	protected List<User> getEntityList(final ResultSet rs) throws SQLException {

		List<User> users = new ArrayList<>();
		while (rs.next())
			users.add(createEntity(rs));

		return users;
	}
	
	@Override
	protected User getEntity(final ResultSet rs) throws SQLException {
		User user = null;
		while (rs.next())
			user = createEntity(rs);
		
		return user;
	}
	
	@Override
	protected User createEntity(final ResultSet rs) throws SQLException {

		User user = new User();
		user.setId(rs.getLong("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setRole(RoleEnum.valueOf(rs.getString("role")));
	
		return user;
	}

}