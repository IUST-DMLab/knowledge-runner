package ir.ac.iust.dml.kg.knowledge.runner.logic;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobState;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobStep;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

public interface RunnerListener {
    RunHistory open(Job job) throws Exception;

    void started(Job job);

    void completed(Job job, JobState state, Exception ex);

    void reportProgress(Job job, float progress);

    void reportStep(Job job, JobStep step, JobState failed);
}
