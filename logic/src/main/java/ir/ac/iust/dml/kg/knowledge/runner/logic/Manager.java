package ir.ac.iust.dml.kg.knowledge.runner.logic;

import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IHistoryDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IJobDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobState;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobStep;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class Manager implements RunnerListener {
    static final Logger LOGGER = LogManager.getLogger(Manager.class);
    @Autowired
    private IJobDao jobs;
    @Autowired
    private IHistoryDao histories;
    private final Map<String, Runner> allRunning = new HashMap<>();


    @PostConstruct
    void setup() {
//        jobs.readAllNeedForRerun().forEach(this::run);
    }

    public void run(Job job) {
        synchronized (allRunning) {
            if (allRunning.containsKey(job.getIdentifier())) return;
            final Runner runner = new Runner(job, this);
            allRunning.put(job.getIdentifier(), runner);
            runner.start();
        }
    }

    public void shutdown() {
        LOGGER.info("Shutdown all");
        synchronized (allRunning) {
            allRunning.values().forEach(Runner::shutdown);
        }
        allRunning.values().forEach(i -> {
            try {
                i.join();
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public RunHistory open(Job job) throws Exception {
        return new RunHistoryWrapper(job, histories.create(job));
    }

    @Override
    public void started(Job job) {
        LOGGER.info(String.format("%s started", job));
        job.setStartEpoch(System.currentTimeMillis());
        job.setState(JobState.Running);
        jobs.write(job);
    }

    @Override
    public void completed(Job job, JobState state, Exception ex) {
        LOGGER.info(String.format("%s completed with %s", job, state));
        job.setEndEpoch(System.currentTimeMillis());
        job.setState(state);
        jobs.write(job);
        synchronized (allRunning) {
            allRunning.remove(job.getIdentifier());
        }
    }

    @Override
    public void reportProgress(Job job, float progress) {
        if (job.getProgress() != null) {
            job.setProgress(progress);
            jobs.write(job);
        } else
            job.setProgress(progress);
    }

    @Override
    public void reportStep(Job job, JobStep step, JobState result) {
        LOGGER.info(String.format("%s of %s completed with %s", step, job, result));
        step.setResult(result);
        jobs.write(job);
    }
}
