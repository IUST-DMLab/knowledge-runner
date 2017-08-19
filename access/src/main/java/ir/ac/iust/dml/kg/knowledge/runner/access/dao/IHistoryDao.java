package ir.ac.iust.dml.kg.knowledge.runner.access.dao;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

public interface IHistoryDao {
    RunHistory create(Job job) throws Exception;

    RunHistory read(Job job) throws Exception;

    void delete(Job jon) throws Exception;
}
