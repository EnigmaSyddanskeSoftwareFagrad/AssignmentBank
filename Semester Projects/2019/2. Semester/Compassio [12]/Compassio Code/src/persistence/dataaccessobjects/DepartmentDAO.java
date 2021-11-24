package persistence.dataaccessobjects;

import persistence.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
import persistence.util.ArgumentParser;

/**
 * Provides standard operations for interacting with the Department table in the
 * database.
 *
 * @author Morten Kargo Lyngesen
 */
public class DepartmentDAO implements DataAccessObject {

    private final BasicDataSource connectionPool;

    /**
     * Initializes the class.
     */
    public DepartmentDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }

    @Override
    public String[] get(String... id) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT * FROM department WHERE departmentid=?")) {
            statement.setInt(1, (int) Integer.parseInt(id[0]));
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnLength = rsmd.getColumnCount();
            if (rs.next() == false) {
                return null;
            } else {
                String[] department = new String[columnLength - 1];
                for (int i = 1; i < columnLength; i++) {
                    department[i - 1] = rs.getString(i);
                }
                return department;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ArrayList getAll(String... cond) {
        ArrayList<String> departments = new ArrayList<>();
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT departmentid, name FROM Department")) {
            ResultSet tuples = statement.executeQuery();
            while (tuples.next()) {
                if (tuples.getLong(1) != -1) {
                    String departmentInfo = tuples.getLong(1) + " " + tuples.getString(2);
                    departments.add(departmentInfo);
                } else {
                    String departmentInfo = tuples.getLong(1) + " " + "Ukendt";
                    departments.add(departmentInfo);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departments;
    }

    @Override
    public boolean create(String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("id") && !options.containsKey("name")
                && !options.containsKey("phonenumber") && !options.containsKey("address")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("INSERT INTO Department VALUES (?, ?, ?, ?)")) {
            statement.setLong(1, Integer.parseInt(options.get("id").get(0)));
            statement.setString(2, options.get("name").get(0));
            statement.setInt(3, Integer.parseInt(options.get("phonenumber").get(0)));
            statement.setString(4, options.get("address").get(0));
            statement.execute();
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean update(long id, String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("name") && !options.containsKey("phonenumber")
                && !options.containsKey("address")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("UPDATE Department SET name=?, phonenumber=?, address=? WHERE departmentid=?")) {
            statement.setLong(4, id);
            statement.setString(1, options.get("name").get(0));
            statement.setInt(2, Integer.parseInt(options.get("phonenumber").get(0)));
            statement.setString(3, options.get("address").get(0));
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("DELETE FROM Department WHERE departmentid=?")) {
            statement.setLong(1, id);
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
