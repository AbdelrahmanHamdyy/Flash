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
        StringBuilder myOutput=new StringBuilder();
        int n = 0;
        if(all!=null)
        {
            List<String> output=(List<String>)all.first;
            HashMap<String, ArrayList<String>>results=(HashMap<String, ArrayList<String>>)all.second;
            n=output.size();
            for(String i:output)
            {
                myOutput.append("<div class='search-item'>");
                myOutput.append("<h4 class='mb-1 Link'><a href='"+i+"'>"+results.get(i).get(0)+"</a></h4>");
                myOutput.append("<a class='font-13 text-success mb-3'>"+i+"</a><br>");
                myOutput.append("<p class='mb-0 text-muted'>"+results.get(i).get(1)+"</p>");
                myOutput.append("</div>");
            }
        }
        else
        {
            myOutput.append("<h1 style=\"text-align: center; color: lightgrey;\">NOT FOUND!</h1>");
            //myOutput.append("<h1>"+name+"</h1>");
        }
        response.setContentType("text/html");
        String page = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <!--  This file has been downloaded from bootdey.com @bootdey on twitter -->\n" +
                "    <!--  All snippets are MIT license http://bootdey.com/license -->\n" +
                "    <title>" + name + " - Flash Search</title>\n" +
                "    <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/Icon.png\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\t<script src=\"https://code.jquery.com/jquery-1.10.2.min.js\"></script>\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "\t<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.bundle.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"content\">\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"row\">\n" +
                "            <div class=\"col-lg-12\">\n" +
                "                <div class=\"search-result-box card-box\">\n" +
                "                    <form action=\"request\" method=\"GET\">\n" +
                "                    <div class=\"row\">\n" +
                "                        <div class=\"col-md-8 offset-md-2\">\n" +
                "                            <div class=\"pt-3 pb-4\">\n" +
                "                                <div class=\"input-group\">\n" +
                "                                       <input value='" +name+"'\" id=\"search\" class=\"form-control\" name=\"q\" type=\"text\" placeholder=\"What are you looking for?\" />\n" +
                "                                    <div class=\"input-group-append\">\n" +
                "                                        <button type=\"submit\" class=\"btn waves-effect waves-light btn-custom\"><i class=\"fa fa-search mr-1\"></i> Search</button>\n" +
                "                                    </div>\n" +
                "                                </div>\n" +
                "                                <div class=\"mt-4 text-center\">\n" +
                "                                    <h4>Search Results For \"" + name + "\"</h4></div>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                    </form>\n" +
                "                    <!-- end row -->\n" +
                "                    <ul class=\"nav nav-tabs tabs-bordered\">\n" +
                "                        <li class=\"nav-item\"><a href=\"#home\" data-toggle=\"tab\" aria-expanded=\"true\" class=\"nav-link active\">All results <span class=\"badge badge-success ml-1\">" + n + "</span></a></li>\n" +
                "                    </ul>\n" +
                "                    <div class=\"tab-content\">\n" +
                "                        <div class=\"tab-pane active\" id=\"home\">\n" +
                "                            <div class=\"row\">\n" +
                "                                <div class=\"col-md-12\">\n" +
                "                                    <script>\n" +
                "                                        let size = " + n + ";\n" +
                "                                        let NumberOfPages = size / 10;\n" +
                "                                        let currentPage = document.querySelector('#selected');\n" +
                "                                        let loopCount = 10;\n" +
                "                                        if (currentPage == NumberOfPages) {\n" +
                "                                            loopCount = size % 10;\n" +
                "                                            if (loopCount == 0)\n" +
                "                                               loopCount = 10;\n" +
                "                                        }\n" +
                "                                    </script> \n" + myOutput +
                "                                    <ul class=\"pagination justify-content-end pagination-split mt-0\">\n" +
                "                                        <li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">«</span> <span class=\"sr-only\">Previous</span></a></li>\n" +
                "                                        <li class=\"page-item selected\"><a class=\"page-link\" href=\"#\">1</a></li>\n" +
                "                                        <script>\n" +
                "                                            for(let i = 2; i <= NumberOfPages; i++) {\n" +
                "                                                <li class=\"page-item selected\"><a class=\"page-link\" href=\"#\">i</a></li>\n" +
                "                                            }\n" +
                "                                        </script>\n" +
                "                                        <li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">»</span> <span class=\"sr-only\">Next</span></a></li>\n" +
                "                                    </ul>" +
                "                                    <div class=\"clearfix\"></div>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <!-- end All results tab -->\n" +
                "                        <!-- Users tab -->\n" +
                "                        <div class=\"tab-pane\" id=\"users\">\n" +
                "\n" +
                "\n" +
                "                            \n" +
                "                            \n" +
                "                           \n" +
                "                            <ul class=\"pagination justify-content-end pagination-split mt-0\">\n" +
                "                                <li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">«</span> <span class=\"sr-only\">Previous</span></a></li>\n" +
                "                                <li class=\"page-item\"><a class=\"page-link\" href=\"#\">1</a></li>\n" +
                "                                <li class=\"page-item active\"><a class=\"page-link\" href=\"#\">2</a></li>\n" +
                "                                <li class=\"page-item\"><a class=\"page-link\" href=\"#\">3</a></li>\n" +
                "                                <li class=\"page-item\"><a class=\"page-link\" href=\"#\">4</a></li>\n" +
                "                                <li class=\"page-item\"><a class=\"page-link\" href=\"#\">5</a></li>\n" +
                "                                <li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">»</span> <span class=\"sr-only\">Next</span></a></li>\n" +
                "                            </ul>\n" +
                "                            <div class=\"clearfix\"></div>\n" +
                "                        </div>\n" +
                "                        <!-- end Users tab -->\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <!-- end row -->\n" +
                "    </div>\n" +
                "    <!-- container -->\n" +
                "</div>\n" +
                "\n" +
                "<style type=\"text/css\">\n" +
                "body{\n" +
                "    margin-top:20px;\n" +
                "    background-color:rgba(18, 23, 46);\n" +
                "}\n" +
                ".text-center{\n" +
                "    color:white\n" +
                "}\n" +
                ".card-box {\n" +
                "    padding: 20px;\n" +
                "    border-radius: 3px;\n" +
                "    margin-bottom: 30px;\n" +
                "    background-color: #211439;\n" +
                "}\n" +
                ".text-muted{\n" +
                "    color: lightgrey !important;\n" +
                "}\n" +
                ".search-result-box .tab-content {\n" +
                "    padding: 30px 30px 10px 30px;\n" +
                "    -webkit-box-shadow: none;\n" +
                "    box-shadow: none;\n" +
                "    -moz-box-shadow: none\n" +
                "}\n" +
                ".search-result-box .search-item {\n" +
                "    padding-bottom: 20px;\n" +
                "    border-bottom: 1px solid #e3eaef;\n" +
                "    margin-bottom: 20px\n" +
                "}\n" +
                ".text-success {\n" +
                "    color: #00ddb1!important;\n" +
                "    transition: 0.5s;\n" +
                "}\n" +
                ".mb-1{\n" +
                "    transition: 0.5s;\n" +
                "    color: #8ab4f8;\n" +
                "}\n" +
                ".mb-1:hover{\n" +
                "    transform: scale(1.02);\n" +
                "}\n" +
                "a {\n" +
                "    color: #4995ff;\n" +
                "    text-decoration: none;\n" +
                "    background-color: transparent;\n" +
                "    transition: 0.5s;\n" +
                "}\n" +
                ".btn-custom {\n" +
                "    background-color: #00366b;\n" +
                "    color: white;\n" +
                "}\n" +
                ".btn-custom, .btn-danger, .btn-info, .btn-inverse, .btn-pink, .btn-primary, .btn-purple, .btn-success, .btn-warning {\n" +
                "    color: #fff!important;\n" +
                "}\n" +
                "</style>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        response.getWriter().println(page);
    }

}