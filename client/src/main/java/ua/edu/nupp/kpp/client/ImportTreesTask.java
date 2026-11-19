package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Logger;

public class ImportTreesTask extends Task<Integer> {
    private static final Logger LOG = Logger.getLogger(ImportTreesTask.class.getName());
    private final Path file;
    private final HttpGreenSpacesClient api;

    public ImportTreesTask(Path file, HttpGreenSpacesClient api) {
        this.file = file;
        this.api = api;
    }

    @Override
    protected Integer call() throws Exception {
        // Читаємо файл у кодуванні UTF-8
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        int total = Math.max(1, lines.size() - 1), imported = 0, processed = 0;

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] p = line.split(";", -1);
            if (p.length != 4) {
                LOG.warning("Пропущено рядок " + (i + 1) + ": невірна кількість полів");
                processed++;
                continue;
            }

            try {
                String rawDate = p[2].trim();
                if (rawDate.contains("-")) {
                    rawDate = rawDate.split("-")[0];
                }
                int plantingDate = Integer.parseInt(rawDate);

                int conditionScore = (int) Double.parseDouble(p[3].trim());

                Tree tree = new Tree(
                        null,
                        p[0].trim(),
                        p[1].trim(),
                        plantingDate,
                        conditionScore
                );

                api.addTree(tree);
                imported++;
            } catch (NumberFormatException e) {
                LOG.warning("Некоректне числове значення у рядку " + (i + 1) + ": " + e.getMessage());
            }

            processed++;
            updateProgress(processed, total);
            updateMessage("Імпортовано: " + imported + " із " + total);
        }
        return imported;
    }
}
