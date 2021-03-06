package femtocraft.power.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import femtocraft.power.blocks.BlockCoilMicroCharging;
import femtocraft.proxy.ProxyClient;
import femtocraft.render.RenderModel;
import femtocraft.render.RenderPoint;
import femtocraft.render.RenderQuad;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderChargingCoil implements
		ISimpleBlockRenderingHandler {
	RenderModel segment;

	public RenderChargingCoil() {

	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		BlockCoilMicroCharging coil = (BlockCoilMicroCharging) block;
		if (coil == null)
			return;

		Tessellator tessellator = Tessellator.instance;

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_F(1, 1, 1);
		renderCoil(coil, 0, 0, 0);
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		BlockCoilMicroCharging coil = (BlockCoilMicroCharging) block;
		if (coil == null)
			return false;

		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(
				renderer.blockAccess, x, y, z));
		tessellator.setColorOpaque_F(1, 1, 1);
		renderCoil(coil, x, y, z);

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return ProxyClient.FemtocraftChargingCoilRenderID;
	}

	private void renderCoil(BlockCoilMicroCharging coil, int x, int y, int z) {
		if (segment == null) {
			createSegment(coil);
		}

		segment.location = new RenderPoint(x, y, z);
		segment.draw();
		segment.location = new RenderPoint(x, y + 8.f / 16.f, z);
		segment.draw();
	}

	private void createSegment(BlockCoilMicroCharging coil) {
		segment = new RenderModel();

		float minY = 0;
		float maxY = 8.f / 16.f;
		float min = 4.f / 16.f;
		float max = 12.f / 16.f;

		RenderQuad connectorNorth = new RenderQuad(new RenderPoint(min, minY, min), new RenderPoint(
				min, maxY, min), new RenderPoint(max, maxY, min), new RenderPoint(max,
				minY, min), coil.coilConnector);
		RenderQuad connectorSouth = new RenderQuad(new RenderPoint(max, minY, max), new RenderPoint(
				max, maxY, max), new RenderPoint(min, maxY, max), new RenderPoint(min,
				minY, max), coil.coilConnector);
		RenderQuad connectorEast = new RenderQuad(new RenderPoint(max, minY, min), new RenderPoint(max,
				maxY, min), new RenderPoint(max, maxY, max),
				new RenderPoint(max, minY, max), coil.coilConnector);
		RenderQuad connectorWest = new RenderQuad(new RenderPoint(min, minY, max), new RenderPoint(min,
				maxY, max), new RenderPoint(min, maxY, min),
				new RenderPoint(min, minY, min), coil.coilConnector);

		segment.addQuad(connectorNorth);
		segment.addQuad(connectorSouth);
		segment.addQuad(connectorEast);
		segment.addQuad(connectorWest);

		RenderQuad top = new RenderQuad(new RenderPoint(0, maxY, 0), new RenderPoint(0, maxY, 1),
				new RenderPoint(1, maxY, 1), new RenderPoint(1, maxY, 0),
				coil.coilConnectorTop);
		RenderQuad bot = new RenderQuad(new RenderPoint(1, minY, 0), new RenderPoint(1, minY, 1),
				new RenderPoint(0, minY, 1), new RenderPoint(0, minY, 0),
				coil.coilConnectorTop);

		segment.addQuad(top);
		segment.addQuad(bot);
	}
}
