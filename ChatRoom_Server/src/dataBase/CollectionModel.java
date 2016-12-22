package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class CollectionModel {
    private DBConnection connection;
    public CollectionModel(DBConnection conn) {
        connection = conn;
    }
    
    /**
     * getCollectionsByJK
     * 根据用户的JK号获取collectionInfo 的list
     * @param jk JK号
     * @return List<CollectionInfo>
     * @throws SQLException SQL异常
     */
    public List<CollectionInfo> getCollectionsByJK(int jk) throws SQLException {
        ResultSet rs = connection.query("SELECT * FROM collection where user_id=" + jk);
        ArrayList<CollectionInfo> res = new ArrayList<>();
        while (rs.next()) {
            res.add(new CollectionInfo(rs));
        }
        rs.close();
        return res;
    }
    
    /**
     * addUserToCollection
     * @param jk 用户JK号
     * @param coll_id 列表id
     * @return 添加的数目
     * @throws SQLException SQL异常
     */
    public int addUserToCollection(int jk, int coll_id) throws SQLException {
        return connection.update(String.format("INSERT INTO collection_entry (user_id, collection_id) VALUES (%d, %d)", jk, coll_id));
    }
    
    /**
     * createCollection
     * @param jk 用户JK号
     * @param collName 列表名
     * @return 新建的列表
     * @throws SQLException SQL异常
     */
    public CollectionInfo createCollection(int jk, String collName) throws SQLException {
        String sql = String.format("INSERT INTO collection (name, user_id) VALUES ('%s', %d)", collName, jk);
        int id = connection.insertAndGet(sql);
        return getCollection(id);
    }
    
    /**
     * getCollection
     * 获取指定的好友列表
     * @param id 列表ID
     * @return 找到的列表。无为null
     * @throws SQLException SQL异常
     */
    public CollectionInfo getCollection(int id) throws SQLException {
        String sql = String.format("SELECT * FROM collection where collection_id=%d", id);
        ResultSet rs = connection.query(sql);
        if (!rs.next()) {
            return null;
        }
        CollectionInfo result = new CollectionInfo(rs);
        rs.close();
        return result;
    }

    public boolean isUserInCollection(int userJK, int collectionId) throws SQLException {
        ResultSet rs = connection.query(String.format("SELECT * FROM collection_entry where user_id=%d AND collection_id=%d", userJK, collectionId));
        return rs.next();
    }
    
    /**
     * 
     * @param name
     * @param jk
     * @return
     * @throws SQLException
     */
    public CollectionInfo getCollectionByNameAndOwner(String name, int jk) throws SQLException {
        String sql = String.format("SELECT * FROM collection where user_id=%d AND name='%s'", jk, name);
        ResultSet rs = connection.query(sql);
        if (!rs.next()) {
            return null;
        }
        CollectionInfo result = new CollectionInfo(rs);
        rs.close();
        return result;
    }
    
    /**
     * removeCollection
     * @param id
     * @return
     * @throws SQLException
     */
    public int removeCollection(int id) throws SQLException {
        String sql = String.format("DELETE FROM collection WHERE collection_id=%d", id);
        return connection.update(sql);
    }
    
    /**
     * getCollectionsByUser
     * @param user
     * @return
     * @throws Exception
     */
    public List<CollectionInfo> getCollectionsByUser(UserInfo user) throws Exception {
        int jk = user.getJKNum();
        return getCollectionsByJK(jk);
    }
    
    /**
     * getCollectionsByUser
     * @param jk
     * @return
     * @throws SQLException
     */
    public List<CollectionInfo> getCollectionsByUser(int jk) throws SQLException {
        return getCollectionsByJK(jk);
    }
    
//    public static void main(String args[]) throws SQLException {
//        DBConnection db = DBConnection.getInstance();
//        
//        //UserModel userModel = new UserModel(db);
//        CollectionModel collectionModel = new CollectionModel(db);
//        //UserInfo user = userModel.getUserByJK(0);
//        /*
//        List<CollectionInfo>  coll = collectionModel.getCollectionsByJK(0);
//		List<UserInfo> testlist;
//        for(int j = 0; j<coll.size();j++){
//        	try {
//        		System.out.println(coll.get(j).toString());
//				testlist = coll.get(j).getMembers();
//				for(int i = 0; i < testlist.size();i++){
//		        	System.out.println(testlist.get(i).getNickName());
//		        }
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//        }*/
//     
//        CollectionInfo ci = collectionModel.getCollectionByNameAndOwner("我的好友", 0);
//        List<UserInfo> testlist;
//		try {
//			testlist = ci.getMembers();
//			System.out.println(ci.getId()+"  "+ci.getName());
//		    for(int i = 0; i< testlist.size();i++){
//		        System.out.println(testlist.get(i).getNickName());
//		    }
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
}
