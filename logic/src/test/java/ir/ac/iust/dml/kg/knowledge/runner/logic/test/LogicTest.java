package ir.ac.iust.dml.kg.knowledge.runner.logic.test;

import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IRunDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.CommandLine;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Run;
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
    IRunDao runs;

    @Test
    public void testProcess() throws InterruptedException, IOException {
        final Run run = new Run("title", new ArrayList<>());
        final CommandLine step = new CommandLine("java", "Sample");
        step.setWorkingDirectory("E:\\IUST\\KnowledgeGraph\\knowledge-runner\\sample\\target\\classes");
        run.getCommands().add(step);
        runs.write(run);
        manager.run(run);
        Thread.sleep(3000);
        manager.shutdown();
    }
}
