package bkav.com.springboot.services.impl;

import bkav.com.springboot.helper.ConnectDB;
import bkav.com.springboot.payload.response.MessageResponse;
import bkav.com.springboot.payload.util.Status;
import bkav.com.springboot.repository.CategoryRepository;
import bkav.com.springboot.services.FileService;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class FileServiceImpl implements FileService {

    @Value("${spring.datasource.username}")
    private String userDB;

    @Value("${spring.datasource.password}")
    private String passDB;

    @Value("${spring.datasource.url}")
    private String urlDB;

    public static String TYPE = "text/csv";

    public static File fontFile = new File("fonts/vuArial.ttf");

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void exportPDF(HttpServletResponse response, int idCategory, String filename) {
        try {
            Document document = new Document(PageSize.A2);
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            BaseFont baseFont = BaseFont.createFont(fontFile.getAbsolutePath(), com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 14, Font.NORMAL);

            // insert data
            Connection conn = ConnectDB.getConnection(urlDB, userDB, passDB);
            CallableStatement callableStatement = conn.prepareCall("{Call category_get_detail_export(?,?,?,?)}");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.setInt(3, idCategory);
            callableStatement.setInt(4, 0);
            callableStatement.execute();
            ResultSet rsData = null;
            rsData = callableStatement.getResultSet();
            ResultSetMetaData meta = rsData.getMetaData();
            int columnCount = meta.getColumnCount();

            Paragraph p = new Paragraph("Export PDF " + filename, font);
            p.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(p);

            PdfPTable table = new PdfPTable(columnCount);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(10);

            PdfPCell cellTitle = new PdfPCell();
            cellTitle.setBackgroundColor(new BaseColor(Color.BLUE));
            cellTitle.setPadding(5);

            PdfPCell cellContent = new PdfPCell();
            cellContent.setPadding(5);

            Font fontColumn = new Font(baseFont, 14, com.itextpdf.text.Font.NORMAL);

            fontColumn.setColor(new BaseColor(Color.WHITE));
            // set column and row
            if (rsData != null) {
                // set column
                for (int i = 1; i <= columnCount; i++) {
                    cellTitle.setPhrase(new Phrase(meta.getColumnName(i).toLowerCase(Locale.ROOT), fontColumn));

                    table.addCell(cellTitle);
                }
                // set row
                while (rsData.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        cellContent.setPhrase(new Phrase(rsData.getString(i), font));

                        table.addCell(cellContent);
                    }
                }
            }
            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ByteArrayInputStream exportCSV(int idCategory) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            Connection conn = ConnectDB.getConnection(urlDB, userDB, passDB);
            CallableStatement callableStatement = conn.prepareCall("{Call category_get_detail_export(?,?,?,?)}");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.setInt(3, idCategory);
            callableStatement.setInt(4, 0);
            callableStatement.execute();
            ResultSet rsData = null;
            rsData = callableStatement.getResultSet();
            if (rsData != null) {
                ResultSetMetaData meta = rsData.getMetaData();
                int columnCount = meta.getColumnCount();
                List<String> dataResult = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    dataResult.add(meta.getColumnName(i).toLowerCase(Locale.ROOT));
                }
                List<List<String>> dataColumn = Arrays.asList(dataResult);
                csvPrinter.printRecords(dataColumn);
                csvPrinter.printRecords(rsData);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException | SQLException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> save(MultipartFile file, int idCategory) {
        if (hasCSVFormat(file)) {
            try {
                if (readCSVFile(file.getInputStream(), idCategory) == 0)
                    return ResponseEntity.ok(new MessageResponse(Status.SUCCESS, "Uploaded the file successfully: " + file.getOriginalFilename(), true, false));
                else
                    return ResponseEntity.ok(new MessageResponse(Status.FAIL, "Uploaded the file fail: " + file.getOriginalFilename(), true, false));
            } catch (Exception e) {
                return ResponseEntity.ok(new MessageResponse(Status.EXCEPTION, "Could not upload the file: " + file.getOriginalFilename() + "!", false, true));
            }
        } else {
            return ResponseEntity.ok(new MessageResponse(Status.FAIL, "Please upload a csv file!", true, false));
        }
    }



    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public int readCSVFile(InputStream is, int idCategory) throws CsvValidationException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); CSVReader reader = new CSVReader(fileReader)) {
            StringBuilder listColumn = new StringBuilder();
            List<List<String>> records = new ArrayList<List<String>>();
            String[] values = null;
            while ((values = reader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
            for (int i = 0; i < 1; i++) {
                for (String column : records.get(i)) {
                    listColumn.append("`");
                    listColumn.append(column);
                    listColumn.append("`,");
                }
            }
            for (int i = 1; i < records.size(); i++) {
                StringBuilder listValue = new StringBuilder();
                for (String column : records.get(i)) {
                    listValue.append("'");
                    listValue.append(column);
                    listValue.append("',");
                }
                String lsColumn = listColumn.toString();
                lsColumn = lsColumn.substring(0, lsColumn.length() - 1);
                String lsValue = listValue.toString();
                lsValue = lsValue.substring(0, lsValue.length() - 1);
                logger.info("lsColumn: " + lsColumn);
                logger.info("lsValue: " + lsValue);
                int code = categoryRepository.insertCategory(idCategory, lsColumn, lsValue);
                logger.info("code: " + code);
                return code;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
        return 999;
    }
}
