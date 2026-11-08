package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
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
        List<String> lines = Files.readAllLines(file);
        int total = Math.max(1, lines.size() - 1), imported = 0;

        for (int i = 1; i < lines.size(); i++) {
            String[] p = lines.get(i).split(";");
            if (p.length != 4) {
                LOG.warning("Пропущено рядок " + (i + 1));
                continue;
            }

            Tree tree = new Tree(
                    null,
                    p[0],
                    p[1],
                    Integer.parseInt(p[2]),
                    Integer.parseInt(p[3])
            );

            api.addTree(tree);
            imported++;

            updateProgress(i, total);
            updateMessage("Імпортовано: " + imported + " із " + total);
        }
        return imported;
    }
}
