package ir.ac.iust.dml.kg.knowledge.runner.logic;

import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IHistoryDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IRunDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.CommandLine;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Run;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunState;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Manager implements RunnerListener {
    static final Logger LOGGER = LogManager.getLogger(Manager.class);
    @Autowired
    private IRunDao runs;
    @Autowired
    private IHistoryDao histories;
    private final Map<String, Runner> allRunning = new ConcurrentHashMap<>();


    @PostConstruct
    void setup() {
        runs.readAllNeedForRerun().forEach(this::run);
    }

    public void run(Run run) {
        synchronized (allRunning) {
            if (allRunning.containsKey(run.getIdentifier())) return;
            final Runner runner = new Runner(run, this);
            allRunning.put(run.getIdentifier(), runner);
            runner.start();
        }
    }

    public void shutdown() {
        LOGGER.info("Shutdown all");
        allRunning.values().forEach(Runner::shutdown);
        allRunning.values().forEach(i -> {
            try {
                i.join();
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public RunHistory open(Run run) throws Exception {
        return new RunHistoryWrapper(run, histories.create(run));
    }

    @Override
    public void started(Run run) {
        LOGGER.info(String.format("%s started", run));
        run.setStartEpoch(System.currentTimeMillis());
        run.setState(RunState.Running);
        runs.write(run);
    }

    @Override
    public void completed(Run run, RunState state, Exception ex) {
        LOGGER.info(String.format("%s completed with %s", run, state));
        run.setEndEpoch(System.currentTimeMillis());
        run.setState(state);
        runs.write(run);
        synchronized (allRunning) {
            allRunning.remove(run.getIdentifier());
        }
    }

    @Override
    public void reportProgress(Run run, float progress) {
        if (run.getProgress() == null || Math.abs(run.getProgress() - progress) >= 1) {
            LOGGER.info(String.format("Progress %s", progress));
            run.setProgress(progress);
            runs.write(run);
        } else
            run.setProgress(progress);
    }

    @Override
    public void command(Run run, CommandLine step, RunState result) {
        LOGGER.info(String.format("%s of %s completed with %s", step, run, result));
        step.setResult(result);
        runs.write(run);
    }
}
