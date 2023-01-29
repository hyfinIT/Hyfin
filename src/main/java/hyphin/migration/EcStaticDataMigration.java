package hyphin.migration;


import hyphin.model.EcStaticDataDaily;
import hyphin.repository.EcStaticDataDailyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EcStaticDataMigration {

    private final EcStaticDataDailyRepository ecStaticDataDailyRepository;
    private static final String FILE_NAME = "statDaily.xlsx";


    public void start() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Bebrotron\\IdeaProjects\\Hyfin\\src\\main\\resources\\" + FILE_NAME);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(1);
            List<EcStaticDataDaily> list = new ArrayList<>();

            long id = 0L;
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    continue;
                }

                EcStaticDataDaily ecStaticDataDaily = new EcStaticDataDaily();

                Field[] fields = EcStaticDataDaily.class.getDeclaredFields();
                for (int i = 1; i < fields.length - 1; i++) {    //from 1 cause field#0 is ID;
                    log.info("Current obj: {}", ecStaticDataDaily);
                    Field f = fields[i + 1];
                    log.info("\nRow #{}, field#{}, fieldName: {}" , row.getRowNum(), i, f.getName());

                    Class<?> t = f.getType();
                    f.setAccessible(true);

                    if (t.getTypeName().contains("String")) {
                        log.info("Cell value (String): {}", row.getCell(i).getStringCellValue());
                        f.set(ecStaticDataDaily, row.getCell(i).getStringCellValue());
                    }

                    if (t.getTypeName().contains("Integer")) {
                       try {
                           log.info("Cell value (Integer): {}", (int) row.getCell(i).getNumericCellValue());
                           f.set(ecStaticDataDaily, (int) row.getCell(i).getNumericCellValue());
                       } catch (Exception e) {
                           log.info("Cell value (Integer): {}", Integer.parseInt(row.getCell(i).getStringCellValue()));
                           f.set(ecStaticDataDaily,  Integer.parseInt(row.getCell(i).getStringCellValue()));
                       }
                    }

                    if (t.getTypeName().contains("Double")) {
                        try {
                            log.info("Cell value (Double): {}", row.getCell(i).getNumericCellValue());
                            f.set(ecStaticDataDaily, row.getCell(i).getNumericCellValue());
                        } catch (Exception e) {
                            log.info("Cell value (Double): {}", row.getCell(i).getStringCellValue());
                            f.set(ecStaticDataDaily, Double.parseDouble(row.getCell(i).getStringCellValue().replace(",", ".")));
                        }
                    }
                    f.setAccessible(false);
                }
                id++;
                ecStaticDataDaily.setId(id);
                list.add(ecStaticDataDaily);
            }
            file.close();

            list.forEach(ecStaticDataDaily -> ecStaticDataDaily.setStringDate("07.11.2022"));

            log.info("FINAL: list length is: {}", list.size());
            list.forEach(ecStaticDataDailyRepository::save);
            log.info("SAVED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
