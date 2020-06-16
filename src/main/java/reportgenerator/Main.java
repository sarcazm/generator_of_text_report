package reportgenerator;




import reportgenerator.generators.GeneratorHelper;
import reportgenerator.generators.GeneratorPage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            FileHelper fileHelper = new FileHelper("D:\\test\\settings.xml", "D:\\test\\source-data.tsv", "D:\\test\\example-report.txt");
            GeneratorPage generatorPage = new GeneratorPage(fileHelper.getSettings(), fileHelper.getInputData());
            List<String> result = generatorPage.generate();
            fileHelper.recordDataInFile(result);
            for (String s : result){
                System.out.println(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
