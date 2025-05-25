package com.englishtest.englishtest.dto;

import com.englishtest.englishtest.entity.reading.QuestionAnswer;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {

    public static List<QuestionAnswer> readAnswerKeyFromExcel(InputStream is) throws Exception {
        List<QuestionAnswer> answerKey = new ArrayList<>();

        // Thay vì tạo XSSFWorkbook, dùng WorkbookFactory để tự động nhận dạng file
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rows = sheet.iterator();

        boolean firstRow = true; // nếu có header thì bỏ qua
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (firstRow) {
                firstRow = false;
                continue;
            }

            Cell questionCell = currentRow.getCell(0);
            Cell answerCell = currentRow.getCell(1);

            if (questionCell == null || answerCell == null) continue;

            int questionNumber = 0;
            String correctAnswer = "";

            // Xử lý dữ liệu trong questionCell có thể là numeric hoặc string
            if (questionCell.getCellType() == CellType.NUMERIC) {
                questionNumber = (int) questionCell.getNumericCellValue();
            } else if (questionCell.getCellType() == CellType.STRING) {
                try {
                    questionNumber = Integer.parseInt(questionCell.getStringCellValue());
                } catch (NumberFormatException e) {
                    // Nếu không parse được, bỏ qua dòng này
                    continue;
                }
            }

            // Xử lý dữ liệu trong answerCell, có thể cần kiểm tra CellType
            if (answerCell.getCellType() == CellType.STRING) {
                correctAnswer = answerCell.getStringCellValue();
            } else if (answerCell.getCellType() == CellType.NUMERIC) {
                // Nếu câu trả lời là số, convert sang string
                correctAnswer = String.valueOf(answerCell.getNumericCellValue());
            } else {
                continue; // nếu cell không đúng kiểu, bỏ qua
            }

            QuestionAnswer qa = new QuestionAnswer();
            qa.setQuestionNumber(questionNumber);
            qa.setCorrectAnswer(correctAnswer);

            answerKey.add(qa);
        }

        workbook.close();
        return answerKey;
    }
}
