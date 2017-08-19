package ir.ac.iust.dml.kg.knowledge.runner.access.mongo;

import ir.ac.iust.dml.kg.knowledge.commons.MongoDaoUtils;
import ir.ac.iust.dml.kg.knowledge.commons.PagingList;
import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IHistoryDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IJobDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobState;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * impl {@link IJobDao}
 */
@Repository
public class JobDaoImpl implements IJobDao {
    @Autowired
    private IHistoryDao historyDao;
    @Autowired
    private MongoOperations op;

    @Override
    public void write(Job... jobs) {
        for (Job j : jobs) op.save(j);
    }

    @Override
    public void delete(Job... jobs) {
        for (Job j : jobs) {
            op.remove(j);
            try {
                historyDao.delete(j);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public Job read(ObjectId id) {
        return op.findOne(
                new Query().addCriteria(Criteria.where("id").is(id)),
                Job.class
        );
    }

    @Override
    public List<Job> readAllNeedForRerun() {
        final Query query = new Query();
        query.addCriteria(Criteria.where("state").ne(JobState.Succeed));
        return op.find(query, Job.class);
    }

    @Override
    public PagingList<Job> search(String title, JobState state, int page, int pageSize) {
        final Query query = new Query();
        if (title != null)
            query.addCriteria(Criteria.where("title").regex(title));
        if (state != null)
            query.addCriteria(Criteria.where("state").is(state));
        return MongoDaoUtils.paging(op, Job.class, query, page, pageSize);
    }
}
