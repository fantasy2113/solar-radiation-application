package de.josmer.dwdcdc.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import de.josmer.dwdcdc.app.entities.cache.IrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCacheParser;

@Component("gson")
public class IrradiationCacheParser implements IIrradiationCacheParser {

	private final Gson gson;

	@Autowired
	public IrradiationCacheParser(Gson gson) {
		this.gson = gson;
	}

	@Override
	public String toJson(IIrradiationCache irradiationCache) {
		return gson.toJson(irradiationCache);
	}

	@Override
	public IIrradiationCache getDbCache(String json) {
		return gson.fromJson(json, IrradiationCache.class);
	}
}
