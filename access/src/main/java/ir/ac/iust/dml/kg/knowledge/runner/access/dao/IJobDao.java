package ir.ac.iust.dml.kg.knowledge.runner.access.dao;

import ir.ac.iust.dml.kg.knowledge.commons.PagingList;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobState;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Interface for read and write jobs
 */
public interface IJobDao {
    void write(Job... jobs);

    void delete(Job... jobs);

    Job read(ObjectId id);

    List<Job> readAllNeedForRerun();

    PagingList<Job> search(String title, JobState state, int page, int pageSize);
}
