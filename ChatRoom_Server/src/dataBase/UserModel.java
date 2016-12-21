package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 * Modified by He11o_Liu on 2016/12/13.
 */
public class UserModel {
    DBConnection db;

    public UserModel(DBConnection conn) {
        this.db = conn;
    }

    /**
     * getUserByJK
     * ï¿½ï¿½ï¿½ï¿½JKï¿½Å»ï¿½È¡UserInfoï¿½ï¿½ï¿½ï¿½
     * ï¿½ï¿½ï¿½ï¿½ï¿½Ã»ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ô¼ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ð±ï¿½
     *
     * @param JK
     * @return UserInfo user
     * @throws SQLException
     * @author Hcyue
     * @author He11o_Liu
     * 2016/12/13
     */
    public UserInfo getUserByJK(int JK) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM users where user_id=" + JK);
        if (!rs.next()) return null;
        UserInfo user = new UserInfo(rs);

        //Get Friend List
        CollectionModel collectionModel = new CollectionModel(db);
        List<CollectionInfo> coll = collectionModel.getCollectionsByJK(JK);
        CollectionInfo collection;
        List<UserInfo> memberlist;
        UserInfo member;

        int collectionCount = coll.size();
        int memberCount = 0;
        user.setCollectionCount((byte) collectionCount);

        String[] ListName = new String[collectionCount];
        byte[] bodyCount = new byte[collectionCount];// Ã¿ï¿½ï¿½ï¿½Ð¶ï¿½ï¿½Ù¸ï¿½ï¿½ï¿½
        int bodyNum[][] = new int[collectionCount][];// Ã¿ï¿½ï¿½ï¿½ï¿½ï¿½Ñµï¿½JKï¿½ï¿½
        int bodypic[][] = new int[collectionCount][];//ï¿½ï¿½ï¿½ï¿½Í·ï¿½ï¿½
        String bodyName[][] = new String[collectionCount][];// Ã¿ï¿½ï¿½ï¿½ï¿½ï¿½Ñµï¿½ï¿½Ç³ï¿½

        for (int j = 0; j < coll.size(); j++) {
            try {
                collection = coll.get(j);
                ListName[j] = collection.getName();
                memberlist = collection.getMembers();

                memberCount = memberlist.size();
                bodyCount[j] = (byte) memberCount;

                bodyNum[j] = new int[memberCount];
                bodyName[j] = new String[memberCount];
                bodypic[j] = new int[memberCount];

                for (int i = 0; i < memberlist.size(); i++) {
                    member = memberlist.get(i);
                    bodyNum[j][i] = member.getJKNum();
                    bodyName[j][i] = member.getNickName();
                    bodypic[j][i] = member.getAvatar();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //set friend list
        user.setBodyName(bodyName);
        user.setListName(ListName);
        user.setBodyCount(bodyCount);
        user.setBodyNum(bodyNum);
        user.setBodypic(bodypic);

        rs.close();
        return user;
    }

    /**
     * userAuthorization
     * ï¿½ï¿½Ö¤ï¿½Ã»ï¿½ï¿½ï¿½ï¿½ï¿½
     *
     * @param jk
     * @param passwd
     * @return boolean Result
     * @throws SQLException
     * @author Hcyue
     */
    public boolean userAuthorization(int jk, String passwd) throws SQLException {
        ResultSet rs = db.query(String.format("SELECT * FROM users WHERE user_id=%d AND password='%s'", jk, passwd));
        if (!rs.next()) return false;
        rs.close();
        return true;
    }

    public boolean isFriendsOfUser(int target, int jk) throws SQLException {
        String sql = String.format(
                "SELECT\n" +
                        "	*\n" +
                        "FROM\n" +
                        "	users\n" +
                        "WHERE\n" +
                        "	user_id = %d\n" +
                        "AND users.user_id IN (\n" +
                        "	SELECT\n" +
                        "		user_id\n" +
                        "	FROM\n" +
                        "		collection_entry\n" +
                        "	WHERE\n" +
                        "		collection_id IN (\n" +
                        "			SELECT\n" +
                        "				collection_id\n" +
                        "			FROM\n" +
                        "				collection\n" +
                        "			WHERE\n" +
                        "				user_id = %d\n" +
                        "		)\n" +
                        ")", target, jk);
        ResultSet rs = db.query(sql);
        return rs.next();
    }

    /**
     * getUsersInCollection
     *
     * @param coll_id
     * @return
     * @throws SQLException
     * @author Hcyue
     */
    public List<UserInfo> getUsersInCollection(int coll_id) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM users WHERE user_id IN (SELECT user_id FROM collection_entry WHERE collection_id = " + coll_id + ")");
        ArrayList<UserInfo> res = new ArrayList<>();
        while (rs.next()) {
            res.add(new UserInfo(rs));
        }
        rs.close();
        return res;
    }

    /**
     * createUser
     * ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
     *
     * @param passwd
     * @param nick
     * @param avatar
     * @return
     * @throws SQLException
     */
    public UserInfo createUser(String passwd, String nick, int avatar) throws SQLException {
        String sql = String.format("INSERT INTO users (nickname, password, avatar) VALUES ('%s', '%s', %d)", nick, passwd, avatar);
        int res = db.insertAndGet(sql);
        return getUserByJK(res);
    }

    /**
     * removeUser
     * É¾ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
     *
     * @param jk
     * @return
     * @throws SQLException
     */
    public int removeUser(int jk) throws SQLException {
        String sql = String.format("DELETE FROM users WHERE user_id=%d", jk);
        int res = db.update(sql);
        return res;
    }

    /**
     * @param addJk
     * @param ownJk
     * @param listName
     * @return 0 ³É¹¦
     * @throws Exception
     * @author He11o_Liu
     */
    public int addFriend(int addJk, int ownJk, String listName) throws Exception {
        //check add_jk
        UserInfo dest = getUserByJK(addJk);
        if (dest == null) {
            //²»´æÔÚÕâ¸öÈË
            return 1;
        }
        CollectionModel collectionModel = new CollectionModel(db);
        CollectionInfo collection = collectionModel.getCollectionByNameAndOwner(listName, ownJk);
        if (collection == null) {
            collection = collectionModel.createCollection(ownJk, listName);
        } else if (isFriendsOfUser(addJk, ownJk)) {
            return 2;
        }
        collectionModel.addUserToCollection(addJk, collection.getId());
        return 0;
    }

    public static void main(String args[]) throws SQLException {
        DBConnection db = DBConnection.getInstance();

        //UserModel userModel = new UserModel(db);
        CollectionModel collectionModel = new CollectionModel(db);
        //UserInfo user = userModel.getUserByJK(0);
        /*
        List<CollectionInfo>  coll = collectionModel.getCollectionsByJK(0);
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
        }*/

        CollectionInfo ci = collectionModel.getCollectionByNameAndOwner("ÎÒµÄºÃÓÑ", 6);
        List<UserInfo> testlist;
        if (ci == null) {
            System.out.println("No such list");
        }
        try {
            testlist = ci.getMembers();
            System.out.println(ci.getId() + "  " + ci.getName());
            for (int i = 0; i < testlist.size(); i++) {
                System.out.println(testlist.get(i).getNickName());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
