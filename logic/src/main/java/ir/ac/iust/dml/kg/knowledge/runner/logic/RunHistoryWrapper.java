package ir.ac.iust.dml.kg.knowledge.runner.logic;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

import java.io.IOException;

/**
 * Wrapper of run history to alarm history changed
 */
public class RunHistoryWrapper extends RunHistory {
    private final Job job;
    private final RunHistory runHistory;

    public RunHistoryWrapper(Job job, RunHistory runHistory) {
        super(runHistory.getOutputLines(), runHistory.getErrorLines());
        this.job = job;
        this.runHistory = runHistory;
    }

    @Override
    public void appendError(String error) throws Exception {
        Manager.LOGGER.error(String.format("%s:%s", job, error));
        runHistory.appendError(error);
    }

    @Override
    public void appendOutput(String output) throws Exception {
        Manager.LOGGER.info(String.format("%s:%s", job, output));
        runHistory.appendOutput(output);
    }

    @Override
    public void close() throws IOException {
        runHistory.close();
    }
}
