package mods.railcraft.util;

import mods.railcraft.client.ClientEffects;
import net.minecraft.network.PacketBuffer;

public enum RemoteEffectType {

  TELEPORT() {
    @Override
    public void handle(ClientEffects effects, PacketBuffer input) {
      effects.readTeleport(input);
    }
  },
  FIRE_SPARK() {
    @Override
    public void handle(ClientEffects effects, PacketBuffer input) {
      effects.readFireSpark(input);
    }
  },
  FORCE_SPAWN() {
    @Override
    public void handle(ClientEffects effects, PacketBuffer input) {
      effects.readForceSpawn(input);
    }
  },
  ZAP_DEATH() {
    @Override
    public void handle(ClientEffects effects, PacketBuffer input) {
      effects.readZapDeath(input);
    }
  },
  BLOCK_PARTICLE() {
    @Override
    public void handle(ClientEffects effects, PacketBuffer input) {
      effects.readBlockParticle(input);
    }
  },
  ;

  public static final RemoteEffectType[] VALUES = values();

  public abstract void handle(ClientEffects effects, PacketBuffer input);
}
