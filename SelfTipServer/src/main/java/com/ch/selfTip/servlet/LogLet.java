package com.ch.selfTip.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ch.selfTip.service.Service;

@WebServlet(name = "LogLet",urlPatterns="/LogLet",description="servlet")
public class LogLet extends HttpServlet {

    private static final long serialVersionUID = 369840050351775312L;
    
    private String text;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ���տͻ�����Ϣ
        String username = request.getParameter("username");
        username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
        String password = request.getParameter("password");
        System.out.println(username + "--" + password);

        // �½��������
        Service serv = new Service();

        // ��֤����
        ResultSet loged = serv.login(username, password);
        if (loged!=null) {
            System.out.print("Succss");
            text="success";	
            request.getSession().setAttribute("username", username);
            // response.sendRedirect("welcome.jsp");
        } else {
            System.out.print("Failed");
            text="failed";
        }

        // ������Ϣ���ͻ���
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(text);
        try {
        	int i=0;
			while(i<=8) {
				i++;
				out.print("next");
				out.print(loged.getString(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int i=0;
		while(i<=8) {
			i++;
			  try {
				System.out.print(loged.getString(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        out.flush();
        out.close();

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	// ���տͻ�����Ϣ
        String username = request.getParameter("username");
        username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
        String password = request.getParameter("password");
        System.out.println(username + "--" + password);

        // �½��������
        Service serv = new Service();

        // ��֤����
        ResultSet loged = serv.login(username, password);
        if (loged!=null) {
            System.out.print("Succss");
            text="success";	
            request.getSession().setAttribute("username", username);
            // response.sendRedirect("welcome.jsp");
        } else {
            System.out.print("Failed");
            text="failed";
        }

        // ������Ϣ���ͻ���
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(text);
        try {
        	int i=0;
			while(i<=8) {
				i++;
				out.print("next");
				out.print(loged.getString(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int i=0;
		while(i<=8) {
			i++;
			  try {
				System.out.print(loged.getString(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        out.flush();
        out.close();
    }
}