package me.desht.pneumaticcraft.common.thirdparty.computer_common;

import me.desht.pneumaticcraft.api.item.EnumUpgrade;
import me.desht.pneumaticcraft.common.block.BlockPneumaticCraft;
import me.desht.pneumaticcraft.common.core.ModBlocks;
import me.desht.pneumaticcraft.common.thirdparty.ThirdPartyManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

import java.util.Collections;
import java.util.Map;

public class BlockDroneInterface extends BlockPneumaticCraft {
    static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public BlockDroneInterface() {
        super(ModBlocks.defaultProps());

        setDefaultState(getStateContainer().getBaseState().with(CONNECTED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);

        builder.add(CONNECTED);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityDroneInterface.class;
    }

    @Override
    public Map<EnumUpgrade, Integer> getApplicableUpgrades() {
        return Collections.emptyMap();
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (ThirdPartyManager.instance().isModTypeLoaded(ThirdPartyManager.ModType.COMPUTER)) {
            super.fillItemGroup(group, items);
        }
    }
}
