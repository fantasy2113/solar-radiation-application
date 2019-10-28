package de.josmer.dwdcdc.app.interfaces;

import java.time.LocalDate;
import java.util.Optional;

public interface IIrradiationCaching {

	void add(IIrradiationCache irradiationCache);

	Optional<IIrradiationCache> get(Identifiable irrRequest);

	default boolean isOldCache(IIrradiationCache irradiationCache) {
		final LocalDate localDate = LocalDate.now();
		final int validMonth = localDate.getDayOfMonth() > 15 ? localDate.getMonthValue() - 1
				: localDate.getMonthValue() - 2;
		return irradiationCache.getCreated().getYear() == localDate.getYear()
				&& irradiationCache.getMonths().stream().filter(m -> m.geteGlobGen() != 0).count() == validMonth;
	}
}
