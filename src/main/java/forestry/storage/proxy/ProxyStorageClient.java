package forestry.storage.proxy;

import forestry.core.models.ModelCrate;
import forestry.core.models.ModelIndex;
import forestry.core.proxy.Proxies;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ProxyStorageClient extends ProxyStorage {

	@Override
	public void registerCrateModel() {
		ModelResourceLocation modelLocation = new ModelResourceLocation("forestry:crate-filled", "crate-filled");
		Proxies.render.registerModel(new ModelIndex(modelLocation, new ModelCrate()));
	}
	
}
