package net.fabricmc.fabric.api.event.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface CustomRegistryEntryListSerializer {
	Identifier getIdentifier();

	<T1> MapCodec<? extends CustomRegistryEntryList<T1>> createCodec(RegistryKey<? extends Registry<T1>> registryKey, Codec<RegistryEntry<T1>> entryCodec, boolean forceList);

	<T1> PacketCodec<RegistryByteBuf, ? extends CustomRegistryEntryList<T1>> createPacketCodec(RegistryKey<? extends Registry<T1>> registryKey);
}
