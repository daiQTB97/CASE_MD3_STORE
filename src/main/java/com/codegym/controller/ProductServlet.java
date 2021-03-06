package com.codegym.controller;

import com.codegym.dao.CategoryService;
import com.codegym.dao.CategoryServiceImpl;
import com.codegym.dao.ProductService;
import com.codegym.dao.ProductServiceImpl;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.utils.UploadFile;
import com.codegym.utils.Validate;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MultipartConfig
@WebServlet(name = "ProductServlet",urlPatterns = "/product")
public class ProductServlet extends HttpServlet {
    CategoryService categoryService;
    ProductService productService;
    ProductServiceImpl service;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryServiceImpl();
        productService = new ProductServiceImpl();
        service = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create" :
                ShowCreateUser(req,resp);
                break;
            case "edit" :
                ShowEditUser(req,resp);
                break;
            case "block":
                blockUser(req, resp);
                break;
            case "delete":
                try {
                    deleteUser(req,resp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                listUser(req,resp);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create" :
                createUser(req,resp);
                break;
            case "edit" :
                editUser(req,resp);
                break;
            default:
                listUser(req,resp);
                break;
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productService.deleteUser(id);

        List<Product> productList = productService.findAll();
        req.setAttribute("productList", productList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("product/list.jsp");
        dispatcher.forward(req,resp);
    }

    private void editUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("product/edit.jsp");
        String strIdProduct = req.getParameter("id_product");
        String title = req.getParameter("title");
        String strPrice = req.getParameter("price");
        String strQuantity = req.getParameter("quantity");
        String urlFile = UploadFile.uploadImagesServer(req);
        String strCategoryId = req.getParameter("category_id");
        List<Category> categoryList = categoryService.findAll();
        req.setAttribute("categoryList",categoryList);
        List<String> errors = new ArrayList<>();
        if(title.equals("")) {
            errors.add("Kh??ng ???????c ????? Tr???ng Ti??u ????? S???n Ph???m");
        }
        if(strPrice.equals("")) {
            errors.add("Kh??ng ???????c ????? Tr???ng Gi?? S???n Ph???m");
        }
        if (strQuantity.equals("")) {
            errors.add("Kh??ng ???????c ????? Tr???ng S??? L?????ng S???n Ph???m");
        }
        if(strCategoryId.equals("")) {
            errors.add("Vui L??ng Ch???n Danh M???c S???n Ph???m");
        }

        boolean priceIsNumber = Validate.isNumberValid(strPrice);
        try {
            if (!priceIsNumber || strPrice.length() > 12 || Integer.parseInt(strPrice) < 0) {
                errors.add("Gi?? C???a S???n Ph???m Kh??ng H???p L???");
            }
        } catch (NumberFormatException ex) {
            errors.add("Gi?? C???a S???n Ph???m V?????t Qu?? Gi???i H???n Cho Ph??p");
        }

        boolean quantityIsNumber = Validate.isNumberValid(strQuantity);
        try {
            if (!quantityIsNumber || Integer.parseInt(strQuantity) < 0 || Integer.parseInt(strQuantity) >999) {
                errors.add("S??? L?????ng S???n Ph???m Kh??ng H???p L???");
            }
        } catch (NumberFormatException ex) {
            errors.add("S??? L?????ng C???a S???n Ph???m V?????t Qu?? Gi???i H???n Cho Ph??p");
        }

        boolean idCategoryIsNumber = Validate.isNumberValid(strCategoryId);
        if (!idCategoryIsNumber) {
            errors.add("Danh M???c S???n Ph???m Kh??ng H???p L???");
        }

        boolean strIdProductIsNumber  = Validate.isNumberValid(strIdProduct);
        if (!strIdProductIsNumber) {
            errors.add("Id S???n Ph???m Kh??ng H???p L???");
        }

        if (errors.size() == 0) {
            boolean exists = productService.existsById(Integer.parseInt(strIdProduct));
            boolean success = false;
            String message = "";
            if (exists) {
                boolean existsCategory = categoryService.existById(Integer.parseInt(strCategoryId));
                if (existsCategory){
                    Product product = new Product(Integer.parseInt(strIdProduct),title,urlFile,BigDecimal.valueOf(Integer.parseInt(strPrice)),Integer.parseInt(strQuantity),Integer.parseInt(strCategoryId));
                    Map<String, String> result = productService.update(product);
                    success = Boolean.parseBoolean(result.get("success"));
                    message = result.get("message");
                    if (!success){
                        errors.add(message);
                    }
                }else {
                    errors.add("Danh M???c S???n Ph???m Kh??ng T???n T???i");
                }
            } else {
                errors.add("S???n Ph???m Kh??ng T???n T???i");
            }
            if (success) {
                req.setAttribute("success", true);
                req.setAttribute("message",message);
            }
            else {
                req.setAttribute("error", true);
                errors.add("S???a Th??ng Tin S???n Ph???m Th???t B???i");
            }
        }
        if (errors.size() > 0){
            req.setAttribute("errors",errors);
            List<Product> productFind = productService.findProductId(Integer.parseInt(strIdProduct));
            req.setAttribute("productFind",productFind);
        }
        dispatcher.forward(req,resp);
    }



    private void createUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("product/create.jsp");
        String title = req.getParameter("title");
        String strPrice = req.getParameter("price");
        String strQuantity = req.getParameter("quantity");
        String strCategory_id = req.getParameter("category_id");
        List<Category> categoryList = categoryService.findAll();
        req.setAttribute("categoryList",categoryList);
        List<String> errors = new ArrayList<>();
        if(title.equals("")) {
            errors.add("Kh??ng ???????c ????? Tr???ng Ti??u ????? S???n Ph???m");
        }
        if(strPrice.equals("")) {
            errors.add("Kh??ng ???????c ????? Tr???ng Gi?? S???n Ph???m");
        }
        if (strQuantity.equals("")) {
            errors.add("Kh??ng ???????c ????? Tr???ng S??? L?????ng S???n Ph???m");
        }
        if(strCategory_id.equals("")) {
            errors.add("Vui L??ng Ch???n Danh M???c S???n Ph???m");
        }

        boolean priceIsNumber = Validate.isNumberValid(strPrice);
        try {
            if (!priceIsNumber || strPrice.length() > 12 || Integer.parseInt(strPrice) < 0) {
                errors.add("Gi?? C???a S???n Ph???m Kh??ng H???p L???");
            }
        } catch (NumberFormatException ex) {
            errors.add("Gi?? C???a S???n Ph???m V?????t Qu?? Gi???i H???n Cho Ph??p");
        }

        boolean quantityIsNumber = Validate.isNumberValid(strQuantity);
        if (!quantityIsNumber || Integer.parseInt(strQuantity) < 0) {
            errors.add("S??? L?????ng S???n Ph???m Kh??ng H???p L???");
        }

        boolean idCategoryIsNumber = Validate.isNumberValid(strCategory_id);
        if (!idCategoryIsNumber) {
            errors.add("Danh M???c S???n Ph???m Kh??ng H???p L???");
        }

        boolean checkFile = UploadFile.checkFile(req);
        if (checkFile){
            errors.add("Vui L??ng Ch???n File");
        }else {
            boolean checkNoFile = UploadFile.checkNoFile(req);
            if (!checkNoFile){
                errors.add("File Kh??ng H???p L???");
            }
        }

        if (errors.size() == 0) {
            boolean exist = categoryService.existById(Integer.parseInt(strCategory_id));
            boolean success = false;
            if (exist){
                String urlImage = UploadFile.uploadImagesServer(req);
                Product product = new Product(title,urlImage, BigDecimal.valueOf(Long.parseLong(strPrice)),Integer.parseInt(strQuantity),Integer.parseInt(strCategory_id));
                Map<String, String> result = productService.doCreate(product);
                success = Boolean.parseBoolean(result.get("success"));
                String message = result.get("message");
                if (!success){
                    errors.add(message);
                }
            }else {
                errors.add("Danh M???c S???n Ph???m Kh??ng t???n t???i");
            }
            if (success) {
                req.setAttribute("success", true);
            }
            else {
                req.setAttribute("error", true);
                errors.add("Th??m S???n Ph???m Th???t B???i");
            }
        }
        if (errors.size() > 0){
            req.setAttribute("errors",errors);
        }
        dispatcher.forward(req,resp);
    }


