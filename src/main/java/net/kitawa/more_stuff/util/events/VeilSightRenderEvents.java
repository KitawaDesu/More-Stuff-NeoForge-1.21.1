package net.kitawa.more_stuff.util.events;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = MoreStuff.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class VeilSightRenderEvents {

    private static final Set<Integer> madeVisible = new HashSet<>();

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        if (!VeilSightHelper.localPlayerHasVeilSight()) return;
        LivingEntity entity = event.getEntity();
        if (entity.isInvisible()) {
            entity.setInvisible(false);
            madeVisible.add(entity.getId());
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (madeVisible.remove(entity.getId())) {
            entity.setInvisible(true);
        }
    }

    @SubscribeEvent
    public static void onEndermanAnger(LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof EnderMan enderman)) return;
        if (!(event.getNewAboutToBeSetTarget() instanceof Player player)) return;

        var registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        boolean hasVeilSight = StreamSupport.stream(player.getArmorSlots().spliterator(), false)
                .anyMatch(stack -> stack.getEnchantmentLevel(
                        registry.getOrThrow(ModEnchantments.VEIL_SIGHT)) > 0);

        if (hasVeilSight) {
            event.setCanceled(true);
        }
    }
}