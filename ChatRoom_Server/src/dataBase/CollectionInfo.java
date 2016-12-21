package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 */
public class CollectionInfo {
    private int ownerJK;
    private List<UserInfo> members;
    private String name;
    private int id;

    /**
     * CollectionInfo
     * 从collection表中读取的数据构造info对象
     * @param rs
     * @throws SQLException
     */
    CollectionInfo(ResultSet rs) throws SQLException {
        ownerJK = rs.getInt("user_id");
        name = rs.getString("name");
        id = rs.getInt("collection_id");
    }
    
    /**
     * getMembers
     * 获取一个collection中的对象
     * @return List<UserInfo>
     * @throws Exception
     */
    public List<UserInfo> getMembers() throws Exception {
        if (members == null) {
            DBConnection conn = DBConnection.getInstance();
            UserModel userModel = new UserModel(conn);
            members = userModel.getUsersInCollection(id);
            conn.close();
        }
        return members;
    }
    
    /**
     * 输出测试
     */
    public String toString() {
        return String.format("Collection: %s, id: %d, ownerJK: %d", name, id, ownerJK);
    }
    

    public int getOwnerJK() {
        return ownerJK;
    }

    public void setOwnerJK(int ownerJK) {
        this.ownerJK = ownerJK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
