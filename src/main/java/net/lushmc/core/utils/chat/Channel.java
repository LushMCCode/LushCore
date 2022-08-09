package net.lushmc.core.utils.chat;

public interface Channel {

	public String getName();

	public boolean isGlobal();

	public String getTag();

	public boolean isLocal();

	public int getDistance();
}
