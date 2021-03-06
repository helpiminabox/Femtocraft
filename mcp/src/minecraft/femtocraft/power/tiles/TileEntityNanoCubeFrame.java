package femtocraft.power.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import femtocraft.FemtocraftDataUtils.Saveable;
import femtocraft.core.multiblock.IMultiBlockComponent;
import femtocraft.core.multiblock.MultiBlockInfo;
import femtocraft.core.tiles.TileEntityBase;

public class TileEntityNanoCubeFrame extends TileEntityBase implements
		IMultiBlockComponent {
	private @Saveable(desc = true) MultiBlockInfo info;

	public TileEntityNanoCubeFrame() {
		info = new MultiBlockInfo();
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
//		info.loadFromNBT(par1nbtTagCompound.getCompoundTag("info"));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
//		NBTTagCompound infoC = new NBTTagCompound();
//		info.saveToNBT(infoC);
//		par1nbtTagCompound.setTag("info", infoC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * femtocraft.core.tiles.TileEntityBase#handleDescriptionNBT(net.minecraft
	 * .nbt.NBTTagCompound)
	 */
	@Override
	public void handleDescriptionNBT(NBTTagCompound compound) {
		super.handleDescriptionNBT(compound);
//		info.loadFromNBT(compound.getCompoundTag("info"));
//		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * femtocraft.core.tiles.TileEntityBase#saveToDescriptionCompound(net.minecraft
	 * .nbt.NBTTagCompound)
	 */
	@Override
	public void saveToDescriptionCompound(NBTTagCompound compound) {
		super.saveToDescriptionCompound(compound);
//		NBTTagCompound infoC = new NBTTagCompound();
//		info.saveToNBT(infoC);
//		compound.setTag("info", infoC);
	}

	@Override
	public boolean onSideActivate(EntityPlayer par5EntityPlayer, int side) {
		if (isValidMultiBlock()) {
			TileEntity te = worldObj.getBlockTileEntity(info.x(), info.y(),
					info.z());
			// Big Oops? Or chunk unloaded...despite having player activating it
			// >.>
			if (te == null)
				return false;

			par5EntityPlayer.openGui(getMod(), getGuiID(), worldObj, info.x(),
					info.y(), info.z());
			return true;
		}
		return false;
	}

	@Override
	public boolean hasGUI() {
		return info.isValidMultiBlock();
	}

	@Override
	public boolean isValidMultiBlock() {
		return info.isValidMultiBlock();
	}

	@Override
	public boolean formMultiBlock(World world, int x, int y, int z) {
		boolean result = info.formMultiBlock(world, x, y, z);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord,
				worldObj.getBlockId(xCoord, yCoord, zCoord));
		return result;
	}

	@Override
	public boolean breakMultiBlock(World world, int x, int y, int z) {
		boolean result = info.breakMultiBlock(world, x, y, z);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord,
				worldObj.getBlockId(xCoord, yCoord, zCoord));
		return result;
	}

	@Override
	public MultiBlockInfo getInfo() {
		return info;
	}

}
