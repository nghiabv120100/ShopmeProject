package com.shopme.admin.user;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> listUser, HttpServletResponse resp) throws IOException {
        super.setResponseHeader(resp, "text/csv", ".csv");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(resp.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(header);
        for(User user : listUser) {
            csvWriter.write(user, fieldMapping);
        }
        csvWriter.close();
    }
}
