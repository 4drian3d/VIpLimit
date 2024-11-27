package io.github._4drian3d.viplimit.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.viplimit.Configuration;
import io.github._4drian3d.viplimit.VIpLimit;

import java.net.InetAddress;
import java.util.Map;

public final class LoginListener implements Listener<LoginEvent> {
  @Inject
  private EventManager eventManager;
  @Inject
  private VIpLimit plugin;
  @Inject
  private Configuration configuration;
  @Inject
  private Map<InetAddress, Integer> limitMap;

  @Override
  public void register() {
    this.eventManager.register(plugin, LoginEvent.class, this);
  }

  @Override
  public EventTask executeAsync(LoginEvent event) {
    return EventTask.withContinuation(continuation -> {
      final Player player = event.getPlayer();
      limitMap.compute(player.getRemoteAddress().getAddress(), (address, value) -> {
        if (value == null) {
          continuation.resume();
          return 1;
        }
        if (configuration.playerLimitByIp() >= value) {
          event.getPlayer().disconnect(configuration.limitReachedMessage());
          continuation.resume();
          return value;
        }
        continuation.resume();
        return value + 1;
      });
    });
  }
}
