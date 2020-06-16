package reportgenerator.generators;




import lombok.Getter;
import reportgenerator.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorHelper {
    private Settings settings;

    public static final String PAGE_DELIMITER = "~";
    public static final String WORD_DELIMITER = " ";
    public static final String COLUMN_DELIMITER = "|";
    public static final String ROW_DELIMITER = "-";

    @Getter
    private String titleRow;
    @Getter
    private String delimiterRow;


    public GeneratorHelper(Settings settings) {
        this.settings = settings;
        delimiterRow = ROW_DELIMITER.repeat(settings.getPage().getWidth());
        titleRow = generateTitleRow();
    }

    private String generateTitleRow() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(COLUMN_DELIMITER);
        for (int i = 0; i < settings.getColumns().size(); i++) {
            stringBuilder.append(generateColumn(settings.getColumns().get(i).getTitle(), settings.getColumns().get(i).getWidth()));
        }
        return stringBuilder.toString();
    }

    private StringBuilder generateColumn(String s, int columnWidth) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WORD_DELIMITER);
        stringBuilder.append(s);
        int diffSize = columnWidth - s.length() ;
        if(diffSize > 0) {
            stringBuilder.append(" ".repeat(diffSize));
        }
        stringBuilder.append(WORD_DELIMITER).append(COLUMN_DELIMITER);
        return stringBuilder;
    }

    public String generate(String[] data){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(COLUMN_DELIMITER);
        for (int i = 0; i < settings.getColumns().size(); i++) {
            stringBuilder.append(generateColumn(data[i], settings.getColumns().get(i).getWidth()));
        }
        return stringBuilder.toString();
    }



    public int countMaxHeight(String[] data){
        int height = 1;
        for(int i = 0; i < data.length; i++){
            int width = settings.getColumns().get(i).getWidth();
            if(height <= data[i].length() / width){
                if(data[i].length() % width == 0 && data[i].length() != 0) height = data[i].length() / width;
                else if(data[i].length() % width > 0) height = 1 + data[i].length() / width;
            }
        }
        return height;
    }

}
