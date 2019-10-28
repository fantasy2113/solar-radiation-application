package de.josmer.dwdcdc.app.entities.cache;

import java.time.LocalDateTime;
import java.util.LinkedList;

import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;

public class IrradiationCache implements IIrradiationCache {

	private int id;
	private String key;
	private LocalDateTime created;
	private LinkedList<SolIrrExp> months;

	public IrradiationCache(String key, LinkedList<SolIrrExp> months) {
		this.key = key;
		this.months = months;
		this.id = this.key.hashCode();
		this.created = LocalDateTime.now();
	}

	public IrradiationCache() {
		this.key = "";
		this.months = new LinkedList<>();
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public LinkedList<SolIrrExp> getMonths() {
		return months;
	}

	public void setMonths(LinkedList<SolIrrExp> months) {
		this.months = months;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
}
