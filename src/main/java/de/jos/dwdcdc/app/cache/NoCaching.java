package de.jos.dwdcdc.app.cache;

import de.jos.dwdcdc.app.interfaces.IIrradiationCache;
import de.jos.dwdcdc.app.interfaces.IIrradiationCaching;
import de.jos.dwdcdc.app.interfaces.Identifiable;
import de.jos.dwdcdc.app.spring.BeanNames;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(BeanNames.NO_IRRADIATION_CACHING)
public class NoCaching implements IIrradiationCaching {

  @Override
  public void add(IIrradiationCache irradiationCache) {
    // do nothing
  }

  @Override
  public Optional<IIrradiationCache> get(Identifiable irrRequest) {
    return Optional.empty();
  }
}
