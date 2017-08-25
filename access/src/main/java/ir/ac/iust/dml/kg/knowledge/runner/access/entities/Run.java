package ir.ac.iust.dml.kg.knowledge.runner.access.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * data class for triples
 * http://194.225.227.161:8081/browse/KG-180
 */
@XmlType(name = "Run", namespace = "http://kg.dml.iust.ac.ir")
@Document(collection = "runs")
public class Run {
    @Id
    @JsonIgnore
    private ObjectId id;
    @Indexed
    private String title;
    private List<CommandLine> commands;
    private long creationEpoch;
    private Long startEpoch;
    private Float progress;
    private Long endEpoch;
    @Indexed
    private RunState state;

    public Run() {
    }

    public Run(String title, List<CommandLine> commands) {
        this.creationEpoch = System.currentTimeMillis();
        this.title = title;
        this.commands = commands;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommandLine> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandLine> commands) {
        this.commands = commands;
    }

    public long getCreationEpoch() {
        return creationEpoch;
    }

    public void setCreationEpoch(long creationEpoch) {
        this.creationEpoch = creationEpoch;
    }

    public Long getStartEpoch() {
        return startEpoch;
    }

    public void setStartEpoch(Long startEpoch) {
        this.startEpoch = startEpoch;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public Long getEndEpoch() {
        return endEpoch;
    }

    public void setEndEpoch(Long endEpoch) {
        this.endEpoch = endEpoch;
    }

    public RunState getState() {
        return state;
    }

    public void setState(RunState state) {
        this.state = state;
    }

    public String getIdentifier() {
        return id != null ? id.toString() : null;
    }

    @Override
    public String toString() {
        return String.format("Run{%s, %s}", id, title);
    }
}

