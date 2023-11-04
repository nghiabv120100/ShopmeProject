package com.shopme.admin.user;

import jakarta.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {
    public String[] header = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
    public String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
    public void setResponseHeader(HttpServletResponse resp, String contentType, String extension) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = "users_" + timestamp + extension;

        resp.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerVal = "attachment; filename=" + fileName;
        resp.setHeader(headerKey, headerVal);
    }
}
