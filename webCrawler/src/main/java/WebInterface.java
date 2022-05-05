import com.company.Pair;
import com.company.queryProcessor;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebInterface extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("q");

        String message = "you searched for " + name ;
        queryProcessor myq = new queryProcessor(name);
        Pair all= myq.Run();
        List<String> output=(List<String>)all.first;
        HashMap<String, ArrayList<String>>results=(HashMap<String, ArrayList<String>>)all.second;
        StringBuilder myOutput=new StringBuilder();
        if(output!=null)
        {
            int n=output.size();
            for(String i:output)
            {
                myOutput.append("<a href='"+i+"'>"+i+"</a><br>");
                myOutput.append("<h5>"+results.get(i).get(0)+"</h5>");
                myOutput.append("<p>"+results.get(i).get(1)+"</p>");
            }
        }
        else
        {
            myOutput.append("<h1>not found</h1>");
        }
        response.setContentType("text/html");
        String page = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Search</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/results.css\">\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <nav class=\"navigation\">\n" +
                "        <form action=\"request\">\n" +
                "            <div>\n" +
                "                <h1 class=\"Logo\"><i class=\"fa fa-flash\"></i>Flash</h1>\n" +
                "            </div>\n" +
                "            <div class=\"inner-form\">\n" +
                "                <div class=\"input-field first-wrap\">\n" +
                "                    <div class=\"svg-wrapper\">\n" +
                "                        <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\">\n" +
                "                    <path d=\"M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z\"></path>\n" +
                "                  </svg>\n" +
                "                    </div>\n" +
                "                    <input value=\""+name+"\" id=\"search\" name=\"q\" type=\"text\" placeholder=\"What are you looking for?\" />\n" +
                "                </div>\n" +
                "                <div class=\"input-field second-wrap\">\n" +
                "                    <button class=\"btn-search\" onclick=\"window.location.href='results.html';\" type=\"button\">SEARCH</button>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </form>\n" +
                "        <div>\n" +
                "\n" +
                "        </div>\n" +
                "    </nav>\n" +
                "    <div>\n" +
                myOutput +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        response.getWriter().println(page);
    }

}
