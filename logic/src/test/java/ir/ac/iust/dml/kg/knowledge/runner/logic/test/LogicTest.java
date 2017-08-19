package ir.ac.iust.dml.kg.knowledge.runner.logic.test;

import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IJobDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobStep;
import ir.ac.iust.dml.kg.knowledge.runner.logic.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Unit test for access
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:logic-context.xml")
public class LogicTest {
    @Autowired
    Manager manager;
    @Autowired
    IJobDao jobs;

    @Test
    public void testProcess() throws InterruptedException, IOException {
        final Job job = new Job("title", new ArrayList<>());
        job.getSteps().add(new JobStep("calc.exe", "/c", "start"));
        jobs.write(job);
        manager.run(job);
        Thread.sleep(10000);
        manager.shutdown();
    }
}
