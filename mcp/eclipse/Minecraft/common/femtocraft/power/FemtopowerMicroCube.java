package femtocraft.power;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import femtocraft.Femtocraft;
import femtocraft.power.TileEntity.FemtopowerMicroCubeTile;
import femtocraft.proxy.ClientProxyFemtocraft;

public class FemtopowerMicroCube extends FemtopowerContainer {
	public Icon outputSide;
	public Icon inputSide;
	public Icon side;

	public FemtopowerMicroCube(int par1) {
		super(par1, Material.iron);
		setCreativeTab(Femtocraft.femtocraftTab);
		setUnlocalizedName("microCube");
		setHardness(3.f);
		setStepSound(Block.soundMetalFootstep);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new FemtopowerMicroCubeTile();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxyFemtocraft.FemtopowerMicroCubeRenderID;
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		FemtopowerMicroCubeTile tile = (FemtopowerMicroCubeTile)par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null && !par1World.isRemote)
		{
			tile.onSideActivate(ForgeDirection.getOrientation(par6));
			par1World.markBlockForUpdate(par2, par3, par4);
		}
		return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer,
				par6, par7, par8, par9);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		inputSide = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase()+":" + "MicroCube_input");
		outputSide = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase()+":" + "MicroCube_output");
		side = par1IconRegister.registerIcon(Femtocraft.ID.toLowerCase() + ":" + "MicroCube_side");
	}
}