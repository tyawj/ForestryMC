package forestry.api.genetics;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.core.gui.GuiAlyzer;

public interface IAlyzerPlugin {

	@SideOnly(Side.CLIENT)
	void drawAnalyticsPage1(GuiAlyzer gui, ItemStack itemStack);

	@SideOnly(Side.CLIENT)
	void drawAnalyticsPage2(GuiAlyzer gui, ItemStack itemStack);

	@SideOnly(Side.CLIENT)
	void drawAnalyticsPage3(GuiAlyzer gui, ItemStack itemStack);

	Map<String, ItemStack> getIconStacks();

	List<String> getHints();
}
