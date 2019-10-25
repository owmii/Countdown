package xieao.countdown.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import xieao.countdown.Countdown;

public class ItemGroups {
    public static final ItemGroup MAIN = new ItemGroup(Countdown.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.POTATO);
        }
    };
}
