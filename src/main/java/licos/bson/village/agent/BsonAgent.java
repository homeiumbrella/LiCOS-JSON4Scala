package licos.bson.village.agent;

import licos.bson.village.BsonUpdate;
import licos.bson.village.BsonName;
import licos.json.village.agent.JsonAgent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

/**
 * <pre>
 * Created on 2018/01/10.
 * </pre>
 *
 * @author K.Sakamoto
 */
@Entity("entities")
public class BsonAgent extends BsonAbstractAgent {
    @Getter @Setter
    private boolean isMine;

    @Getter @Setter
    private String status;

    @Getter @Setter @Reference
    private BsonUpdate update;

    @Getter @Setter
    private boolean isAChoice;

    @SuppressWarnings("unused")
    private BsonAgent() {
        // Do nothing
    }

    public BsonAgent(ObjectId _id,
                     String $context,
                     String $id,
                     long id,
                     BsonName name,
                     String image,
                     boolean isMine,
                     String status,
                     BsonUpdate update,
                     boolean isAChoice) {
        this._id = _id;
        this.$context = $context;
        this.$id = $id;
        this.id = id;
        this.name = name;
        this.image = image;
        this.isMine = isMine;
        this.status = status;
        this.update = update;
        this.isAChoice = isAChoice;
    }

    @Override
    public JsonAgent toJson() {
        return new JsonAgent(
                $context,
                $id,
                id,
                name.toJson(),
                image,
                isMine,
                status,
                update.toJson(),
                isAChoice
        );
    }
}