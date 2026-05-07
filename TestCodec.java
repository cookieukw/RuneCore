import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import java.io.File;
import java.nio.file.Files;
import com.hypixel.hytale.codec.CodecJson;
import com.hypixel.hytale.codec.Codec;
import java.util.Optional;

public class TestCodec {
    public static void main(String[] args) throws Exception {
        System.out.println("Reading Speed.json...");
        String json = Files.readString(new File("src/main/resources/Server/Entity/Effects/Speed.json").toPath());
        
        System.out.println("Attempting to parse using EntityEffect.CODEC...");
        try {
            Codec<EntityEffect> codec = (Codec<EntityEffect>) EntityEffect.CHILD_ASSET_CODEC;
            Optional<EntityEffect> res = CodecJson.parse(codec, json);
            System.out.println("Parsed: " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
