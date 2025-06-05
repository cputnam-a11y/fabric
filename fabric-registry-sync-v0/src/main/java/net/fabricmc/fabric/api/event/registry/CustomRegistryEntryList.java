/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.event.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.impl.registry.sync.registryentrylists.CustomRegistryEntryListImpl;

public interface CustomRegistryEntryList<T> extends RegistryEntryList<T> {
	CustomRegistryEntryListSerializer getSerializer();

	static void registerSerializer(@NotNull CustomRegistryEntryListSerializer serializer) {
		CustomRegistryEntryListImpl.registerSerializer(serializer);
	}

	static @Nullable CustomRegistryEntryListSerializer getSerializer(Identifier id) {
		return CustomRegistryEntryListImpl.getSerializer(id);
	}

	@SafeVarargs
	static <T> RegistryEntryList<T> union(RegistryEntryList<T>... parts) {
		return CustomRegistryEntryListImpl.union(parts);
	}

	@SafeVarargs
	static <T> RegistryEntryList<T> intersection(RegistryEntryList<T>... parts) {
		return CustomRegistryEntryListImpl.intersection(parts);
	}

	static <T> RegistryEntryList<T> inverse(RegistryEntryLookup<T> lookup, RegistryEntryList<T> opposite) {
		return CustomRegistryEntryListImpl.inverse(lookup, opposite);
	}

	static <T> RegistryEntryList<T> universal(RegistryEntryLookup<T> lookup) {
		return CustomRegistryEntryListImpl.universal(lookup);
	}

	static <T> RegistryEntryList<T> subtraction(RegistryEntryLookup<T> lookup, RegistryEntryList<T> initial, RegistryEntryList<T> subtracted) {
		return CustomRegistryEntryListImpl.subtraction(lookup, initial, subtracted);
	}

	interface CustomRegistryEntryListSerializer {
		Identifier getIdentifier();

		<T1> MapCodec<? extends CustomRegistryEntryList<T1>> createCodec(RegistryKey<? extends Registry<T1>> registryKey, Codec<RegistryEntry<T1>> entryCodec, boolean forceList);

		<T1> PacketCodec<RegistryByteBuf, ? extends CustomRegistryEntryList<T1>> createPacketCodec(RegistryKey<? extends Registry<T1>> registryKey);
	}
}
