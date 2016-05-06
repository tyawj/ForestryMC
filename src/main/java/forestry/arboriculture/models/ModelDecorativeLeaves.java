/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.arboriculture.models;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.core.IModelBaker;
import forestry.arboriculture.blocks.BlockDecorativeLeaves;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.core.models.ModelBlockDefault;
import forestry.core.models.baker.ModelBaker;
import forestry.core.proxy.Proxies;
import forestry.core.render.TextureManager;

public class ModelDecorativeLeaves extends ModelBlockDefault<BlockDecorativeLeaves> {

	public ModelDecorativeLeaves() {
		super(BlockDecorativeLeaves.class);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		IModelBaker baker = new ModelBaker();

		Block block = state.getBlock();
		if (!blockClass.isInstance(block)) {
			return null;
		}
		BlockDecorativeLeaves bBlock = blockClass.cast(block);
		TreeDefinition tree = state.getValue(bBlock.getVariant());

		baker.setRenderBounds(Block.FULL_BLOCK_AABB);
		bakeBlock(bBlock, tree, baker);

		blockModel = baker.bakeModel(false);
		return blockModel.getQuads(state, side, rand);
	}
	
	@Override
	protected ItemOverrideList createOverrides() {
		return new LeaveOverideList();
	}
	
	private class LeaveOverideList extends ItemOverrideList{

		public LeaveOverideList() {
			super(Collections.emptyList());
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			IModelBaker baker = new ModelBaker();
			Block block = Block.getBlockFromItem(stack.getItem());
			if (!blockClass.isInstance(block)) {
				return null;
			}
			BlockDecorativeLeaves bBlock = blockClass.cast(block);
			TreeDefinition tree = bBlock.getTreeType(stack.getMetadata());

			baker.setRenderBounds(Block.FULL_BLOCK_AABB);
			bakeBlock(bBlock, tree, baker);

			return itemModel = baker.bakeModel(true);
		}
		
	}

	@Override
	protected void bakeInventoryBlock(@Nonnull BlockDecorativeLeaves block, @Nonnull ItemStack item, @Nonnull IModelBaker baker) {

	}

	@Override
	protected void bakeWorldBlock(@Nonnull BlockDecorativeLeaves block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IExtendedBlockState stateExtended, @Nonnull IModelBaker baker) {
	}

	public static void bakeBlock(BlockDecorativeLeaves block, TreeDefinition treeDefinition, IModelBaker baker) {
		if (treeDefinition == null) {
			return;
		}

		ITreeGenome genome = treeDefinition.getGenome();
		IAlleleTreeSpecies species = genome.getPrimary();
		ILeafSpriteProvider leafSpriteProvider = species.getLeafSpriteProvider();
		TextureAtlasSprite leaveSprite = leafSpriteProvider.getSprite(false, Proxies.render.fancyGraphicsEnabled());
		
		// Render the plain leaf block.
		baker.addBlockModel(block, Block.FULL_BLOCK_AABB, null, leaveSprite, 0);

		// Render overlay for fruit leaves.
		TextureAtlasSprite fruitSprite = TextureManager.getInstance().getSprite(genome.getFruitProvider().getDecorativeSpriteIndex());

		if (fruitSprite != null) {
			baker.addBlockModel(block, Block.FULL_BLOCK_AABB, null, fruitSprite, 1);
		}
		
		// Set the particle sprite
		baker.setParticleSprite(leaveSprite);
	}
}
