import irpackage.IREngine;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.File;

public class IRServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        String lemmatizedDirectory = getServletConfig().getServletContext().getRealPath("LemmatizedWords") + File.separator;
        String stopwordsPath = getServletConfig().getServletContext().getRealPath("Stopwords") + File.separator + "stopwords-en.txt";
        IREngine irEngine = new IREngine(lemmatizedDirectory, stopwordsPath);
        irEngine.start();
        getServletContext().setAttribute("engine", irEngine);
        System.out.println("ENGINE STARTED...");
    }
}
