package com.ch.selfTip.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ch.selfTip.service.Service;

@WebServlet(name = "RegLet",urlPatterns="/RegLet",description="servlet")
public class RegLet extends HttpServlet {



	   private static final long serialVersionUID = 369840050351775312L;
	   
	   private String text;

	   public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        // ���տͻ�����Ϣ
	        String username = request.getParameter("username");
	        username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
	        String password = request.getParameter("password");
	        password = new String(password.getBytes("ISO-8859-1"), "UTF-8");
	        String phone = request.getParameter("phone");
	        phone = new String(phone.getBytes("ISO-8859-1"), "UTF-8");
	        String email = request.getParameter("email");
	        email = new String(email.getBytes("ISO-8859-1"), "UTF-8");
	        System.out.println(username + "--" + password+ "--" + phone + "--" +email);

	        // �½��������
	        Service serv = new Service();

	        // ��֤����
	        boolean loged = serv.register(username, password,phone,email);
	        if (loged) {
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
	        out.flush();
	        out.close();

	    }


	    public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        // ���տͻ�����Ϣ
	        String username = request.getParameter("username");
	        username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
	        String password = request.getParameter("password");
	        password = new String(password.getBytes("ISO-8859-1"), "UTF-8");
	        String phone = request.getParameter("phone");
	        phone = new String(phone.getBytes("ISO-8859-1"), "UTF-8");
	        String email = request.getParameter("email");
	        email = new String(email.getBytes("ISO-8859-1"), "UTF-8");
	        System.out.println(username + "--" + password+ "--" + phone + "--" +email);

	        // �½��������
	        Service serv = new Service();

	        // ��֤����
	        boolean loged = serv.register(username, password,phone,email);
	        if (loged) {
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
	        out.flush();
	        out.close();

	    }

}
