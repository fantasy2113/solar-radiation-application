package de.jos.dwdcdc.app.repositories;

import de.jos.dwdcdc.app.entities.Precalculated;
import de.jos.dwdcdc.app.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface PrecalculatedRepositoryCrud extends CrudRepository<Precalculated, Long> {
}