    private void blockUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = -1;
        if(req.getParameter("id")!=null){
            id = Integer.parseInt(req.getParameter("id"));
        }
        List<Product> productList = service.findStatusProductId(id);
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = resp.getWriter();
        if (productList.isEmpty()){
            printWriter.println("fail");
        }else {
            int status = productList.get(0).getStopSelling();
            if (status == 1){
                status = 0;
            }else {
                status = 1;
            }
            boolean update = service.updateStatus(id,status);
            if (update){
                List<Product> productList1 = service.findStatusProductId(id);
                Gson gson = new Gson();
                String jsonInString = gson.toJson(productList1.get(0));
                printWriter.println(jsonInString);
            }
        }

        printWriter.close();

    }

    private void ShowEditUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("product/edit.jsp");
        String strProductId = req.getParameter("id");
        List<Category> categoryList = categoryService.findAll();
        req.setAttribute("categoryList",categoryList);
        boolean productIdIsNumber = Validate.isNumberValid(strProductId);
        List<String> errors = new ArrayList<>();
        if (!productIdIsNumber){
            errors.add("Id S???n Ph???m Kh??ng H???p L???");
        }else {
            boolean exists = productService.existsById(Integer.parseInt(strProductId));
            if (!exists) {
                errors.add("S???n Ph???m Kh??ng T???n T???i");
            }else {
                List<Product> productFind = productService.findProductId(Integer.parseInt(strProductId));
                if (productFind.size() == 0) {
                    errors.add("S???n Ph???m ??ang D???ng B??n, Vui L??ng Thay ?????i Tr???ng Th??i C???a S???n Ph???m ????? S???a Th??ng Tin");
                }else {
                    req.setAttribute("productFind",productFind);
                }
            }
        }
        if (errors.size() > 0) {
            req.setAttribute("errors",errors);
        }
        dispatcher.forward(req,resp);
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("product/list.jsp");
        int page = 1;
        int recordsPerPage = 4;
        String strPage = req.getParameter("page");
        List<String> error = new ArrayList<>();
        String keySearch = req.getParameter("key_search");
        if(keySearch != null) {
            List<Product> productList = service.searchByKey(keySearch);
            req.setAttribute("productList",productList);
            req.setAttribute("message","K???t Qu??? T??m Ki???m");
            dispatcher.forward(req,resp);
        }else {
            if( strPage != null){
                boolean pageIsNumber = Validate.isNumberValid(req.getParameter("page"));
                if (!pageIsNumber) {
                    error.add("Page Kh??ng H???p L???");
                }else {
                    page = Integer.parseInt(req.getParameter("page"));
                }
            }
            if (error.size() == 0) {
                List<Category> categoryList = categoryService.findAll();
                List<Product> productList  = service.findAll((page-1)*recordsPerPage,recordsPerPage);
                int noOfRecords = service.getNoOfRecords();
                int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
                System.out.println("noOfRecords " + noOfRecords);
                System.out.println("noOfPages " + noOfPages);
                req.setAttribute("categoryList",categoryList);
                req.setAttribute("productList",productList);
                req.setAttribute("noOfPages", noOfPages);
                req.setAttribute("currentPage", page);
                if (productList.isEmpty()){
                    error.add("Kh??ng C?? D??? Li???u Ph?? H???p V???i Page");
                }
            }
            if (error.size() > 0){
                req.setAttribute("error",error);
            }
            dispatcher.forward(req,resp);
        }
    }

    private void ShowCreateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("product/create.jsp");
        List<Category> categoryList = categoryService.findAll();
        req.setAttribute("categoryList",categoryList);
        dispatcher.forward(req,resp);
    }
}