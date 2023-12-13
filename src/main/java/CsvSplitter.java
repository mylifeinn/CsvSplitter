import java.io.*;
import java.util.Scanner;

/**
 * 2023.12.14
 * 此脚本经过测试，用于分割CSV文件，以供GPT学习
 * 360000行GeoLite2-City-CSV数据大概25MB
 */

public class CsvSplitter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 用户输入CSV文件路径
        System.out.print("请输入CSV文件的完整路径: ");
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

        // 用户输入要保存文件的目录
        System.out.print("请输入要保存文件的目录路径: ");
        String outputDirectory = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int rowCount = 0;
            int fileCount = 0;
            String line;
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + "/output_" + fileCount + ".csv"));

            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                rowCount++;

                if (rowCount % maxRowsPerFile == 0) {
                    writer.close();
                    fileCount++;
                    writer = new BufferedWriter(new FileWriter(outputDirectory + "/output_" + fileCount + ".csv"));
                }
            }

            writer.close(); // 关闭最后一个文件的写入器
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
