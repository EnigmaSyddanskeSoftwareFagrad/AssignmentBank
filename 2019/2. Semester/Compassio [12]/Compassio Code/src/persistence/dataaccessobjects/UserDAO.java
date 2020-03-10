package persistence.dataaccessobjects;

import persistence.util.DatabaseConnection;
import persistence.util.PasswordTool;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import persistence.util.ArgumentParser;

/**
 * Provides standard operations for interacting with the people table in the
 * database.
 *
 * @author Morten Kargo Lyngesen
 */
public class UserDAO implements DataAccessObject {

    private final BasicDataSource connectionPool;

    /**
     * Initializes the class by retrieving the connection pool.
     */
    public UserDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }
    
    /**
     * Validates user ID's.
     * @param userID ID to validate
     * @return <code>true</code> on valid userID, and <code>false</code> on invalid.
     */
    public boolean validateUserID(String userID) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement existCheck = db.prepareStatement("SELECT COUNT(userID) AS total FROM People WHERE userID = ?")) {
            existCheck.setLong(1, Long.parseLong(userID));
            ResultSet tuples = existCheck.executeQuery();
            tuples.next();
            return 0 < tuples.getInt("total");

        } catch (SQLException | NumberFormatException ex) {
            return false;

        }
    }

    /**
     * Validates a user password.
     * @param userID user id to validate password of.
     * @param password password to validate.
     * @return <code>true</code> on valid password, and <code>false</code> on invalid.
     */
    public boolean validateUserPassword(long userID, String password) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement existCheck = db.prepareStatement("SELECT hashedpassword, salt FROM People WHERE userID = ?")) {
            existCheck.setLong(1, userID);
            ResultSet res = existCheck.executeQuery();

            if (res.next()) {
                byte[] salt = res.getBytes("salt");
                byte[] passDB = res.getBytes("hashedpassword");
                byte[] hashedpass = PasswordTool.hashPassword(password, salt);

                return Arrays.equals(passDB, hashedpass);
            } else {
                return false;
            }

        } catch (SQLException | NumberFormatException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return false;
        }
    }

    @Override
    public String[] get(String... id) {
        if (id.length != 2) {
            return null;
        }
        String[] user = new String[5];
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT salt FROM people WHERE username=? AND inactive=false")) {
            statement.setString(1, id[0]);
            ResultSet rs = statement.executeQuery();
            if (rs.next() == false) {
                return null;
            } else {
                byte[] salt = rs.getBytes("salt");

                try (PreparedStatement checkStatement = db.prepareStatement("SELECT * FROM people WHERE username=? AND hashedpassword=?")) {
                    checkStatement.setString(1, id[0]);
                    checkStatement.setBytes(2, PasswordTool.hashPassword(id[1], salt));
                    ResultSet res = checkStatement.executeQuery();
                    if (res.next()) {
                        user[0] = res.getString("userid");
                        user[1] = res.getString("username");
                        user[2] = res.getString("firstname");
                        user[3] = res.getString("lastname");
                        String[] type = UserTypeRelationDAO.getInstance().get("-id " + user[0]);
                        if (type != null) {
                            user[4] = type[0];
                        }
                        return user;
                    } else {
                        return null;
                    }
                } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                    return null;
                }
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Returns all users that satisfy the condition(s)
     *
     * @param cond department ID's that the user must be associated with.
     * @return all entries that satisfy the conditions
     */
    @Override
    public List<String[]> getAll(String... cond) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement getUserType = db.prepareStatement("SELECT people.userid, username, firstname, lastname, typeid, inactive, departmentid FROM people NATURAL JOIN employeesofdepartment")) {

            ResultSet rs = getUserType.executeQuery();

            ArrayList<String[]> users = new ArrayList<>();

            while (rs.next()) {
                if (Arrays.asList(cond).contains(Long.toString(rs.getLong("departmentid")))) {
                    String[] user = new String[]{
                        rs.getLong("userid") + "",
                        rs.getString("username"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("typeid") + "",
                        rs.getBoolean("inactive") + ""
                    };
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean create(String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("firstName") && !options.containsKey("lastName")
                && !options.containsKey("userName") && !options.containsKey("password")
                && !options.containsKey("typeid")
                && !options.containsKey("departmentid")) {
            return false;
        }

        byte[] salt = PasswordTool.generateSalt();
        try (
                final Connection db = connectionPool.getConnection();
                final PreparedStatement statement = db.prepareStatement("INSERT INTO people(firstname, lastname, username, hashedpassword, salt, typeid, inactive) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                final PreparedStatement relationStatement = db.prepareStatement("INSERT INTO employeesofdepartment VALUES (?, ?)")) {
            statement.setString(1, options.get("firstName").get(0));
            statement.setString(2, options.get("lastName").get(0));
            statement.setString(3, options.get("userName").get(0));
            statement.setBytes(4, PasswordTool.hashPassword(options.get("password").get(0), salt));
            statement.setBytes(5, salt);
            statement.setInt(6, Integer.parseInt(options.get("typeid").get(0)));
            statement.setBoolean(7, false);
            statement.executeUpdate();

            ResultSet key = statement.getGeneratedKeys();

            if (key.next()) {
                relationStatement.setLong(1, Integer.parseInt(options.get("departmentid").get(0)));
                relationStatement.setLong(2, key.getLong(1));
                relationStatement.execute();
            }
            return true;
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return false;
        }
    }

    @Override
    public boolean update(long id, String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (options.containsKey("updatePassword")) {
            byte[] salt = PasswordTool.generateSalt();
            try (Connection db = connectionPool.getConnection();
                    PreparedStatement statement = db.prepareStatement("UPDATE people SET hashedpassword=?, salt=? WHERE username=?");) {
                statement.setBytes(1, PasswordTool.hashPassword(options.get("newPassword").get(0), salt));
                statement.setBytes(2, salt);
                statement.setString(3, options.get("username").get(0));
                statement.execute();
            } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
                return false;
            }
            return true;
        }
        try (final Connection db = connectionPool.getConnection();
                final PreparedStatement statement = db.prepareStatement("UPDATE people SET typeid=?, inactive=? WHERE userid=?")) {
            statement.setInt(1, Integer.parseInt(options.get("type").get(0)));
            statement.setBoolean(2, Boolean.parseBoolean(options.get("inactive").get(0)));
            statement.setLong(3, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("DELETE FROM people WHERE userid=?")) {
            statement.setLong(1, id);
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
