package ir.ac.iust.dml.kg.knowledge.runner.access.file;

import ir.ac.iust.dml.kg.knowledge.runner.access.dao.IHistoryDao;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Repository
@PropertySource("classpath:persistence.properties")
public class HistoryDaoImpl implements IHistoryDao {
    @Value("${history.location}")
    private String baseFile;
    private Set<String> locks = new HashSet<>();

    @PostConstruct
    void setup() throws IOException {
        if (baseFile.startsWith("~"))
            baseFile = System.getProperty("user.home") + baseFile.substring(1);
        final Path dir = Paths.get(baseFile);
        if (!Files.exists(dir) && !Files.isDirectory(dir))
            Files.createDirectories(dir);
    }

    @Override
    public RunHistory create(Job job) throws Exception {
        synchronized (locks) {
            if (locks.contains(job.getIdentifier()))
                throw new Exception("File is in used");
            locks.add(job.getIdentifier());
        }
        final Path path1 = Paths.get(baseFile, job.getIdentifier() + ".output");
        final Path path2 = Paths.get(baseFile, job.getIdentifier() + ".error");
        return new RunHistoryImpl(path1, path2);
    }

    @Override
    public RunHistory read(Job job) throws IOException {
        final Path path1 = Paths.get(baseFile, job.getIdentifier() + ".output");
        final Path path2 = Paths.get(baseFile, job.getIdentifier() + ".error");
        return new RunHistoryImpl(Files.readAllLines(path1), Files.readAllLines(path2));
    }

    @Override
    public void delete(Job job) throws IOException {
        Files.deleteIfExists(Paths.get(baseFile, job.getIdentifier() + ".output"));
        Files.deleteIfExists(Paths.get(baseFile, job.getIdentifier() + ".error"));
    }
}
