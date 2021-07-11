package com.ch.selfTip.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ch.selfTip.db.DBManager;

public class Service {

    public ResultSet login(String username, String password) {

        // ��ȡSql��ѯ���
        String logSql = "select * from user where muser = '" + username
                + "' and mpsd ='" + password + "'";

        // ��ȡDB����
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        // ����DB����
        try {
            ResultSet rs = sql.executeQuery(logSql);
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.closeDB();
        return null;
    }

    public Boolean register(String username, String password,String phone,String email ) {
    
        // ��ȡSql��ѯ���
        String regSql = "insert into user(muser,mpsd,memail,mphone) values('"+ username+ "','"+ password+"','"+ phone + "','" + email + "') ";
// ��ȡDB����
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        
        return false;
    }
    
    public Boolean changemessage(String changemessagename, String changemessageid,String changemessage) {
        
    
    	
        String chgSql = "update user set "+ changemessagename + " =' "+ changemessage + "' WHERE mid = ' "+changemessageid  + "'";
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        int ret = sql.executeUpdate(chgSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        
        return false;
    }
    
public Boolean changepsd(String changepsdid, String newpassword) {
        
    
    	
        String chgSql = "update user set mpsd ='"+ newpassword + "' WHERE mid = ' "+changepsdid  + "'";
        DBManager sql = DBManager.createInstance();
        sql.connectDB();

        int ret = sql.executeUpdate(chgSql);
        if (ret != 0) {
            sql.closeDB();
            return true;
        }
        sql.closeDB();
        
        return false;
    }
public ResultSet alldata() {

    // ��ȡSql��ѯ���
    String selSql = "select * from user ";

    // ��ȡDB����
    DBManager sql = DBManager.createInstance();
    sql.connectDB();

    // ����DB����
    try {
        ResultSet rs = sql.executeQuery(selSql);
        if (rs.next()) {
            return rs;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    sql.closeDB();
    return null;
}
public Boolean delete(String id) {
    
    
	
    String chgSql = "delete from user where mid ='"+id+"'";
    DBManager sql = DBManager.createInstance();
    sql.connectDB();

    int ret = sql.executeUpdate(chgSql);
    if (ret != 0) {
        sql.closeDB();
        return true;
    }
    sql.closeDB();
    
    return false;
}
    
}