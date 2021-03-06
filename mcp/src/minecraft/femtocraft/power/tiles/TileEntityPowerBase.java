package femtocraft.power.tiles;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import femtocraft.FemtocraftDataUtils.Saveable;
import femtocraft.api.IPowerBlockContainer;
import femtocraft.api.PowerContainer;
import femtocraft.core.tiles.TileEntityBase;
import femtocraft.managers.research.EnumTechLevel;

public class TileEntityPowerBase extends TileEntityBase implements
		IPowerBlockContainer {
	private @Saveable(item = true)
	PowerContainer container = new PowerContainer(EnumTechLevel.MACRO, 250);
	private float maxPowerPerTick = .05f;
	private float maxSizePackets = .05f;
	private float distributionBuffer = .01f;
	public boolean[] connections = new boolean[6];;

	public TileEntityPowerBase() {
		super();
		Arrays.fill(connections, false);
	}

	public void setMaxStorage(int maxStorage_) {
		container.setMaxPower(maxStorage_);
	}

	public void setCurrentStorage(int currentStorage) {
		container.setCurrentPower(currentStorage);
	}

	public void setTechLevel(EnumTechLevel level) {
		container.setTechLevel(level);
	}

	public boolean isConnected(int i) {
		return connections[i];
	}

	public int numConnections() {
		int count = 0;
		for (int i = 0; i < 6; ++i)
			if (connections[i])
				++count;
		return count;
	}

	@Override
	public int getCurrentPower() {
		return container.getCurrentPower();
	}

	@Override
	public int getMaxPower() {
		return container.getMaxPower();
	}

	@Override
	public float getFillPercentage() {
		return container.getFillPercentage();
	}

	@Override
	public float getFillPercentageForCharging(ForgeDirection from) {
		return getFillPercentage();
	}

	@Override
	public float getFillPercentageForOutput(ForgeDirection to) {
		return getFillPercentage();
	}

	@Override
	public boolean canCharge(ForgeDirection from) {
		return getFillPercentage() < 1.0f;
	}

	@Override
	public int charge(ForgeDirection from, int amount) {
		return container.charge(amount);
	}

	@Override
	public boolean consume(int amount) {
		return container.consume(amount);
	}

	@Override
	public void updateEntity() {
		checkConnections();
		super.updateEntity();
	}

	@Override
	public void femtocraftServerUpdate() {
		// Don't do anything for empty containers
		if (getCurrentPower() <= 0) {
			return;
		}

		boolean[] willCharge = Arrays.copyOf(connections, 6);
		// boolean[] willCharge = new boolean[6];
		// Arrays.fill(willCharge, true);
		int numToFill = 0;
		for (int i = 0; i < 6; ++i) {
			if (willCharge[i])
				++numToFill;
		}
		float[] percentFilled = new float[6];
		Arrays.fill(percentFilled, 1.0f);
		int maxSpreadThisTick = (int) (((float) getCurrentPower()) * maxPowerPerTick);

		while (maxSpreadThisTick > 0 && numToFill > 0) {
			for (int j = 0; j < 6; ++j) {
				// Once it won't accept power, nothing will happen this tick,
				// Inside this update, that could make it accept power again
				if (!willCharge[j]) {
					continue;
				}

				ForgeDirection offset = ForgeDirection.getOrientation(j);
				int locx = this.xCoord + offset.offsetX;
				int locy = this.yCoord + offset.offsetY;
				int locz = this.zCoord + offset.offsetZ;

				TileEntity checkTile = this.worldObj.getBlockTileEntity(locx,
						locy, locz);

				if (checkTile == null
						|| !(checkTile instanceof IPowerBlockContainer)) {
					willCharge[j] = false;
					--numToFill;
					percentFilled[j] = 1.f;
					continue;
				}

				// Having passed initial check, and due to this now being this
				// block's
				// update function, can safely assume adjacent blocks remain the
				// same (unless it does simultaneous updates)
				IPowerBlockContainer container = (IPowerBlockContainer) this.worldObj
						.getBlockTileEntity(locx, locy, locz);

				if (container != null
						&& container.canCharge(offset.getOpposite())) {
					// Check for within buffer range - if so, this pipe will
					// only get less filled from here on out
					// So it will never attempt to fill that pipe again
					percentFilled[j] = container
							.getFillPercentageForCharging(offset.getOpposite());

					if ((this.getFillPercentageForOutput(offset) - percentFilled[j]) < distributionBuffer) {
						willCharge[j] = false;
						numToFill--;
						percentFilled[j] = 1.f;
					}
				} else {
					// Update as we fill
					willCharge[j] = false;
					numToFill--;
					percentFilled[j] = 1.f;
				}
			}

			// Find lowest % filled from
			int lowest = 0;
			float lowestAmt = 1.f;

			for (int i = 0; i < 6; i++) {
				if (willCharge[i] && percentFilled[i] < lowestAmt) {
					lowestAmt = percentFilled[i];
					lowest = i;
				}
			}

			ForgeDirection offset = ForgeDirection.getOrientation(lowest);
			int locx = this.xCoord + offset.offsetX;
			int locy = this.yCoord + offset.offsetY;
			int locz = this.zCoord + offset.offsetZ;

			int amountToFill = (int) ((float) getCurrentPower() * maxSizePackets);
			amountToFill = amountToFill < maxSpreadThisTick ? amountToFill
					: maxSpreadThisTick;
			amountToFill = amountToFill < getCurrentPower() ? amountToFill
					: getCurrentPower();
			// Having passed initial check, and due to this now being this
			// block's
			// update function, can safely assume adjacent blocks remain the
			// same (unless it does simultaneous updates)
			TileEntity TE = this.worldObj.getBlockTileEntity(locx, locy, locz);
			if (TE != null && TE instanceof IPowerBlockContainer) {
				IPowerBlockContainer container = (IPowerBlockContainer) TE;

				if (container != null) {
					int powerconsumed = container.charge(offset.getOpposite(),
							amountToFill);
					consume(powerconsumed);
					maxSpreadThisTick -= powerconsumed;
				}
			}
		}
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		// container = PowerContainer.createFromNBT(par1NBTTagCompound
		// .getCompoundTag("power"));

		// for(int i = 0; i < 6; i++) {
		// connections[i] =
		// par1NBTTagCompound.getBoolean(String.format("connection%d", i));
		// }
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		// NBTTagCompound power = new NBTTagCompound();
		// container.saveToNBT(power);
		// par1NBTTagCompound.setTag("power", power);

		// for(int i = 0; i < 6; i++) {
		// par1NBTTagCompound.setBoolean(String.format("connection%d", i),
		// connections[i]);
		// }
	}

	public void checkConnections() {
		boolean changed = false;
		for (int j = 0; j < 6; ++j) {
			boolean prev = connections[j];
			connections[j] = false;
			ForgeDirection offset = ForgeDirection.getOrientation(j);
			int locx = this.xCoord + offset.offsetX;
			int locy = this.yCoord + offset.offsetY;
			int locz = this.zCoord + offset.offsetZ;

			TileEntity checkTile = this.worldObj.getBlockTileEntity(locx, locy,
					locz);

			if (checkTile != null && checkTile instanceof IPowerBlockContainer) {
				IPowerBlockContainer fc = (IPowerBlockContainer) checkTile;
				if (!fc.canConnect(offset.getOpposite()))
					continue;
				if (!fc.canAcceptPowerOfLevel(
						getTechLevel(offset.getOpposite()),
						offset.getOpposite()))
					continue;

				connections[j] = true;
				if (prev != connections[j]) {
					changed = true;
				}
			}
		}
		if (changed)
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord,
					this.zCoord);
	}

	@Override
	public boolean canAcceptPowerOfLevel(EnumTechLevel level,
			ForgeDirection from) {
		return container.canAcceptPowerOfLevel(level);
	}

	@Override
	public EnumTechLevel getTechLevel(ForgeDirection to) {
		return container.getTechLevel();
	}

	@Override
	public boolean canConnect(ForgeDirection from) {
		return true;
	}

	@Override
	public void loadInfoFromItemNBT(NBTTagCompound compound) {
		super.loadInfoFromItemNBT(compound);
		// container = PowerContainer.createFromNBT(compound
		// .getCompoundTag("power"));
	}

	@Override
	public void saveInfoToItemNBT(NBTTagCompound compound) {
		super.saveInfoToItemNBT(compound);
		// NBTTagCompound power = new NBTTagCompound();
		// container.saveToNBT(power);
		// compound.setTag("power", power);
	}
}
