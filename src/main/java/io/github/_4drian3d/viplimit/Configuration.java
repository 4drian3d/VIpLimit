package io.github._4drian3d.viplimit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Configuration {

  Component limitReachedMessage();

  int playerLimitByIp();

  static void createIfNotExists(final Path path) throws IOException {
    if (Files.notExists(path)) {
      Files.createDirectory(path);
    }
    final Path configPath = path.resolve("config.conf");
    if (Files.notExists(configPath)) {
      try (InputStream stream = Configuration.class
              .getClassLoader().getResourceAsStream("config.conf")
      ) {
        if (stream != null) {
          Files.copy(stream, configPath);
        }
      }
    }
  }

  static Configuration load(final Path path) throws ConfigurateException {
    final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .path(path.resolve("config.conf"))
            .build();

    CommentedConfigurationNode rootNode = loader.load();

    final Component limitMessage = MiniMessage.miniMessage()
            .deserializeOr(rootNode.node("limit-message").getString(), Component.empty());
    final int playerLimitByIp = rootNode.node("player-limit-by-ip").getInt(100);

    return new Configuration() {

      @Override
      public Component limitReachedMessage() {
        return limitMessage;
      }

      @Override
      public int playerLimitByIp() {
        return playerLimitByIp;
      }
    };
  }
}
