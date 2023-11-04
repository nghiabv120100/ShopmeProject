package com.shopme.admin.user;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        this.workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        this.sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);
        for (int i = 0; i < header.length; i ++) {
            createCell(row, i, header[i], cellStyle);
        }
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if (value instanceof  Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof  Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }

    private void writeDataLines(List<User> listUsers) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        int rowIndex = 1;
        for (User user : listUsers) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getFirstName(), cellStyle);
            createCell(row, columnIndex++, user.getLastName(), cellStyle);
            createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, columnIndex, user.isEnabled(), cellStyle);
        }
    }

    public void export(List<User> listUser, HttpServletResponse resp) throws IOException {
        super.setResponseHeader(resp, "application/octet-stream", ".xlsx");
        writeHeaderLine();
        writeDataLines(listUser);
        ServletOutputStream outputStream = resp.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
