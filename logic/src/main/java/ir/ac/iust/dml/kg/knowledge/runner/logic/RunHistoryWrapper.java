package ir.ac.iust.dml.kg.knowledge.runner.logic;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Run;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

import java.io.IOException;

/**
 * Wrapper of run history to alarm history changed
 */
public class RunHistoryWrapper extends RunHistory {
    private final Run run;
    private final RunHistory runHistory;

    RunHistoryWrapper(Run run, RunHistory runHistory) {
        super(runHistory.getOutputLines(), runHistory.getErrorLines());
        this.run = run;
        this.runHistory = runHistory;
    }

    @Override
    public void appendError(String error) throws Exception {
        Manager.LOGGER.error(String.format("%s:%s", run, error));
        runHistory.appendError(error);
    }

    @Override
    public void appendOutput(String output) throws Exception {
        Manager.LOGGER.info(String.format("%s:%s", run, output));
        runHistory.appendOutput(output);
    }

    @Override
    public void close() throws IOException {
        runHistory.close();
    }
}
