package bewis09.communicated.item

import bewis09.communicated.Communicated
import bewis09.communicated.item.interfaces.FlatModelItem
import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.UnaryOperator

object CommunicatedItems {
    val flatModelItems = arrayListOf<Item>()

    val LETTER = register("letter", { s -> LetterItem(s) }, Item.Settings())
    val LETTER_PAPER = register("letter_paper", { s -> LetterPaperItem(s) }, Item.Settings().maxCount(1), ItemGroups.TOOLS)
    val ENVELOPE = register("envelope", { s -> EnvelopeItem(s) }, Item.Settings().maxCount(1), ItemGroups.TOOLS)

    private fun register(path: String, factory: ((Item.Settings) -> Item), settings: Item.Settings?, vararg itemGroup: RegistryKey<ItemGroup>): Item {
        val registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("communicated", path))
        val i = Items.register(registryKey, factory, settings)
        if(i is GeneratedTranslationItem)
            Communicated.translations["item.communicated.$path"] = i.getTitle()
        if(i is FlatModelItem)
            flatModelItems.add(i)

        itemGroup.forEach { s ->
            ItemGroupEvents.modifyEntriesEvent(s).register {
                it.add { i }
            }
        }

        return i
    }
}