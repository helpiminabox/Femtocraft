package femtocraft.power.TileEntity;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import femtocraft.Femtocraft;
import femtocraft.FemtocraftUtils;
import femtocraft.api.FemtopowerContainer;
import femtocraft.managers.research.TechLevel;

public class FemtopowerMicroCubeTile extends FemtopowerTile {
	public boolean[] outputs;
	static final public String packetChannel = Femtocraft.ID + ".MCube";
	public static final int maxStorage = 10000;
	public static final TechLevel techLevel = TechLevel.MICRO;

	public FemtopowerMicroCubeTile() {
		super();
		setMaxStorage(maxStorage);
		setTechLevel(techLevel);
		outputs = new boolean[6];
		Arrays.fill(outputs, false);
		setTechLevel(techLevel);
	}

	public static FemtopowerContainer getDefaultContainer() {
		return new FemtopowerContainer(techLevel, maxStorage);
	}

	public void onSideActivate(ForgeDirection side) {
		int i = FemtocraftUtils.indexOfForgeDirection(side);
		outputs[i] = !outputs[i];
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	// @Override
	// public String getPacketChannel() {
	// return packetChannel;
	// }

	@Override
	public void handleDescriptionNBT(NBTTagCompound compound) {
		super.handleDescriptionNBT(compound);
		parseOutputMask(compound.getByte("output"));
	}

	@Override
	public void saveToDescriptionCompound(NBTTagCompound compound) {
		super.saveToDescriptionCompound(compound);
		compound.setByte("output", generateOutputMask());
	}

	public byte generateOutputMask() {
		byte output = 0;

		for (int i = 0; i < 6; i++) {
			if (outputs[i])
				output += 1 << i;
		}
		return output;
	}

	public void parseOutputMask(byte mask) {
		byte temp;

		for (int i = 0; i < 6; i++) {
			temp = mask;
			outputs[i] = (((temp >> i) & 1) == 1) ? true : false;
		}

		if (worldObj != null)
			worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public float getFillPercentageForCharging(ForgeDirection from) {
		return outputs[FemtocraftUtils.indexOfForgeDirection(from)] ? 1.f : 0.f;
	}

	@Override
	public float getFillPercentageForOutput(ForgeDirection to) {
		return outputs[FemtocraftUtils.indexOfForgeDirection(to)] ? 1.f : 0.f;
	}

	@Override
	public boolean canCharge(ForgeDirection from) {
		return !outputs[FemtocraftUtils.indexOfForgeDirection(from)]
				&& super.canCharge(from);
	}

	@Override
	public int charge(ForgeDirection from, int amount) {
		if (!outputs[FemtocraftUtils.indexOfForgeDirection(from)]) {
			return super.charge(from, amount);
		}

		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		parseOutputMask(par1nbtTagCompound.getByte("output"));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setByte("output", generateOutputMask());
	}

}
