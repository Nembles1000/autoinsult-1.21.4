package me.nembs.insult;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.time.Instant;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class DeathEvent implements ModInitializer {

    // 64 block range
    private static final double RENDER_DISTANCE = 64.0;

    // random function
    private static final Random RANDOM = new Random();

    // message list
    private static final List<String> DEATH_MESSAGES = Arrays.asList(
            "Ezz %s!",
            "%s needs to watch a Minecraft tutorial",
            "%s needs some help",
            "I believed in evolution, until I met %s",
            "%s forgot how to breathe",
            "If you're going to act like a turd, go lay on the yard",
            "On a scale from 1 to 10, how old are you?",
            "Some day you will go far and I hope you stay there",
            "I'm tired of wasting my time with %s",
            "There's no room for idiots like %s",
            "If i wanted to kill myself, I would climb to your ego and jump to your IQ",
            "See ya later %s",
            "When you look in the mirror, say hi to the clown you see in there",
            "Poor performance indeed",
            "Your idea of comeback is spitting into a fan",
            "You're in first place if we look at the leaderboard starting from the bottom, %s",
            "Your useless efforts only delay the inevitable, %s!",
            "Playtime is over!",
            "What a shame!",
            "%s is way too fat to wear his armor",
            "%s just received a reality check in form of defeat"
    );

    @Override
    public void onInitialize() {
        // listener
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof PlayerEntity) {
                PlayerEntity deadPlayer = (PlayerEntity) entity;
                Vec3d deathPos = deadPlayer.getPos();
                ServerWorld world = (ServerWorld) deadPlayer.getWorld();

                boolean found = world.getPlayers().stream()
                        .filter(player -> player != deadPlayer && player.isAlive())
                        .anyMatch(player ->
                                player.squaredDistanceTo(deathPos) <= RENDER_DISTANCE * RENDER_DISTANCE
                        );
                if (found) {
                    // select random message
                    String messageTemplate = DEATH_MESSAGES.get(
                            RANDOM.nextInt(DEATH_MESSAGES.size())
                    );
                    String formattedMessage = String.format(
                            messageTemplate,
                            deadPlayer.getName().getString()
                    );
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null && client.getNetworkHandler() != null) {
                        LastSeenMessageList.Acknowledgment emptyAck = new
                                LastSeenMessageList.Acknowledgment(0, new BitSet());

                        ChatMessageC2SPacket packet = new ChatMessageC2SPacket(
                                formattedMessage,
                                Instant.now(),
                                0L,
                                null,
                                emptyAck
                        );

                        client.getNetworkHandler().sendPacket(packet);
                    }
                }
            }
        });
    }
}