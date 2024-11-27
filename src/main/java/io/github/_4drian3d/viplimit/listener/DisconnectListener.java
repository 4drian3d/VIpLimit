package io.github._4drian3d.viplimit.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import io.github._4drian3d.viplimit.VIpLimit;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.InetAddress;
import java.util.Map;

public class DisconnectListener implements Listener<DisconnectEvent> {
  @Inject
  private EventManager eventManager;
  @Inject
  private VIpLimit plugin;
  @Inject
  private Map<InetAddress, Integer> limitMap;

  @Override
  public void register() {
    this.eventManager.register(plugin, DisconnectEvent.class, this);
  }

  @Override
  public @Nullable EventTask executeAsync(DisconnectEvent event) {
    return EventTask.async(() -> {
      if (event.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN) {
        return;
      }
      final InetAddress remoteAddress = event.getPlayer().getRemoteAddress().getAddress();
      limitMap.compute(remoteAddress, (ad, online) -> {
        if (online == null || online <= 1) {
          return online;
        }
        return online - 1;
      });
    });
  }
}
