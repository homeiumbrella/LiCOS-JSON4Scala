package licos.bson.element.village;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import licos.bson.element.village.character.BsonRoleCharacter;
import licos.json.element.village.JsonStar;
import licos.json.element.village.JsonSubStar;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Entity("stars")
public class BsonStar extends BsonElement {

    @Id
    @SuppressWarnings("unused")
    private ObjectId _id;

    @Getter @Setter @Reference
    private BsonBase base;

    @Getter @Setter @Reference
    private BsonRoleCharacter myCharacter;

    @Getter @Setter @Reference
    private BsonStarInfo star;

    @SuppressWarnings("unused")
    private BsonStar() {
        // Do nothing
    }

    public BsonStar(ObjectId _id,
                    BsonBase base,
                    BsonRoleCharacter myCharacter,
                    BsonStarInfo star) {
        this._id = _id;
        this.base = base;
        this.myCharacter = myCharacter;
        this.star = star;
    }

    @Override
    public JsonStar toJson() {
        return new JsonStar(
                base.toJson(),
                new JsonSubStar(
                    myCharacter.toJson(),
                    star.toJson()
                )
        );
    }
}
