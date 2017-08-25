package ir.ac.iust.dml.kg.knowledge.runner.access.dao;

import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Definition;
import org.bson.types.ObjectId;

import java.util.List;

public interface IDefinitionDao {
    void write(Definition... definitions);

    void delete(Definition... definitions);

    Definition read(ObjectId id);

    Definition readByTitle(String title);

    List<Definition> readAll();
}
