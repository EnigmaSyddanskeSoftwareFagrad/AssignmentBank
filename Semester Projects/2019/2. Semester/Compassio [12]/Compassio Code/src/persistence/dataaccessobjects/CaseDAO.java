package persistence.dataaccessobjects;

import persistence.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import persistence.util.ArgumentParser;

/**
 * Provides standard operations for interacting with the socialcase table in the
 * database.
 *
 * @author Morten Kargo Lyngesen
 */
public class CaseDAO implements DataAccessObject {

    private final BasicDataSource connectionPool;

    private static final CaseDAO INSTANCE = new CaseDAO();

    private CaseDAO() {
        this.connectionPool = DatabaseConnection.getInstance().getConnectionPool();
    }
    
     /**
     * Returns a singleton instance of the class.
     * @return a singleton instance of the class.
     */
    public static CaseDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public String[] get(String... id) {
        if (id.length < 1) {
            return null;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("SELECT * FROM socialcase WHERE caseid=?")) {
            statement.setInt(1, (int) Integer.parseInt(id[0]));
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnLength = rsmd.getColumnCount();
            if (rs.next() == false) {
                return null;
            } else {
                String[] socialcase = new String[columnLength - 1];
                for (int i = 1; i < columnLength; i++) {
                    socialcase[i - 1] = rs.getString(i);
                }
                return socialcase;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Returns all cases by either a specific department or user.
     *
     * @param cond Use "-department" to get all cases by a department or "-user"
     * to get all cases by a specific user
     * @return all cases by either a specific department or user.
     */
    @Override
    public ArrayList<String[]> getAll(String... cond) {
        Map<String, List<String>> options = ArgumentParser.parse(cond);
        if (!options.containsKey("id")) {
            return null;
        }
        ArrayList<String[]> cases = new ArrayList<>();
        String byDepartment = "SELECT * FROM SocialCase NATURAL JOIN CPR NATURAL JOIN CaseTypeRelation WHERE 'departmentID'=(?)";
        String byUser = "SELECT * FROM SocialCase NATURAL JOIN CaseUserRelation NATURAL JOIN CPR NATURAL JOIN CaseTypeRelation WHERE userID=(?)";
        String query;
        if (options.containsKey("department")) {
            query = byDepartment;
        } else {
            query = byUser;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement(query)) {
            statement.setLong(1, Long.parseLong(options.get("id").get(0)));
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String[] singleCase = new String[10];
                singleCase[0] = rs.getString("firstname");
                singleCase[1] = rs.getString("lastname");
                singleCase[2] = rs.getString("caseid");
                singleCase[3] = rs.getString("cprnumber");
                singleCase[4] = rs.getString("name");
                singleCase[5] = rs.getString("mainBody");
                if (rs.getString("datecreated") != null) {
                    singleCase[6] = rs.getString("datecreated").substring(0, 10);
                }
                if (rs.getString("dateclosed") != (null)) {
                    singleCase[7] = rs.getString("dateclosed").substring(0, 10);
                }
                singleCase[8] = rs.getString("departmentid");
                singleCase[9] = rs.getString("inquiry");
                cases.add(singleCase);
            }
        } catch (SQLException ex) {
            return null;
        }
        return cases;
    }

    @Override
    public boolean create(String... args) {
        Map<String, List<String>> options = ArgumentParser.parse(args);
        if (!options.containsKey("caseID") && !options.containsKey("cprNumber")
                && !options.containsKey("type") && !options.containsKey("mainBody")
                && !options.containsKey("dateCreated")
                && !options.containsKey("departmentID")
                && !options.containsKey("inquiry")) {
            return false;
        }
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("INSERT INTO socialcase VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (caseid) DO UPDATE SET (mainbody, departmentid, inquiry, typeid, dateclosed) = (?, ?, ?, ?, ?)")) {
            statement.setString(1, options.get("caseID").get(0));
            statement.setLong(2, Long.parseLong(options.get("cprNumber").get(0)));
            long typeid = Long.parseLong(CaseTypeRelationDAO.getInstance().get(options.get("type").get(0))[0]);
            statement.setLong(8, typeid);
            statement.setLong(12, typeid);

            StringBuilder sb = new StringBuilder();
            options.get("mainBody").stream().map((String mainBody1) -> {
                sb.append(mainBody1);
                return mainBody1;
            }).forEachOrdered((String _item) -> {
                sb.append(" ");
            });

            String mainBody = sb.toString();
            statement.setString(3, mainBody);
            statement.setString(9, mainBody);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date date;

            String dateCreatedStr = options.get("dateCreated").get(0);
            try {
                date = sdf1.parse(dateCreatedStr);
            } catch (ParseException ex) {
                return false;
            }

            java.sql.Date dateCreated = new java.sql.Date(date.getTime());

            if (dateCreated != null) {
                statement.setDate(4, dateCreated);
            } else {
                statement.setDate(4, null);
            }
            java.sql.Date dateClosed = null;
            if (!options.get("dateClosed").get(0).equals("null")) {
                String dateClosedStr = options.get("dateClosed").get(0);

                try {
                    date = sdf1.parse(dateClosedStr);
                } catch (ParseException ex) {
                    return false;
                }

                dateClosed = new java.sql.Date(date.getTime());
            }
            if (dateClosed != null) {
                statement.setDate(5, dateClosed);
                statement.setDate(13, dateClosed);
            } else {
                statement.setDate(5, null);
                statement.setDate(13, null);
            }

            StringBuilder builder = new StringBuilder();
            options.get("inquiry").stream().map((String inquiry1) -> {
                builder.append(inquiry1);
                return inquiry1;
            }).forEachOrdered((String _item) -> {
                builder.append(" ");
            });

            String inquiry = builder.toString();
            int departmentID = Integer.parseInt(options.get("departmentID").get(0));
            statement.setInt(6, departmentID);
            statement.setInt(10, departmentID);
            statement.setString(7, inquiry);
            statement.setString(11, inquiry);
            statement.execute();
            return true;

        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean update(long id, String[] args) {
        //The create case query is built to update if already excists.
        return create(args);
    }

    @Override
    public boolean delete(long id) {
        try (Connection db = connectionPool.getConnection();
                PreparedStatement statement = db.prepareStatement("DELETE FROM socialcase WHERE caseid=?")) {
            statement.setLong(1, id);
            statement.executeQuery();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
