package dataBase;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class CollectionModel {
    DBConnection connection;
    public CollectionModel(DBConnection conn) {
        connection = conn;
    }
    private List<CollectionInfo> getColletionsByJK(int jk) throws SQLException {
        ResultSet rs = connection.query("SELECT * FROM collection where user_id=" + jk);
        int count = rs.getFetchSize();

        ArrayList<CollectionInfo> res = new ArrayList<>();

        while (rs.next()) {
            res.add(new CollectionInfo(rs));
        }
        return res;
    }
    private int addUserToCollection(int jk, int coll_id) throws SQLException {
        return connection.update(String.format("INSERT INTO collection_entry (user_id, collection_id) VALUES (%d, %d)", jk, coll_id));
    }
    public CollectionInfo createCollection(int jk, String collName) throws SQLException {
        String sql = String.format("INSERT INTO collection (name, user_id) VALUES ('%s', %d)", collName, jk);
        int id = connection.insertAndGet(sql);
        return getCollection(id);
    }
    public CollectionInfo getCollection(int id) throws SQLException {
        String sql = String.format("SELECT * FROM collection where collection_id=%d", id);
        ResultSet rs = connection.query(sql);
        return new CollectionInfo(rs);
    }
    public int removeCollection(int id) throws SQLException {
        String sql = String.format("DELETE FROM collection WHERE collection_id=%d", id);
        return connection.update(sql);
    }
    public List<CollectionInfo> getCollectionsByUser(UserInfo user) throws Exception {
        int jk = user.getJKNum();
        return getColletionsByJK(jk);
    }
    public List<CollectionInfo> getCollectionsByUser(int jk) throws SQLException {
        return getColletionsByJK(jk);
    }
    public static void main(String args[]) throws SQLException {
        DBConnection db = DBConnection.getInstance();
        UserModel userModel = new UserModel(db);
        CollectionModel collectionModel = new CollectionModel(db);

        UserInfo user = userModel.createUser("fuck", "hello", 1234);

        CollectionInfo coll = collectionModel.createCollection(user.getJKNum(), "test");

        System.out.println(coll.toString());
        userModel.removeUser(user.getJKNum());
        collectionModel.removeCollection(coll.getId());
    }
}
