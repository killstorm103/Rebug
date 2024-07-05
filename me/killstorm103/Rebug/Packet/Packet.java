package me.killstorm103.Rebug.Packet;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import lombok.Getter;

@Getter
public final class Packet {

    private final Direction direction;
    private final PacketWrapper<?> rawPacket;
    private final PacketTypeCommon packetId;

    public Packet(Direction direction, PacketWrapper<?> rawPacket, PacketTypeCommon packetId) 
    {
        this.direction = direction;
        this.rawPacket = rawPacket;
        this.packetId = packetId;
    }
    
    public PacketWrapper<?> getRawPacket() {
		return rawPacket;
	}

	public boolean isReceiving() {
        return direction == Direction.RECEIVE;
    }

    public boolean isSending() {
        return direction == Direction.SEND;
    }
    public boolean isCustomPayload() { return isReceiving() && packetId == PacketType.Play.Client.PLUGIN_MESSAGE; }
    public boolean isFlying() {
        return isReceiving() && (packetId == PacketType.Play.Client.PLAYER_ROTATION || packetId == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || packetId == PacketType.Play.Client.PLAYER_POSITION || packetId == PacketType.Play.Client.PLAYER_FLYING);
    }

    public boolean isExplosion() {
        return isSending() && packetId == PacketType.Play.Server.EXPLOSION;
    }

    public boolean isRotation() {
        return isReceiving() && (packetId == PacketType.Play.Client.PLAYER_ROTATION || packetId == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
    }

    public boolean isPosition() {
        return isReceiving() && (packetId == PacketType.Play.Client.PLAYER_POSITION || packetId == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
    }

    public boolean isArmAnimation() {
        return isReceiving() && packetId == PacketType.Play.Client.ANIMATION;
    }

    public boolean isAbilities() {
        return isReceiving() && packetId == PacketType.Play.Client.PLAYER_ABILITIES;
    }

    public boolean isBlockPlace() {
        return isReceiving() && packetId == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT;
    }

    public boolean isBlockDig() {
        return isReceiving() && packetId == PacketType.Play.Client.PLAYER_DIGGING;
    }

    public boolean isEntityAction() {
        return isReceiving() && packetId == PacketType.Play.Client.ENTITY_ACTION;
    }

    public boolean isPosLook() {
        return isReceiving() && packetId == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }

    public boolean isCloseWindow() { return isReceiving() && packetId == PacketType.Play.Client.CLOSE_WINDOW; }

    public boolean isKeepAlive() { return isReceiving() && packetId == PacketType.Play.Client.KEEP_ALIVE; }

    public boolean isSteerVehicle() {
        return isReceiving() && packetId == PacketType.Play.Client.STEER_VEHICLE;
    }

    public boolean isHeldItemSlot() {
        return isReceiving() && packetId == PacketType.Play.Client.HELD_ITEM_CHANGE;
    }

    public boolean isAcceptingTeleport() {
        return isReceiving() && packetId == PacketType.Play.Client.TELEPORT_CONFIRM;
    }
    public boolean isTeleport() {
        return isSending() && packetId == PacketType.Play.Server.ENTITY_TELEPORT;
    }

    public boolean isVelocity() {
        return isSending() && packetId == PacketType.Play.Server.ENTITY_VELOCITY;
    }
    public enum Direction { SEND, RECEIVE }
}
