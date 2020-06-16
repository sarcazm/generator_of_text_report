package reportgenerator.generators;


import org.w3c.dom.ls.LSOutput;
import reportgenerator.settings.Settings;

import java.util.ArrayList;

import java.util.List;

public class GeneratorPage {
    private Settings settings;
    private List<String> inputData;
    private List<String> result;

    private GeneratorHelper generatorHelper;


    public GeneratorPage(Settings settings, List<String> inputData) {
        generatorHelper = new GeneratorHelper(settings);
        this.settings = settings;
        this.inputData = inputData;
        result = new ArrayList<>();
    }

    public List<String> generate(){
        result.add(generatorHelper.getTitleRow());
        result.add(generatorHelper.getDelimiterRow());

        for (String input : inputData){
            String[] dataArr = input.split("\t");
            int sdvig = 0;
            int height = generatorHelper.countMaxHeight(dataArr);
            for (int i = 0; i < height; i++) {
                String[] inner = new String[dataArr.length];
                for (int j = 0; j < dataArr.length; j++) {
                    int widthColumn = settings.getColumns().get(j).getWidth();
                    if(dataArr[j].length() < widthColumn) {
                        if(i == 0) {
                            inner[j] = dataArr[j];
                        } else {
                            inner[j] = "";
                        }
                    } else {
                        int leftMarker = widthColumn * i + sdvig;
                        if (leftMarker >= dataArr[j].length()) {
                            inner[j] = "";
                        } else {
                            if(dataArr[j].charAt(leftMarker) == ' '){
                                leftMarker++;
                                sdvig++;
                            }
                            if((widthColumn + leftMarker) < dataArr[j].length()){
                                inner[j] = dataArr[j].substring(leftMarker, widthColumn + leftMarker);
                            }else{
                                inner[j] = dataArr[j].substring(leftMarker, dataArr[j].length());

                            }
                        }
                    }

                }
                result.add(generatorHelper.generate(inner));
                if (result.size() % settings.getPage().getHeight() == 0) {
                    result.add(GeneratorHelper.PAGE_DELIMITER);
                    result.add(generatorHelper.getTitleRow());
                    result.add(generatorHelper.getDelimiterRow());
                } else if(i == height-1) {
                    result.add(generatorHelper.getDelimiterRow());
                }
            }
        }

        return result;
    }

}
