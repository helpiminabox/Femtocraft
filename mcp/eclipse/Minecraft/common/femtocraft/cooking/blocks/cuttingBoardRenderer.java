package femtocraft.cooking.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import femtocraft.proxy.ClientProxyFemtocraft;

public class cuttingBoardRenderer implements ISimpleBlockRenderingHandler{

	@Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
                    RenderBlocks renderer) {
           
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
                    Block block, int modelId, RenderBlocks renderer)
    {
    	//Draw cutting board top/bottom
    	renderer.setRenderBounds(0, 0, 0, 1, 1.0D/16.0D, 1);
    	renderer.renderStandardBlock(block, x, y, z);
    	
    	CuttingBoard cuttingBoard = (CuttingBoard)block;
    	
    	//Draw sides
    	//Draw North side
    	renderer.setRenderBounds(1.0D/16.0D, 0, 0, 1.0D/16.0D, 1, 1);
    	renderer.renderNorthFace(block, x, y, z, cuttingBoard.cuttingBoardSideE);
    	
    	//Draw East side
    	renderer.setRenderBounds(0,0,1.0D/16.0D, 1, 1, 1.0D/16.0D);
    	renderer.renderEastFace(block, x, y, z, cuttingBoard.cuttingBoardSideNS);
    	
    	//Draw South side
    	renderer.setRenderBounds(15.0D/16.0D,0,0, 15.0D/16.0D, 1, 1);
    	renderer.renderSouthFace(block, x, y, z, cuttingBoard.cuttingBoardSideW);
    	
    	//Draw West side
    	renderer.setRenderBounds(0,0,15.0D/16.0D, 1, 1, 15.0D/16.0D);
    	renderer.renderWestFace(block, x, y, z, cuttingBoard.cuttingBoardSideNS);
    	
    	return true;
    }
   
    @Override
    public boolean shouldRender3DInInventory()
    {
    	return false;
    }

    @Override
    public int getRenderId()
    {       
    	return ClientProxyFemtocraft.cuttingBoardRenderType;
    }
    
}
