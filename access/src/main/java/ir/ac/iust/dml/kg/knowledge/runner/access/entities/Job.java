package ir.ac.iust.dml.kg.knowledge.runner.access.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * data class for triples
 * http://194.225.227.161:8081/browse/KG-180
 */
@XmlType(name = "Job", namespace = "http://kg.dml.iust.ac.ir")
@Document(collection = "jobs")
public class Job {
    @Id
    @JsonIgnore
    private ObjectId id;
    @Indexed
    private String title;
    private List<JobStep> steps;
    private long creationEpoch;
    private Long startEpoch;
    private Float progress;
    private Long endEpoch;
    @Indexed
    private JobState state;
    @Transient //Save it on file
    private String identifier;

    public Job() {
    }

    public Job(String title, List<JobStep> steps) {
        this.creationEpoch = System.currentTimeMillis();
        this.title = title;
        this.steps = steps;
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

    public List<JobStep> getSteps() {
        return steps;
    }

    public void setSteps(List<JobStep> steps) {
        this.steps = steps;
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

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public String getIdentifier() {
        return id != null ? id.toString() : null;
    }
}

