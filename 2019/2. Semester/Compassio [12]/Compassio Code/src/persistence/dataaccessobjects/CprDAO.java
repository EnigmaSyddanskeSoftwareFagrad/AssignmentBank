package persistence.dataaccessobjects;

import persistence.util.DatabaseConnection;
import persistence.util.ArgumentParser;
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

/**
 * Provides standard operations for interacting with the cpr table in the database.
 *
 * @author Morten Kargo Lyngesen
 */
public class CprDAO implements DataAccessObject {

    private final BasicDataSource connectionPool;

    /**
     * Initializes the class.
     */
    public CprDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }

    @Override
    public String[] get(String... id) {
        if (id.length < 1) {
            return null;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT * FROM cpr WHERE cprnumber=?")) {
            statement.setInt(1, (int) Integer.parseInt(id[0]));
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnLength = rsmd.getColumnCount();
            if (rs.next() == false) {
                return null;
            } else {
                String[] person = new String[columnLength - 1];
                for (int i = 1; i < columnLength; i++) {
                    person[i - 1] = rs.getString(i);
                }
                return person;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<String[]> getAll(String... cond) {
        ArrayList<String[]> people = new ArrayList<>();
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT * FROM cpr")) {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnLength = rsmd.getColumnCount();
            while (rs.next()) {
                String[] person = new String[columnLength - 1];
                for (int i = 1; i < columnLength; i++) {
                    person[i - 1] = rs.getString(i);
                }
                people.add(person);
            }   
                return people;    
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean create(String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("cpr") && !options.containsKey("firstname")
                && !options.containsKey("lastname")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("INSERT INTO cpr VALUES (?, ?, ?)")) {
            statement.setLong(1, Long.parseLong(options.get("cpr").get(0)));
            statement.setString(2, options.get("firstname").get(0));
            statement.setString(3, options.get("lastname").get(0));
            statement.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean update(long id, String[] args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("UPDATE cpr SET firstname = ?, lasttname= ? WHERE cprnumber=?")) {
            statement.setString(1, options.get("firstname").get(0));
            statement.setString(1, options.get("lastname").get(0));
            statement.setLong(3, id);
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("DELETE FROM cpr WHERE cprnumber=?")) {
            statement.setLong(1, id);
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
