package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class CollectionModel {
    private List<CollectionInfo> getColletionsByJK(int jk) throws SQLException {

        DBConnection conn = DBConnection.getInstance();
        ResultSet rs = conn.query("SELECT * FROM Collection where user_id=" + jk);
        int count = rs.getFetchSize();

        ArrayList<CollectionInfo> res = new ArrayList<>();

        while (rs.next()) {
            res.add(new CollectionInfo(rs));
        }
        return res;
    }
    private int addUserToCollection(int jk, int coll_id) throws SQLException {
        DBConnection conn = DBConnection.getInstance();
        return conn.update(String.format("INSERT TO collection_entry (user_id, collection_id) VALUES (%d, %d)", jk, coll_id));
    }
    public List<CollectionInfo> getCollectionsByUser(UserInfo user) throws Exception {
        int jk = user.getJKNum();
        return getColletionsByJK(jk);
    }
    public List<CollectionInfo> getCollectionsByUser(int jk) throws SQLException {
        return getColletionsByJK(jk);
    }
}
