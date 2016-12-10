package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class UserModel {
    DBConnection db;

    UserModel(DBConnection conn) {
        this.db = conn;
    }


    public UserInfo getUserByJK(int JK) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM User where user_id=" + JK);
        UserInfo user;
        user = new UserInfo(rs);
        return user;
    }

    public List<UserInfo> getUsersInCollection(int coll_id) throws SQLException {

        ResultSet rs = db.query("SELECT * FROM User WHERE user_id IN (SELECT user_id FROM collection_entry WHERE collection_id = "+ coll_id +")");
        int size = rs.getFetchSize();
        ArrayList<UserInfo> res = new ArrayList<>();

        while (rs.next()) {
            res.add(new UserInfo(rs));
        }

        return res;
    }

    public UserInfo newUser(String passwd, String nick, int avatar) throws SQLException {
        String sql = String.format("INSERT TO User (nick_name, pass_word, avatar) VALUES (%s, %s, %d)", nick, passwd, avatar);
        int res = db.insertAndGet(sql);
        return getUserByJK(res);
    }
}
