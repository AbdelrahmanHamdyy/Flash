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
        String[] myOutput;
        StringBuilder Results = new StringBuilder();
        int n = 0;
        int NumberOfPages = 0;
        StringBuilder buttons = new StringBuilder();
        if(all!=null)
        {
            List<String> output=(List<String>)all.first;
            HashMap<String, ArrayList<String>>results=(HashMap<String, ArrayList<String>>)all.second;
            n=output.size();
            NumberOfPages = (int) Math.ceil((float) n / 10);
            myOutput = new String[NumberOfPages];
            int index = 0;
            int i = 0;
            while (i < n)
            {
                if (index != 0)
                    myOutput[index] = "<div hidden id=\"shown" + index + "\">";
                else
                    myOutput[index] = "<div id=\"shown" + index + "\">";
                int loopCount = 10;
                if (index == NumberOfPages - 1) {
                    loopCount = n % 10;
                    if (loopCount == 0)
                        loopCount = 10;
                }
                for (int j = 0; j < loopCount; j++) {
                    myOutput[index] += "<div class='search-item'> <h4 class='mb-1 Link'><a href='" + output.get(i) + "'> " + results.get(output.get(i)).get(0) +
                            "</a></h4> <p style=\"color: #00ddb1; margin-bottom: 0;\">" + output.get(i)
                            + "</p> <p class='mb-0 text-muted' style=\"margin-top: 0;\">" + results.get(output.get(i)).get(1) + "</p> </div>";
                    i++;
                }
                myOutput[index] += "</div>";
                index++;
            }
            for (int j = 0; j < NumberOfPages; j++) {
                Results.append(myOutput[j]);
                buttons.append("<li class=\"page-item selected\"><button onclick=\"viewPage("+j+")\" class=\"page-link\">" + (j + 1) + "</button></li>");
            }
        }
        else
        {
            Results.append("<h1 style=\"text-align: center; color: lightgrey;\">NOT FOUND!</h1>");
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
                "   <link href=\"https://fonts.googleapis.com/css?family=Poppins\" rel=\"stylesheet\" />" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.9.0/css/all.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">" +
                "\t<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.bundle.min.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>\n"+
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
                "                                       <h1 class=\"Logo\"><a href=\"http://localhost:8080/\"><i class=\"fa fa-flash\"></i>Flash</a></h1>" +
                "                                       <input value='" +name+"'\" id=\"search\" class=\"form-control\" name=\"q\" type=\"text\" placeholder=\"What are you looking for?\" />\n" +
                "                                    <div class=\"input-group-append\">\n" +
                "                                        <button onclick=\"return empty()\" type=\"submit\" class=\"btn waves-effect waves-light btn-custom\"><i class=\"fa fa-search mr-1\"></i> Search</button>\n" +
                "                                    </div>\n" +
                "                                </div>\n" +
                "                                <div class=\"mt-4 text-center\">\n" +
                "                                    <h4>Search Results For " + name + "</h4></div>\n" +
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
                                                        Results +
                "                                    <ul class=\"pagination justify-content-end pagination-split mt-0\">\n" +
                                                        buttons +
                "                                        <!--<li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">»</span> <span class=\"sr-only\">Next</span></a></li>-->\n" +
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
                "    background-color:#12172e;\n" +
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
                ".badge-success {\n" +
                "    color: #fff;\n" +
                "    background-color: rgb(50, 41, 111);\n" +
                "}\n" +
                ".search-result-box .search-item {\n" +
                "    padding-bottom: 20px;\n" +
                "    border-bottom: 1px solid #e3eaef;\n" +
                "    margin-bottom: 20px\n" +
                "}\n" +
                "h1{\n" +
                "    text-align:center;\n" +
                "    background:linear-gradient(to right,#84c2f7,#6ebcff,#50adff,#2197ff,#0058af);\n" +
                "    background-size:400%;\n" +
                "    font-family: poppins;\n" +
                "    font-weight: bold;\n" +
                "    -webkit-text-fill-color:transparent;\n" +
                "    -webkit-background-clip: text;\n" +
                "    animation: animate 20s ease infinite;\n" +
                "    animation-direction: reverse;\n" +
                "    font-size: 27px;\n" +
                "    margin-right: 10px;\n" +
                "    margin-top: 5px;\n" +
                "  }\n" +
                "\n" +
                ".input-group-append {\n" +
                "    height: 38px;\n" +
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
                "    transform: scale(1.015);\n" +
                "}\n" +
                "a {\n" +
                "    color: #8ab4f8;\n" +
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
                "function viewPage(page) {\n" +
                "   window.location.href='#';\n" +
                "   for (let i = 0; i < " + NumberOfPages + "; i++) {\n" +
                "       let name = \"shown\" + i.toString();\n" +
                "       let current = document.getElementById(name);\n" +
                "       if (i != page)\n" +
                "           current.style.display = \"none\";\n" +
                "       else {\n" +
                "           current.removeAttribute(\"hidden\");\n" +
                "           current.style.display = \"block\";\n" +
                "       }\n" +
                "   }\n" +
                "}\n" +
                "function empty() {\n" +
                "      var x;\n" +
                "      x = document.getElementById(\"search\").value;\n" +
                "      if (x == \"\") {\n" +
                "        swal ( \"Search\" ,  \"Input Field is Empty!\" ,  \"error\" )\n" +
                "          return false;\n" +
                "      };\n" +
                "      return true;\n" +
                "}\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        response.getWriter().println(page);
    }

}