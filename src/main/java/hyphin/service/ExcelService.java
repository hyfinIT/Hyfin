package hyphin.service;

import hyphin.model.EcStaticData;
import hyphin.repository.EcStaticDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

    private final EcStaticDataRepository ecStaticDataRepository;
    private static final String FILE_NAME = "stat.xlsx";


//    @javax.annotation.PostConstruct
    public void start() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Bebrotron\\IdeaProjects\\Hyfin\\src\\main\\resources\\" + FILE_NAME);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
//            XSSFSheet sheet = workbook.getSheetAt(1);
            List<EcStaticData> list = new ArrayList<>();

            long id = 0L;
            for (Row row : sheet) {
                //here we need to skip first row if needed

                if (row.getCell(0).getStringCellValue().contains("CLIENT")) {
                    continue;
                }

                id++;
//
//                try {
//                    row.getCell(0).getNumericCellValue();
//                } catch (Exception e) {
//                    continue;
//                }
                EcStaticData ecStaticData = new EcStaticData();
                ecStaticData.setId(id);
                ecStaticData.setClient(row.getCell(0).getStringCellValue());
                ecStaticData.setUserMarginCcy(row.getCell(1).getStringCellValue());
                ecStaticData.setUserMargin((int)row.getCell(2).getNumericCellValue());
                ecStaticData.setBrokerLeverage((int)row.getCell(3).getNumericCellValue());

                ecStaticData.setMrgSizeVariable01(row.getCell(4).getNumericCellValue());
                ecStaticData.setMrgSizeVariable02(row.getCell(5).getNumericCellValue());
                ecStaticData.setMrgSizeVariable03(row.getCell(6).getNumericCellValue());
                ecStaticData.setMrgSizeVariable04(row.getCell(7).getNumericCellValue());
                ecStaticData.setMrgSizeVariable05(row.getCell(8).getNumericCellValue());
                ecStaticData.setMrgSizeVariable06(row.getCell(9).getNumericCellValue());
                ecStaticData.setMrgSizeVariable07(row.getCell(10).getNumericCellValue());
                ecStaticData.setMrgSizeVariable08(row.getCell(11).getNumericCellValue());
//                if (Objects.nonNull(row.getCell(4))) {
//                        ecStaticData.setMeasure(row.getCell(4).getStringCellValue());
//                }
                list.add(ecStaticData);
            }
            file.close();
            log.info("FINAL: list length is: {}", list.size());
            list.forEach(ecStaticDataRepository::save);
            log.info("SAVED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
