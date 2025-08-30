package service;

import java.util.List;
import dao.CategoryDao;
import dao.CategoryDaoImpl;
import model.Category;
import service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public void insert(Category category) { categoryDao.insert(category); }

    @Override
    public void edit(Category category) { categoryDao.edit(category); }

    @Override
    public void delete(int id) { categoryDao.delete(id); }

    @Override
    public Category get(int id) { return categoryDao.get(id); }

    @Override
    public Category get(String name) { return categoryDao.get(name); }

    @Override
    public List<Category> getAll() { return categoryDao.getAll(); }

    @Override
    public List<Category> search(String keyword) { return categoryDao.search(keyword); }
}
