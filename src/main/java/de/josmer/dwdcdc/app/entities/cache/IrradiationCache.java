package de.josmer.dwdcdc.app.entities.cache;

import java.time.LocalDateTime;
import java.util.LinkedList;

import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;

public class IrradiationCache implements IIrradiationCache {

	private LocalDateTime created;
	private int id;
	private String key;
	private LinkedList<SolIrrExp> months;

	public IrradiationCache() {
		this.key = "";
		this.months = new LinkedList<>();
	}

	public IrradiationCache(String key, LinkedList<SolIrrExp> months) {
		this.key = key;
		this.months = months;
		this.id = this.key.hashCode();
		this.created = LocalDateTime.now();
	}

	@Override
	public LocalDateTime getCreated() {
		return created;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public LinkedList<SolIrrExp> getMonths() {
		return months;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMonths(LinkedList<SolIrrExp> months) {
		this.months = months;
	}
}
