/**
 * @Author:VictoryChan
 * @Description：
 * @Date:Creat in0:07 2023/12/14
 * @Modified By:
 */
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 2023.12.14
 * 此脚本未经过测试
 */

public class ExcelSplitter {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // 用户输入Excel文件路径
        System.out.print("请输入Excel文件的完整路径: ");
        String inputFile = scanner.nextLine();

        // 用户输入每个子文件的最大行数
        System.out.print("请输入每个子文件的最大行数: ");
        int maxRowsPerFile;
        try {
            maxRowsPerFile = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("输入的行数无效，请输入一个整数。");
            return;
        }

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(inputFile))) {
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表

            Iterator<Row> rowIterator = sheet.iterator();
            int rowCount = 0;
            int fileCount = 0;

            Workbook newWorkbook = new XSSFWorkbook();
            Sheet newSheet = newWorkbook.createSheet();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                copyRow(row, newSheet.createRow(rowCount % maxRowsPerFile));

                rowCount++;
                if (rowCount % maxRowsPerFile == 0) {
                    try (FileOutputStream outputStream = new FileOutputStream("output_" + (fileCount++) + ".xlsx")) {
                        newWorkbook.write(outputStream);
                    }
                    newWorkbook.close();

                    newWorkbook = new XSSFWorkbook();
                    newSheet = newWorkbook.createSheet();
                }
            }

            if (rowCount % maxRowsPerFile != 0) { // 保存最后一个文件（如果有剩余的行）
                try (FileOutputStream outputStream = new FileOutputStream("output_" + fileCount + ".xlsx")) {
                    newWorkbook.write(outputStream);
                }
                newWorkbook.close();
            }
        }
    }

    private static void copyRow(Row sourceRow, Row destinationRow) {
        for (Cell cell : sourceRow) {
            Cell newCell = destinationRow.createCell(cell.getColumnIndex(), cell.getCellType());
            switch (cell.getCellType()) {
                case STRING:
                    newCell.setCellValue(cell.getRichStringCellValue().getString());
                    break;
                case NUMERIC:
                    newCell.setCellValue(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(cell.getCellFormula());
                    break;
                default:
                    break;
            }
        }
    }
}
