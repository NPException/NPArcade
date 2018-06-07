package de.npe.mcmods.nparcade.common.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Taken from Dimensional Pockets and slightly modified.
 * Originaly created by Jezza.
 */
public final class CoordSet {

	public final int x, y, z;

	public CoordSet(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public CoordSet(int[] array) throws ArrayIndexOutOfBoundsException {
		this(array[0], array[1], array[2]);
	}

	public CoordSet(TileEntity tile) {
		this(tile.xCoord, tile.yCoord, tile.zCoord);
	}

	/**
	 * Floors the player's position.
	 */
	public CoordSet(EntityPlayer player) {
		x = (int) Math.floor(player.posX);
		y = (int) Math.floor(player.posY);
		z = (int) Math.floor(player.posZ);
	}

	public boolean withinRange(CoordSet tempSet, int range) {
		return getDistanceSq(tempSet) <= range * range;
	}

	public double getDistanceSq(CoordSet tempSet) {
		double x2 = x - tempSet.x;
		double y2 = y - tempSet.y;
		double z2 = z - tempSet.z;
		return x2 * x2 + y2 * y2 + z2 * z2;
	}

	public float getDistance(CoordSet tempSet) {
		return net.minecraft.util.MathHelper.sqrt_double(getDistanceSq(tempSet));
	}

	public CoordSet addCoordSet(CoordSet coordSet) {
		return new CoordSet(x + coordSet.x, y + coordSet.y, z + coordSet.z);
	}

	public CoordSet addForgeDirection(ForgeDirection direction) {
		return new CoordSet(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
	}

	public boolean isAdjacent(CoordSet coordSet) {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			CoordSet tempSet = coordSet.addForgeDirection(direction);
			if (tempSet.equals(x, y, z)) {
				return true;
			}
		}
		return false;
	}

	public TileEntity getTileEntity(IBlockAccess world) {
		return world.getTileEntity(x, y, z);
	}

	public TileEntity getTileFromDirection(IBlockAccess world, ForgeDirection direction) {
		return addForgeDirection(direction).getTileEntity(world);
	}

	public boolean hasTileEntity(IBlockAccess world) {
		return getTileEntity(world) != null;
	}

	public Block getBlock(IBlockAccess world) {
		return world.getBlock(x, y, z);
	}

	public boolean setBlockToAir(World world) {
		return world.setBlockToAir(x, y, z);
	}

	public boolean isAirBlock(World world) {
		return world.isAirBlock(x, y, z);
	}

	public void writeToNBT(NBTTagCompound tag) {
		writeToNBT(tag, "coordSet");
	}

	public void writeToNBT(NBTTagCompound tag, String key) {
		tag.setIntArray(key, asArray());
	}

	public static CoordSet readFromNBT(NBTTagCompound tag) {
		return readFromNBT(tag, "coordSet");
	}

	public static CoordSet readFromNBT(NBTTagCompound tag, String key) {
		int[] coords = tag.getIntArray(key);
		if (coords.length == 3) {
			return new CoordSet(coords);
		}
		return null;
	}

	public static CoordSet createFromMinecraftTag(NBTTagCompound tag) {
		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");
		return new CoordSet(x, y, z);
	}

	public static CoordSet readFromString(String loc) {
		if (!loc.matches("-?\\d*:-?\\d*:-?\\d*")) {
			return null;
		}
		String[] coords = loc.split(":");

		int x = 0;
		int y = 0;
		int z = 0;
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
			z = Integer.parseInt(coords[2]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return new CoordSet(x, y, z);
	}

	public void writeBytes(ByteBuf bytes) {
		bytes.writeInt(x);
		bytes.writeInt(y);
		bytes.writeInt(z);
	}

	public static CoordSet readBytes(ByteBuf bytes) {
		int x = bytes.readInt();
		int y = bytes.readInt();
		int z = bytes.readInt();

		return new CoordSet(x, y, z);
	}

	public int[] asArray() {
		return new int[]{x, y, z};
	}

	public boolean equals(int x, int y, int z) {
		return this.x == x && this.y == y && this.z == z;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = 31 * hash + x;
		hash = 31 * hash + y;
		hash = 31 * hash + z;
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof CoordSet)
				&& ((CoordSet) other).equals(x, y, z);
	}

	public String toPacketString() {
		return x + ":" + y + ":" + z;
	}

	@Override
	public String toString() {
		return '[' + toPacketString() + ']';
	}

	public CoordSet toChunkCoords() {
		return new CoordSet(x >> 4, y >> 4, z >> 4);
	}

	public CoordSet toBlockCoords() {
		return new CoordSet(x << 4, y << 4, z << 4);
	}

	public CoordSet toChunkOffset() {
		return new CoordSet(
				x < 0 ? x % 16 + 16 & 15 : x % 16,
				y < 0 ? y % 16 + 16 & 15 : y % 16,
				z < 0 ? z % 16 + 16 & 15 : z % 16);
	}

	public static CoordSet fromChunkCoordinates(ChunkCoordinates coordinates) {
		return new CoordSet(coordinates.posX, coordinates.posY, coordinates.posZ);
	}
}
