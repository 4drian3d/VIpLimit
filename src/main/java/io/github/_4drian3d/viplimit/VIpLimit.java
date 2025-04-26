package io.github._4drian3d.viplimit;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import io.github._4drian3d.viplimit.inject.ControlModule;
import io.github._4drian3d.viplimit.listener.Listener;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Plugin(
        id = "viplimit",
        name = "VIpLimit",
        description = "A simple IpLimit Plugin",
        version = Constants.VERSION,
        authors = {"4drian3d"}
)
public final class VIpLimit {

  @Inject
  @DataDirectory
  private Path dataFolder;
  @Inject
  private Injector injector;
  @Inject
  private Logger logger;

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    try {
      Configuration.createIfNotExists(dataFolder);
      final Configuration configuration = Configuration.load(dataFolder);
      final Map<InetAddress, Integer> limitMap = new ConcurrentHashMap<>();
      injector = injector.createChildInjector(new ControlModule(limitMap, configuration));
      Listener.registerListeners(injector);
    } catch (IOException e) {
      logger.info("Cannot load configuration", e);
    }
  }
}
