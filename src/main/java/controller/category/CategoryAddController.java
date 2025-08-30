package controller.category;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Category;
import service.CategoryService;
import service.CategoryServiceImpl;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
                 maxFileSize = 5L * 1024 * 1024, // 5MB
                 maxRequestSize = 10L * 1024 * 1024)
@WebServlet(urlPatterns = {"/admin/category/add"})
public class CategoryAddController extends HttpServlet {
    private CategoryService cateService = new CategoryServiceImpl();
    // Path to project source uploads (so images can be committed)
    private static final Path PROJECT_SRC_UPLOADS = Paths.get("D:", "LT Web", "StudyWeb", "workspace", "vdLoginJSP_forgetPass_logout", "src", "main", "webapp", "uploads", "category");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/category/add-category.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String iconText = req.getParameter("icon"); // text fallback
        String savedPath = null;

        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("error", "Name is required.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/category/add-category.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        Part filePart = req.getPart("iconFile");
        if (filePart != null && filePart.getSize() > 0) {
            String contentType = filePart.getContentType();
            long size = filePart.getSize();
            // Validate content type and size (<= 5MB allowed, recommend 1MB)
            if (contentType != null && contentType.startsWith("image/") && size <= 5L * 1024 * 1024) {
                String submitted = filePart.getSubmittedFileName();
                String ext = "";
                if (submitted != null && submitted.contains(".")) {
                    ext = submitted.substring(submitted.lastIndexOf('.'));
                } else if ("image/svg+xml".equals(contentType)) {
                    ext = ".svg";
                }
                String filename = UUID.randomUUID().toString() + ext;
                // Save under webapp/uploads/category (inside deployed app)
                String uploadDir = req.getServletContext().getRealPath("/uploads/category");
                Path categoryDir = Paths.get(uploadDir);
                if (!Files.exists(categoryDir)) {
                    Files.createDirectories(categoryDir);
                }
                Path filePath = categoryDir.resolve(filename);
                try (InputStream is = filePart.getInputStream()) {
                    Files.copy(is, filePath);
                }
                // Also copy into project src so it can be committed
                try {
                    if (!Files.exists(PROJECT_SRC_UPLOADS)) {
                        Files.createDirectories(PROJECT_SRC_UPLOADS);
                    }
                    Path projectFile = PROJECT_SRC_UPLOADS.resolve(filename);
                    Files.copy(filePath, projectFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    // ignore copy-to-src errors but log
                    e.printStackTrace();
                }
                // Use relative path to be served
                savedPath = "uploads/category/" + filename;
            } else {
                req.setAttribute("error", "Invalid file. Allowed: image types, size <= 5MB.");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/category/add-category.jsp");
                dispatcher.forward(req, resp);
                return;
            }
        }

        Category cate = new Category();
        cate.setName(name);
        if (savedPath != null) {
            cate.setIcon(savedPath);
        } else {
            cate.setIcon(iconText);
        }

        cateService.insert(cate);
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}