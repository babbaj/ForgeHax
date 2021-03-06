package com.matt.forgehax.mods;

import static com.matt.forgehax.Helper.getFileManager;

import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.util.mod.Category;
import com.matt.forgehax.util.mod.ToggleMod;
import com.matt.forgehax.util.mod.loader.RegisterMod;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Created on 6/1/2017 by fr1kin */
@RegisterMod
public class CustomPayloadLogger extends ToggleMod {
  private static final File CLIENT_PAYLOAD_LOG =
      getFileManager().getFileInBaseDirectory("client2server_payload.log");
  private static final File SERVER_PAYLOAD_LOG =
      getFileManager().getFileInBaseDirectory("server2client_payload.log");

  public CustomPayloadLogger() {
    super(Category.MISC, "PayloadLogger", false, "Logs custom payloads");
  }

  private void log(Packet packet) {
    if (packet instanceof SPacketCustomPayload) {
      SPacketCustomPayload payloadPacket = (SPacketCustomPayload) packet;
      String input =
          String.format(
              "%s=%s\n",
              payloadPacket.getChannelName(), new String(payloadPacket.getBufferData().array()));
      try {
        Files.write(
            SERVER_PAYLOAD_LOG.toPath(),
            input.getBytes(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
      } catch (Exception e) {
      }
    } else if (packet instanceof CPacketCustomPayload) {
      CPacketCustomPayload payloadPacket = (CPacketCustomPayload) packet;
      String input =
          String.format(
              "%s=%s\n",
              payloadPacket.getChannelName(), new String(payloadPacket.getBufferData().array()));
      try {
        Files.write(
            CLIENT_PAYLOAD_LOG.toPath(),
            input.getBytes(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
      } catch (Exception e) {
      }
    }
  }

  @SubscribeEvent
  public void onOutgoingCustomPayload(PacketEvent.Outgoing.Pre event) {
    log(event.getPacket());
  }

  @SubscribeEvent
  public void onIncomingCustomPayload(PacketEvent.Incoming.Pre event) {
    log(event.getPacket());
  }
}
