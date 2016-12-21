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
     * getColletionsByJK
     * �����û���JK�Ż�ȡcollectionInfo ��list
     * @param jk JK��
     * @return List<CollectionInfo>
     * @throws SQLException SQL�쳣
     */
    public List<CollectionInfo> getColletionsByJK(int jk) throws SQLException {
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
     * @param jk �û�JK��
     * @param coll_id �б�id
     * @return ��ӵ���Ŀ
     * @throws SQLException SQL�쳣
     */
    public int addUserToCollection(int jk, int coll_id) throws SQLException {
        return connection.update(String.format("INSERT INTO collection_entry (user_id, collection_id) VALUES (%d, %d)", jk, coll_id));
    }
    
    /**
     * createCollection
     * @param jk �û�JK��
     * @param collName �б���
     * @return �½����б�
     * @throws SQLException SQL�쳣
     */
    public CollectionInfo createCollection(int jk, String collName) throws SQLException {
        String sql = String.format("INSERT INTO collection (name, user_id) VALUES ('%s', %d)", collName, jk);
        int id = connection.insertAndGet(sql);
        return getCollection(id);
    }
    
    /**
     * getCollection
     * ��ȡָ���ĺ����б�
     * @param id �б�ID
     * @return �ҵ����б���Ϊnull
     * @throws SQLException SQL�쳣
     */
    public CollectionInfo getCollection(int id) throws SQLException {
        String sql = String.format("SELECT * FROM collection where collection_id=%d", id);
        ResultSet rs = connection.query(sql);
        CollectionInfo result = new CollectionInfo(rs);
        rs.close();
        return result;
    }
    public CollectionInfo getCollectionByNameAndOwner(String name, int jk) throws SQLException {
        String sql = String.format("SELECT * FROM collection where user_id=%d AND name='%s'", jk, name);
        ResultSet rs = connection.query(sql);
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
        return getColletionsByJK(jk);
    }
    
    /**
     * getCollectionsByUser
     * @param jk
     * @return
     * @throws SQLException
     */
    public List<CollectionInfo> getCollectionsByUser(int jk) throws SQLException {
        return getColletionsByJK(jk);
    }
    /*
    public static void main(String args[]) throws SQLException {
        DBConnection db = DBConnection.getInstance();
        //UserModel userModel = new UserModel(db);
        CollectionModel collectionModel = new CollectionModel(db);
        //UserInfo user = userModel.getUserByJK(0);
        List<CollectionInfo>  coll = collectionModel.getColletionsByJK(0);
		List<UserInfo> testlist;
        for(int j = 0; j<coll.size();j++){
        	try {
        		System.out.println(coll.get(j).toString());
				testlist = coll.get(j).getMembers();
				for(int i = 0; i < testlist.size();i++){
		        	System.out.println(testlist.get(i).getNickName());
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }*/
}
