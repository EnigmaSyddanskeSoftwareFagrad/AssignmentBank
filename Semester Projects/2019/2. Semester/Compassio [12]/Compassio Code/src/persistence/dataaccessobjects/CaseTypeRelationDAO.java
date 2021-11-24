package persistence.dataaccessobjects;

import persistence.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
import persistence.util.ArgumentParser;

/**
 * Provides standard operations for interacting with the CaseTypeRelation table 
 * in the database.
 * @author Morten Kargo Lyngesen
 */
public class CaseTypeRelationDAO implements DataAccessObject {

    private final BasicDataSource connectionPool;
    
    private static final CaseTypeRelationDAO INSTANCE = new CaseTypeRelationDAO();

    private CaseTypeRelationDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }
    
     /**
     * Returns a singleton instance of the class.
     * @return a singleton instance of the class.
     */
    public static CaseTypeRelationDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public String[] get(String... id) {      
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT typeid FROM CaseTypeRelation WHERE name = ?")) {
            statement.setString(1, id[0]);
            ResultSet tuples = statement.executeQuery();
            if (tuples.next()){
                return new String[]{Long.toString(tuples.getLong(1))};
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<String[]> getAll(String... cond) {
        ArrayList<String[]> list = new ArrayList<>();
        try (Connection db = connectionPool.getConnection()) {
            ResultSet result = db.prepareStatement("SELECT * FROM casetyperelation").executeQuery();
            while (result.next()) {
                list.add(new String[]{result.getString("name"), result.getString("typeid")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean create(String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("name")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("INSERT INTO casetyperelation VALUES (?, DEFAULT)");) {
            statement.setString(1, options.get("name").toString());
            statement.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean update(long id, String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("name")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("UPDATE CaseTypeRelation SET name=? WHERE typeid=?")) {
            statement.setLong(2, id);
            statement.setString(1, options.get("name").get(0));
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("DELETE FROM casetyperelation WHERE typeid=?")) {
            statement.setLong(1, id);
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
