package hyphin.service;

import hyphin.model.CcyPair;
import hyphin.repository.CcyPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

    private final CcyPairRepository ccyPairRepository;

//    @PostConstruct
    public void start() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Bebrotron\\IdeaProjects\\Hyfin\\src\\main\\resources\\file.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(1);
            List<CcyPair> list = new ArrayList<>();
            for (Row row : sheet) {
                try {
                    row.getCell(0).getNumericCellValue();
                } catch (Exception e) {
                    continue;
                }
                CcyPair ccyPair = new CcyPair();
                ccyPair.setId((long) row.getCell(0).getNumericCellValue());
                ccyPair.setRegion(row.getCell(1).getStringCellValue());
                ccyPair.setCcyPairNumber(row.getCell(2).getStringCellValue());
                ccyPair.setPreferenceType(row.getCell(3).getStringCellValue());
                ccyPair.setFrequencyTier((long) row.getCell(4).getNumericCellValue());
                if (Objects.nonNull(row.getCell(5))) {
                    ccyPair.setDisplayPriority(row.getCell(5).getStringCellValue());
                }
                ccyPair.setCcyPairNumber(row.getCell(6).getStringCellValue());
                ccyPair.setSpread((row.getCell(7).getNumericCellValue()));
                list.add(ccyPair);
            }
            file.close();
            log.info("FINAL: list length is: {}", list.size());
            list.forEach(ccyPairRepository::save);
            log.info("SAVED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
