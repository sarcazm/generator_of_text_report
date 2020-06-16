package reportgenerator.settings;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Settings {

    private Page page;

    private List<Column> columns;

    public Settings() {
        page = new Page();
        columns = new ArrayList<>();
    }
}
