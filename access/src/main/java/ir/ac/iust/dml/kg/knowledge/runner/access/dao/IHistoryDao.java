package ir.ac.iust.dml.kg.knowledge.runner.access.dao;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Run;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

public interface IHistoryDao {
    RunHistory create(Run run) throws Exception;

    RunHistory read(Run run) throws Exception;

    void delete(Run run) throws Exception;
}
