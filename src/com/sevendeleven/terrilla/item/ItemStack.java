package com.sevendeleven.terrilla.item;

public class ItemStack {
	protected long item;
	protected int size;
	protected int meta;
	public ItemStack(long item, int size, int meta) {
		this.item = item;
		this.size = size;
		this.meta = meta;
	}
	public long getItem() {
		return this.item;
	}
	public int getSize() {
		return this.size;
	}
	public int getMeta() {
		return this.meta;
	}
	public ItemStack setItem(int item) {
		this.item = item;
		return this;
	}
	public ItemStack setSize(int size) {
		this.size = size;
		return this;
	}
	public ItemStack setMeta(int meta) {
		this.meta = meta;
		return this;
	}
	public boolean equals(Object o) {
		if (o == null || !o.getClass().equals(this.getClass())) {
			return false;
		}
		ItemStack i = (ItemStack) o;
		if (i.getItem() != this.getItem()) {
			return false;
		}
		if (i.getMeta() != this.getMeta()) {
			return false;
		}
		return true;
	}
	public ItemStack copy() {
		return new ItemStack(this.item, this.size, this.meta);
	}
}
