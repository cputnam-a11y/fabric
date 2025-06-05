package net.fabricmc.fabric.api.event.registry;

import net.fabricmc.fabric.impl.registry.sync.registryentrylists.CustomRegistryEntryListSerializerRegistryImpl;

import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomRegistryEntryListSerializerRegistry {
	static void registerSerializer(@NotNull CustomRegistryEntryListSerializer serializer) {
		CustomRegistryEntryListSerializerRegistryImpl.registerSerializer(serializer);
	}

	static @Nullable CustomRegistryEntryListSerializer getSerializer(Identifier id) {
		return CustomRegistryEntryListSerializerRegistryImpl.getSerializer(id);
	}
}
