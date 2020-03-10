package persistence.dataaccessobjects;

import persistence.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import persistence.util.ArgumentParser;

/**
 * Provides standard operations for interacting with the CaseUserRelationDAO
 * table in the database.
 * @author Morten Kargo Lyngesen
 */
public class CaseUserRelationDAO implements DataAccessObject {
    
    private final BasicDataSource connectionPool;

    /**
     * Initializes the class.
     */
    public CaseUserRelationDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }

    @Override
    public String[] get(String... id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String[]> getAll(String... cond) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Creates an entry in the CaseUserRelation table
     * @param args Must specify an "-id" for the case and "-users" for users assigned to case
     * @return <code>true</code> on success and <code>false</code> on failure.
     */
    @Override
    public boolean create(String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("id") && !options.containsKey("users")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("INSERT INTO CaseUserRelation VALUES (?, ?)");) {
            for (int i = 0; i < options.get("users").size(); i++) {
                //REGEX expression to remove all punction chars except hyphens
                statement.setString(1, options.get("id").toString().replaceAll("[^\\P{P}-]+",""));
                statement.setLong(2, Long.parseLong(options.get("users").get(i)));
                statement.execute();
            }
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean update(long id, String... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
