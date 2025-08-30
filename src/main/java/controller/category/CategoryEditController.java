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
@WebServlet(urlPatterns = {"/admin/category/edit"})
public class CategoryEditController extends HttpServlet {
    private CategoryService cateService = new CategoryServiceImpl();
    // Path to project source uploads so images can be committed
    private static final Path PROJECT_SRC_UPLOADS = Paths.get("D:", "LT Web", "StudyWeb", "workspace", "vdLoginJSP_forgetPass_logout", "src", "main", "webapp", "uploads", "category");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        Category cate = cateService.get(Integer.parseInt(id));
        req.setAttribute("category", cate);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/category/edit-category.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String iconText = req.getParameter("icon");

        Category existing = cateService.get(id);
        String currentIcon = existing != null ? existing.getIcon() : null;
        String savedPath = null;

        Part filePart = req.getPart("iconFile");
        if (filePart != null && filePart.getSize() > 0) {
            String contentType = filePart.getContentType();
            long size = filePart.getSize();
            if (contentType != null && contentType.startsWith("image/") && size <= 5L * 1024 * 1024) {
                String submitted = filePart.getSubmittedFileName();
                String ext = "";
                if (submitted != null && submitted.contains(".")) {
                    ext = submitted.substring(submitted.lastIndexOf('.'));
                } else if ("image/svg+xml".equals(contentType)) {
                    ext = ".svg";
                }
                String filename = UUID.randomUUID().toString() + ext;
                // Save under deployed webapp/uploads/category
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
                    e.printStackTrace();
                }
                savedPath = "uploads/category/" + filename;
            } else {
                req.setAttribute("error", "Invalid file. Allowed: image types, size <= 5MB.");
                req.setAttribute("category", existing);
                RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/category/edit-category.jsp");
                dispatcher.forward(req, resp);
                return;
            }
        }

        String finalIcon = currentIcon;
        if (savedPath != null) finalIcon = savedPath;
        else if (iconText != null && !iconText.trim().isEmpty()) finalIcon = iconText;

        Category cate = new Category(id, name, finalIcon);
        cateService.edit(cate);

        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}