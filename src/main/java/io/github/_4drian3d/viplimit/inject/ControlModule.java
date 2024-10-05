package io.github._4drian3d.viplimit.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import io.github._4drian3d.viplimit.Configuration;

import java.net.InetSocketAddress;
import java.util.Map;

public record ControlModule(
        Map<InetSocketAddress, Integer> limitMap,
        Configuration configuration
) implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(new TypeLiteral<Map<InetSocketAddress, Integer>>(){}).toInstance(limitMap);
    binder.bind(Configuration.class).toInstance(configuration);
  }
}
