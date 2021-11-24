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
 *
 * @author Morten Kargo Lyngesen
 */
public class UserTypeRelationDAO implements DataAccessObject {

    private final BasicDataSource connectionPool;
    
    private static final UserTypeRelationDAO INSTANCE = new UserTypeRelationDAO();

    private UserTypeRelationDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }
    
    /**
     * Returns a singleton instance of the class.
     * @return a singleton instance of the class.
     */
    public static UserTypeRelationDAO getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String[] get(String... id) {
        Map<String, List<String>> options = ArgumentParser.parse(id);
        if (!options.containsKey("id")) { 
            return null;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement getUserType = db.prepareStatement("SELECT name FROM "
                        + "people, usertyperelation "
                        + "WHERE people.typeID = usertyperelation.typeID AND userid=?")) {

            getUserType.setLong(1, Long.parseLong(options.get("id").get(0)));

            ResultSet rs = getUserType.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString("name")};
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<String[]> getAll(String... cond) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement getUserType = db.prepareStatement("SELECT * FROM usertyperelation")) {

            ResultSet rs = getUserType.executeQuery();

            if (rs.next()) {
                ArrayList<String[]> names = new ArrayList<>();
                do {
                    names.add(new String[]{rs.getInt("typeid") + "," + rs.getString("name")});
                } while (rs.next());
                return names;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean create(String... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(long id, String... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
