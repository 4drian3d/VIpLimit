package io.github._4drian3d.viplimit.listener;

import com.google.inject.Injector;
import com.velocitypowered.api.event.AwaitingEventExecutor;

public interface Listener<E> extends AwaitingEventExecutor<E> {
  void register();

  static void registerListeners(Injector injector) {
    final Listener<?>[] listeners = {
            injector.getInstance(DisconnectListener.class),
            injector.getInstance(LoginListener.class)
    };
    for (final Listener<?> listener : listeners) {
      listener.register();
    }
  }
}
