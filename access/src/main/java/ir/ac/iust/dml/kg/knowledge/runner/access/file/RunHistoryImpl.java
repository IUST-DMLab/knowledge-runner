package ir.ac.iust.dml.kg.knowledge.runner.access.file;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RunHistoryImpl extends RunHistory {
    final BufferedWriter pwo;
    final BufferedWriter pwe;

    RunHistoryImpl(Path pathOutput, Path pathErrors) throws IOException {
        super(new ArrayList<>(), new ArrayList<>());
        pwo = Files.newBufferedWriter(pathOutput, Charset.forName("UTF-8"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        pwe = Files.newBufferedWriter(pathErrors, Charset.forName("UTF-8"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    }

    RunHistoryImpl(List<String> outputLines, List<String> errorLines) {
        super(outputLines, errorLines);
        pwo = null;
        pwe = null;
    }

    @Override
    public void appendError(String error) throws Exception {
        pwe.write(error);
        pwe.newLine();
        errorLines.add(error);
    }

    @Override
    public void appendOutput(String output) throws Exception {
        pwo.write(output);
        pwo.newLine();
        outputLines.add(output);
    }

    @Override
    public void close() throws IOException {
        if (pwo != null)
            pwo.close();
        if (pwe != null)
            pwe.close();
    }
}
