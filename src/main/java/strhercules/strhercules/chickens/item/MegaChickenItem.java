package strhercules.chickens.item;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.entity.MegaChicken;
import strhercules.chickens.registry.ModEntityTypes;
import strhercules.chickens.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/** Portable storage for a Mega Chicken, including its complete entity NBT. */
@EventBusSubscriber(modid = ChickensMod.MOD_ID)
public class MegaChickenItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String REVIVAL_TAG = "RequiresActivation";
    private static final String ACTIVATED_TAG = "Activated";

    public MegaChickenItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack createFromEntity(MegaChicken chicken, Item storedItem) {
        ItemStack stack = new ItemStack(storedItem);
        storeEntityData(stack, chicken);
        return stack;
    }

    public static ItemStack createFromDeath(MegaChicken chicken, Item storedItem) {
        chicken.removeAllEffects();
        ItemStack stack = createFromEntity(chicken, storedItem);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putBoolean(REVIVAL_TAG, true);
            tag.putBoolean(ACTIVATED_TAG, false);
        });
        stack.set(DataComponents.DAMAGE_RESISTANT, fireResistance(chicken.registryAccess()));
        return stack;
    }

    public static boolean requiresActivation(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getBooleanOr(REVIVAL_TAG, false);
    }

    public static boolean isActivated(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getBooleanOr(ACTIVATED_TAG, false);
    }

    public static void setActivated(ItemStack stack, boolean activated) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(ACTIVATED_TAG, activated));
        if (activated) {
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            stack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        }
    }

    private static DamageResistant fireResistance(RegistryAccess registryAccess) {
        return new DamageResistant(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypeTags.IS_FIRE));
    }

    private static void storeEntityData(ItemStack stack, MegaChicken chicken) {
        CompoundTag entityData;
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(chicken.problemPath(), LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, chicken.registryAccess());
            chicken.saveWithoutId(output);
            entityData = output.buildResult();
        }
        entityData.remove("Pos");
        entityData.remove("Motion");
        entityData.remove("Rotation");
        entityData.remove("DeathTime");
        entityData.remove("HurtTime");
        entityData.remove("HurtByTimestamp");
        stack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(chicken.getType(), entityData));
    }

    private static boolean hasEntityData(ItemStack stack) {
        return stack.get(DataComponents.ENTITY_DATA) != null;
    }

    @Nullable
    private static EntityType<?> readEntityType(ItemStack stack) {
        TypedEntityData<EntityType<?>> data = stack.get(DataComponents.ENTITY_DATA);
        return data == null ? null : data.type();
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        if (requiresActivation(stack) && !isActivated(stack)) {
            return tryActivate(stack, player) ? InteractionResult.CONSUME : InteractionResult.FAIL;
        }
        if (!hasEntityData(stack)) {
            notifyMissingData(player);
            return InteractionResult.FAIL;
        }

        Vec3 spawnPos = context.getClickLocation()
                .add(Vec3.atLowerCornerOf(context.getClickedFace().getUnitVec3i()).scale(0.02D));
        MegaChicken restored = restore(serverLevel, stack, spawnPos, player);
        if (restored == null) {
            notifyMissingData(player);
            return InteractionResult.FAIL;
        }
        consumeOne(stack, player);
        serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, BlockPos.containing(restored.position()));
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        if (requiresActivation(stack) && !isActivated(stack)) {
            return tryActivate(stack, player)
                    ? InteractionResult.CONSUME
                    : InteractionResult.FAIL;
        }

        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        Vec3 spawnPos = hit.getLocation()
                .add(Vec3.atLowerCornerOf(hit.getDirection().getUnitVec3i()).scale(0.02D));
        MegaChicken restored = restore(serverLevel, stack, spawnPos, player);
        if (restored == null) {
            notifyMissingData(player);
            return InteractionResult.FAIL;
        }
        consumeOne(stack, player);
        serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, BlockPos.containing(restored.position()));
        return InteractionResult.CONSUME;
    }

    @Nullable
    private MegaChicken restore(ServerLevel level, ItemStack stack, Vec3 position, @Nullable Player player) {
        EntityType<?> storedType = readEntityType(stack);
        if (storedType != ModEntityTypes.MEGA_CHICKEN.get()) {
            return null;
        }

        MegaChicken chicken = ModEntityTypes.MEGA_CHICKEN.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        if (chicken == null) {
            return null;
        }

        TypedEntityData<EntityType<?>> storedData = stack.get(DataComponents.ENTITY_DATA);
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(chicken.problemPath(), LOGGER)) {
            chicken.load(TagValueInput.create(reporter, level.registryAccess(), storedData.copyTagWithoutId()));
        }
        double safeY = Math.max(position.y(), level.getMinY() + 0.01D);
        chicken.snapTo(position.x(), safeY, position.z(), level.getRandom().nextFloat() * 360.0F, 0.0F);
        if (requiresActivation(stack)) {
            chicken.removeAllEffects();
            chicken.setHealth(chicken.getMaxHealth());
        }
        chicken.setDeltaMovement(Vec3.ZERO);
        chicken.setOnGround(true);
        level.addFreshEntity(chicken);

        if (player != null) {
            player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
        }
        return chicken;
    }

    private boolean tryActivate(ItemStack stack, @Nullable Player player) {
        if (player == null || !requiresActivation(stack) || isActivated(stack)) {
            return false;
        }
        ItemStack offhand = player.getOffhandItem();
        if (!offhand.is(Items.NETHER_STAR)) {
            player.sendOverlayMessage(
                    Component.translatable("item.chickens.mega_chicken.needs_nether_star"));
            return false;
        }

        offhand.shrink(1);
        setActivated(stack, true);
        player.sendOverlayMessage(Component.translatable("item.chickens.mega_chicken.activated"));
        return true;
    }

    private static void consumeOne(ItemStack stack, @Nullable Player player) {
        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    private static void notifyMissingData(@Nullable Player player) {
        if (player != null) {
            player.sendOverlayMessage(Component.translatable("item.chickens.mega_chicken.empty"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> tooltip, TooltipFlag flag) {
        if (!hasEntityData(stack)) {
            tooltip.accept(Component.translatable("item.chickens.mega_chicken.empty"));
        } else if (requiresActivation(stack) && !isActivated(stack)) {
            tooltip.accept(Component.translatable("item.chickens.mega_chicken.inactive"));
        } else if (requiresActivation(stack)) {
            tooltip.accept(Component.translatable("item.chickens.mega_chicken.active"));
        }
    }

    @SubscribeEvent
    public static void protectRevivalItem(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ItemEntity item)) {
            return;
        }
        ItemStack stack = item.getItem();
        if (!stack.is(ModRegistry.MEGA_CHICKEN_ITEM.get()) || !requiresActivation(stack)) {
            return;
        }

        stack.set(DataComponents.DAMAGE_RESISTANT, fireResistance(event.getLevel().registryAccess()));
        item.setInvulnerable(true);
        item.setNoGravity(true);
        item.setDeltaMovement(Vec3.ZERO);
        item.setUnlimitedLifetime();
        item.clearFire();

        double floor = event.getLevel().getMinY() + 0.25D;
        if (item.getY() < floor) {
            item.setPos(item.getX(), floor, item.getZ());
        }
    }
}
