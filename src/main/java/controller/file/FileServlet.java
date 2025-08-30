package controller.file;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/uploads/*"})
public class FileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // e.g. /category/uuid.jpg
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.contains("..")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Serve only from webapp's uploads folder (no external fallback)
        String webappUploadsReal = getServletContext().getRealPath("/uploads");
        if (webappUploadsReal == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path webappBase = Paths.get(webappUploadsReal);
        Path file = webappBase.resolve(pathInfo.substring(1)).normalize();
        if (!file.startsWith(webappBase) || !Files.exists(file) || Files.isDirectory(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(file.toString());
        if (mime == null) {
            mime = Files.probeContentType(file);
        }
        if (mime == null) mime = "application/octet-stream";
        resp.setContentType(mime);
        resp.setContentLengthLong(Files.size(file));
        try (OutputStream os = resp.getOutputStream()) {
            Files.copy(file, os);
        }
    }
}