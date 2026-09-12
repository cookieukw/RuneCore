package com.cookie.runecore.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Detecta, por inferência, quando um jogador está "segurando" cada direção do WASD (mais
 * espaço/agachar) e avisa no chat quando a direção começa e quando termina.
 *
 * <p>Não existe, na API do servidor, um evento de tecla crua — o servidor nunca recebe "W foi
 * pressionado", só o resultado do movimento já processado pelo cliente a cada tick (pacote
 * {@code ClientMovement}). Este sistema reconstrói a intenção do jogador a partir de dois dados
 * que já chegam prontos e confiáveis todo tick, sem depender de clique nenhum:
 *
 * <ul>
 *   <li>{@link Velocity#getClientVelocity()} — velocidade horizontal em espaço de mundo (eixos
 *       X/Z), fornecida pelo próprio cliente a cada {@code ClientMovement};</li>
 *   <li>{@link TransformComponent#getRotation()} — para onde o jogador está olhando (yaw), usado
 *       para girar a velocidade de "espaço de mundo" para "espaço local" (frente/trás/direita/
 *       esquerda relativos à câmera, que é como W/A/S/D realmente funcionam).</li>
 * </ul>
 *
 * <p>Space e Shift/Ctrl não precisam dessa conta: {@code MovementStates.jumping} e
 * {@code MovementStates.crouching} já vêm prontos do mesmo pacote — a mesma fonte que
 * {@code ChildCarryHelper.isCrouching()} usa no SimTale sem nenhuma falha reportada.
 *
 * <p><b>Sobre a rotação frente/direita:</b> a convenção exata de qual ângulo de yaw corresponde a
 * "olhando para +Z" não foi confirmada por teste em jogo (não achei documentação nem exemplo no
 * código já existente que leia yaw para esse fim). A fórmula abaixo usa a convenção mais comum
 * (yaw 0° = olhando para -Z, giro horário aumenta o yaw). Se no teste W acender como S, ou A como
 * D, é só trocar o sinal nas duas linhas marcadas abaixo — o resto do sistema não muda.
 */
public class MovementKeyDetectionSystem extends EntityTickingSystem<EntityStore> {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    /**
     * Velocidade horizontal mínima (unidades/s, no mesmo espaço de {@code getClientVelocity()})
     * para considerar que o jogador está "empurrando" alguma direção. Abaixo disso é ruído de
     * física (deslizamento, atrito) e não uma tecla realmente segurada.
     */
    private static final double DEADZONE = 0.05;

    private static final Map<UUID, DirState> STATE = new ConcurrentHashMap<>();

    /** Um booleano por direção, guardando se ela estava "pressionada" no tick anterior. */
    private static final class DirState {
        boolean w, a, s, d, space, shift;
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player == null) return;

        Ref<EntityStore> playerRef = player.getReference();
        if (playerRef == null || !playerRef.isValid()) return;

        PlayerRef playerRefComp =
                store.getComponent(playerRef, Universe.get().getPlayerRefComponentType());
        if (playerRefComp == null) return;

        TransformComponent transform =
                store.getComponent(playerRef, TransformComponent.getComponentType());
        Velocity velocity = store.getComponent(playerRef, Velocity.getComponentType());
        MovementStatesComponent msc =
                store.getComponent(playerRef, MovementStatesComponent.getComponentType());
        if (transform == null || velocity == null || msc == null) return;

        MovementStates states = msc.getMovementStates();
        if (states == null) return;

        double yawDeg = transform.getRotation().yaw();
        double yawRad = Math.toRadians(yawDeg);

        Vector3d vel = velocity.getClientVelocity();
        double velX = vel.x();
        double velZ = vel.z();

        // Gira a velocidade (espaço de mundo) para espaço local do jogador.
        // Se W/S ou A/D saírem trocados no teste, inverta o sinal destas duas linhas.
        double forward = -velX * Math.sin(yawRad) - velZ * Math.cos(yawRad);
        double right = velX * Math.cos(yawRad) - velZ * Math.sin(yawRad);

        DirState prev = STATE.computeIfAbsent(playerRefComp.getUuid(), id -> new DirState());

        boolean w = forward > DEADZONE;
        boolean s = forward < -DEADZONE;
        boolean d = right > DEADZONE;
        boolean a = right < -DEADZONE;
        boolean space = states.jumping;
        boolean shift = states.crouching || states.forcedCrouching;

        report(playerRefComp, "W", "frente", prev.w, w);
        report(playerRefComp, "S", "trás", prev.s, s);
        report(playerRefComp, "A", "esquerda", prev.a, a);
        report(playerRefComp, "D", "direita", prev.d, d);
        report(playerRefComp, "SPACE", "pulo", prev.space, space);
        report(playerRefComp, "SHIFT", "agachar", prev.shift, shift);

        prev.w = w;
        prev.s = s;
        prev.a = a;
        prev.d = d;
        prev.space = space;
        prev.shift = shift;
    }

    /**
     * Manda a mensagem de "apertou" na borda de subida (false -&gt; true) e a de "soltou" na
     * borda de descida (true -&gt; false) — as duas ações pedidas: uma que avisa que a tecla foi
     * apertada, outra que calcula quando o jogador parou de apertar.
     */
    private void report(PlayerRef playerRefComp, String key, String label, boolean was, boolean is) {
        if (was == is) return;

        if (is) {
            playerRefComp.sendMessage(Message.raw("[RuneCore] Tecla pressionada: " + key + " (" + label + ")"));
            LOG.fine("[RuneCore] " + playerRefComp.getUuid() + " apertou " + key);
        } else {
            playerRefComp.sendMessage(Message.raw("[RuneCore] Tecla solta: " + key + " (" + label + ")"));
            LOG.fine("[RuneCore] " + playerRefComp.getUuid() + " soltou " + key);
        }
    }
}
