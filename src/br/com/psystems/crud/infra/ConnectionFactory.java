/**
 * 
 */
package br.com.psystems.crud.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.com.psystems.crud.exception.DAOException;

/**
 * @author rafael.saldanha
 *
 */
public class ConnectionFactory {

	//Informacões para conexão com banco de dados PostgreSQL.
	private static final String STR_DRIVER = "org.postgresql.Driver";
	private static final String DATABASE = "crud_db";
	private static final String DATABASE_HML = "crud_db_test";
	private static final String STR_CON = "jdbc:postgresql://localhost:5432/" + DATABASE;
	private static final String STR_CON_HML = "jdbc:postgresql://localhost:5432/" + DATABASE_HML;
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	private static Logger logger = Logger.getLogger(ConnectionFactory.class);

	/**
	 * @return {@link Connection}
	 * Retorna uma conexão com o banco de dados a partir do esquema passado como argumento
	 * */
	public static Connection getConnection(EnviromentEnum schema) throws DAOException {

		Connection conn = null;

		try {

			Class.forName(STR_DRIVER);
			conn = DriverManager.getConnection(
					schema.url, 
					schema.user, 
					schema.password
			);
			conn.setAutoCommit(false);

			return conn;
		} catch (ClassNotFoundException e) {

			logger.error("Driver (JDBC) não encontrado", e);
			throw new DAOException("Driver (JDBC) não encontrado", e);

		} catch (SQLException e) {

			logger.error("Erro ao obter a conexão", e);
			throw new DAOException("Erro ao obter a conexão", e);
		}
	}

	public enum EnviromentEnum  {
		PROD(STR_CON,USER,PASSWORD),
		HML(STR_CON_HML,USER,PASSWORD),
		DEV(STR_CON_HML,USER,PASSWORD)
		;

		private EnviromentEnum(String url, String user, String password) {
			this.url = url;
			this.user = user;
			this.password = password;
		}

		private String url;
		private String user;
		private String password;
	}
}
