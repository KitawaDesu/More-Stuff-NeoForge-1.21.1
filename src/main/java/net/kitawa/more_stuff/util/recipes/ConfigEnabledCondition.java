package net.kitawa.more_stuff.util.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.lang.reflect.Field;

public record ConfigEnabledCondition(String className, String fieldName) implements ICondition {

    public static final MapCodec<ConfigEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    Codec.STRING.fieldOf("class").forGetter(ConfigEnabledCondition::className),
                    Codec.STRING.fieldOf("field").forGetter(ConfigEnabledCondition::fieldName)
            ).apply(inst, ConfigEnabledCondition::new)
    );

    @Override
    public boolean test(ICondition.IContext context) {
        try {
            Class<?> configClass = Class.forName(className);               // load the config class
            Field field = configClass.getDeclaredField(fieldName);         // get the static boolean field
            field.setAccessible(true);
            return field.getBoolean(null);                                 // read static boolean
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false; // fail safe: recipe disabled if error
        }
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
