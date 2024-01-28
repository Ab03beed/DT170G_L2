package com.example.dt170g_l2;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.annotation.Resource;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.sql.DataSource;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }


    public void destroy() {
    }



    @Resource(name = "jdbc/testdb")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT AGE, LENGTH FROM person")) { // Adjusted query

            StringBuilder responseHtml = new StringBuilder("<html><body>");
            responseHtml.append("<h1>Database Connection Successful</h1>");
            responseHtml.append("<table border='1'>");
            responseHtml.append("<tr><th>Age</th><th>Length (cm)</th></tr>"); // Adjust header names as needed

            while (rs.next()) {
                int age = rs.getInt("AGE");
                float length = rs.getFloat("LENGTH"); // Assuming length is stored as a float

                responseHtml.append("<tr>");
                responseHtml.append("<td>").append(age).append("</td>");
                responseHtml.append("<td>").append(length).append("</td>");
                responseHtml.append("</tr>");
            }

            responseHtml.append("</table>");
            responseHtml.append("</body></html>");

            response.getWriter().println(responseHtml.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Database Connection Failed: " + e.getMessage() + "</h1>");
            response.getWriter().println("</body></html>");
        }
    }
}