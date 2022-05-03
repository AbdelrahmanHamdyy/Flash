import com.company.Ranker;
import com.company.queryProcessor;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Map;

public class WebInterface extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("q");
        //String gender = request.getParameter("Gender");

        String message = "you searched for " + name ;
        queryProcessor myq = new queryProcessor(name);
        Ranker.sortedMap.clear();
        Ranker.rank(myq.list, 2);
        Ranker.rank(myq.stemmed, 1);
        Ranker.output.clear();
        for (Map.Entry<String, Double> entry : Ranker.sortedMap.entrySet())
            Ranker.output.add(entry.getKey());
        response.setContentType("text/html");
        StringBuilder myOutput=new StringBuilder();
        if(myOutput.isEmpty())
        {
            int n=Ranker.output.size();
            for(String i:Ranker.output)
            {
                myOutput.append("<a href='"+i+"'>"+i+"</a><br>");
            }
        }
        String page = "<!doctype html> <html> <body> <h1>" + message +" </h1><h2>kkkkkkkk top<h2>"+myOutput+"***************************** </body></html>";
        response.getWriter().println(page);
    }

}
