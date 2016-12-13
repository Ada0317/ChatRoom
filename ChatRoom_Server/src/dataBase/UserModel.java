package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class UserModel {
    DBConnection db;

    public UserModel(DBConnection conn) {
        this.db = conn;
    }


    public UserInfo getUserByJK(int JK) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM users where user_id=" + JK);
        if (rs.next()==false) return null;
        UserInfo user = new UserInfo(rs);
        return user;
    }
    
    public boolean userAuthorization(int jk, String passwd) throws SQLException {
        ResultSet rs = db.query(String.format("SELECT * FROM users WHERE user_id=%d AND password='%s'", jk, passwd));
        if (rs.next() == false) return false;
        return true;
    }
    
    public List<UserInfo> getUsersInCollection(int coll_id) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM users WHERE user_id IN (SELECT user_id FROM collection_entry WHERE collection_id = "+ coll_id +")");
        int size = rs.getFetchSize();
        ArrayList<UserInfo> res = new ArrayList<>();
        while (rs.next()) {
            res.add(new UserInfo(rs));
        }
        return res;
    }

    public UserInfo createUser(String passwd, String nick, int avatar) throws SQLException {
        String sql = String.format("INSERT INTO users (nickname, password, avatar) VALUES ('%s', '%s', %d)", nick, passwd, avatar);
        int res = db.insertAndGet(sql);
        return getUserByJK(res);
    }
    
    public int removeUser(int jk) throws SQLException {
        String sql = String.format("DELETE FROM users WHERE user_id=%d", jk);
        int res = db.update(sql);
        return res;
    }

    
    public static void main(String args[]) throws SQLException {
        UserModel model = new UserModel(DBConnection.getInstance());
        //UserInfo user = model.createUser("123", "He11o_Liu", 001);
        //int jk = user.getJKNum();
        UserInfo res = model.getUserByJK(0);
        if(res!= null) System.out.printf("jk: %d nick: %s\n", res.getJKNum(), res.getNickName());
        else System.out.println("Not in the database");
        System.out.println(model.userAuthorization(0,"123"));
        //model.removeUser(jk);
    }
}
