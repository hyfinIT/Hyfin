package hyphin.service;

import hyphin.model.Atmospherics;
import hyphin.model.CcyPair;
import hyphin.model.FlagIcon;
import hyphin.model.InflationRate;
import hyphin.repository.AtmosphericsRepository;
import hyphin.repository.CcyPairRepository;
import hyphin.repository.FlagIconRepository;
import hyphin.repository.InflationRateRepository;
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

    private final FlagIconRepository flagIconRepository;

//    @PostConstruct
    public void start() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Bebrotron\\IdeaProjects\\Hyfin\\src\\main\\resources\\flag.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
//            XSSFSheet sheet = workbook.getSheetAt(1);
            List<FlagIcon> list = new ArrayList<>();

            long id = 0L;
            for (Row row : sheet) {
                //here we need to skip first row if needed

                if (row.getCell(0).getStringCellValue().contains("CCY_MNEMONIC")) {
                    continue;
                }

                id++;
//
//                try {
//                    row.getCell(0).getNumericCellValue();
//                } catch (Exception e) {
//                    continue;
//                }
                FlagIcon flagIcon = new FlagIcon();
                flagIcon.setId(id);
                flagIcon.setCcyMnemonic(row.getCell(0).getStringCellValue());
                flagIcon.setCountry(row.getCell(1).getStringCellValue());
                flagIcon.setFlagFile(row.getCell(2).getStringCellValue());
//                flagIcon.setSubject(row.getCell(3).getStringCellValue());
//                if (Objects.nonNull(row.getCell(4))) {
//                        flagIcon.setMeasure(row.getCell(4).getStringCellValue());
//                }
//                flagIcon.setFrequency(row.getCell(5).getStringCellValue());
//                flagIcon.setTime((row.getCell(6).getStringCellValue()));
//                flagIcon.setInfRate(row.getCell(7).getNumericCellValue());
//                flagIcon.setInfTrend(row.getCell(8).getStringCellValue());
                list.add(flagIcon);
            }
            file.close();
            log.info("FINAL: list length is: {}", list.size());
            list.forEach(flagIconRepository::save);
            log.info("SAVED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
