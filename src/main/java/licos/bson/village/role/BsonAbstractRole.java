package licos.bson.village.role;

import licos.bson.village.BsonElement;
import licos.bson.village.BsonName;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

public abstract class BsonAbstractRole extends BsonElement {
    @Id @SuppressWarnings("unused")
    protected ObjectId _id;

    @Getter @Setter
    protected String $context;

    @Getter @Setter
    protected String $id;

    @Getter @Setter @Reference
    protected BsonName name;

    @Getter @Setter
    protected String image;
}