package femtocraft.core.items.decomposition;

import femtocraft.Femtocraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class NerveCluster extends Item {
	public NerveCluster(int id) {
		super(id);
		setMaxStackSize(64);
		setCreativeTab(Femtocraft.femtocraftTab);
		setTextureName(Femtocraft.ID.toLowerCase() + ":" + "NerveCluster");
	}

	public void updateIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(Femtocraft.ID
				.toLowerCase() + ":" + "NerveCluster");
	}
}
