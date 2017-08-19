package ir.ac.iust.dml.kg.knowledge.store.access.test;

import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IHistoryDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IJobDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobState;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Unit test for access
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:persistence-context.xml")
public class AccessTest {
    @Autowired
    IHistoryDao historyDao;
    @Autowired
    IJobDao jobDao;

    @Test
    public void job() {
        Job job = new Job("title", new ArrayList<>());
        jobDao.write(job);
        assert jobDao.readAllNeedForRerun().size() == 1;
        job.setState(JobState.Succeed);
        jobDao.write(job);
        assert jobDao.readAllNeedForRerun().size() == 0;
        jobDao.delete(job);
    }

    @Test
    public void history() throws Exception {
        final Job job = new Job("title", new ArrayList<>());
        jobDao.write(job);
        try (RunHistory run = historyDao.create(job)) {
            try (RunHistory run2 = historyDao.create(job)) {
                assert false;
            } catch (Exception ex) {

            }
            run.appendError("A");
            run.appendError("B");
            run.appendOutput("C");
        }

        try (RunHistory read = historyDao.read(job)) {
            assert read.getErrorLines().size() == 2;
            assert read.getOutputLines().size() == 1;
        }
        jobDao.delete(job);
    }
}
