package com.ch.selfTip.controller;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ch.selfTip.service.Service;


@RestController
public class Controller {
	
	@RequestMapping("/changemsg")
	public String changemessage(@RequestBody String map) {
		JSONObject obj=JSON.parseObject(map);
		String changemessage=(String)obj.get("changemessage");
		String changemessagename=(String)obj.get("changemessagename");
		String changemessageid=(String)obj.get("changemessageid");	
		System.out.print("//"+changemessage+"//"+changemessagename+"//"+changemessageid);
		
		Service serv = new Service();

		String text="";
        boolean chged = serv.changemessage(changemessagename, changemessageid,changemessage);
        if (chged) {
            System.out.print("Succss");
            text="success";
        } else {
            System.out.print("Failed");
            text="failed";
        }
		return text;
	}
	@RequestMapping("/changepsd")
	public String changepassword(@RequestBody String map) {
		JSONObject obj=JSON.parseObject(map);
		String newpassword=(String)obj.get("newpassword");
		String changepsdid=(String)obj.get("changepsdid");
		System.out.print("//"+changepsdid+"//"+newpassword+"//");
		
		Service serv = new Service();

		String text="";
        boolean chged = serv.changepsd(changepsdid,newpassword);
        if (chged) {
            System.out.print("Succss");
            text="success";
        } else {
            System.out.print("Failed");
            text="failed";
        }
		return text;
	}  
	
	@RequestMapping("/Login")
	public ModelAndView login() {
		return new ModelAndView("Login");
	}
	
	@RequestMapping("/Register")
	public ModelAndView register() {
		return new ModelAndView("Register");
	}
	@RequestMapping("/WebSocke")
	public ModelAndView websocket() {
		return new ModelAndView("WebSocket");
	}
	@RequestMapping("/userinfo")
	public ModelAndView userinfo() {
		return new ModelAndView("userinfo");
	}
	@RequestMapping("/userinfoimp")
	public String userinofimp() {
		Service ser=new Service();
		ResultSet rs=ser.alldata();
		JSONArray jsonarray=new JSONArray();
	       // 获取列数 
		try {
			ResultSetMetaData metaData = rs.getMetaData();
	       // 遍历ResultSet中的每条数据 
	       while (rs.next()) { 
	    	   int columnCount = metaData.getColumnCount(); 
	            // 遍历每一列 
	        	JSONObject jsonObj = new JSONObject();  
	            for (int i = 1; i <= columnCount; i++) { 
	            	   
	                String columnName =metaData.getColumnLabel(i); 
	 
	                String value = rs.getString(columnName); 
	 
	                jsonObj.put(columnName, value); 
	 
	            }   
	            jsonarray.add(jsonObj);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	 
	       return jsonarray.toJSONString(); 
		
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String id) {
		Service ser=new Service();
		if(ser.delete(id)) {
			return "删除成功" ;
		}else {

			return "删除失败" ;
		}
	}
	@RequestMapping("/users")
	public ModelAndView users() {
		return new ModelAndView("users");
	}
	
}
