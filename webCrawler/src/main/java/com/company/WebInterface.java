package com.company;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

public class WebInterface extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("q");
        //String gender = request.getParameter("Gender");

        String message = "you searched for " + name ;

        response.setContentType("text/html");

        String page = "<!doctype html> <html> <body> <h1>" + message +" </h1><h2>kkkkkkkk top<h2> </body></html>";
        response.getWriter().println(page);
    }

}
