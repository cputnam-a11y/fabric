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

package net.fabricmc.fabric.impl.registry.sync.registryentrylists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.registry.CustomRegistryEntryList;
import net.fabricmc.fabric.impl.registry.sync.registryentrylists.defaults.DefaultRegistryEntryLists;
import net.fabricmc.fabric.impl.registry.sync.registryentrylists.defaults.IntersectionRegistryEntryList;
import net.fabricmc.fabric.impl.registry.sync.registryentrylists.defaults.InverseRegistryEntryList;
import net.fabricmc.fabric.impl.registry.sync.registryentrylists.defaults.UnionRegistryEntryList;
import net.fabricmc.fabric.impl.registry.sync.registryentrylists.defaults.UniversalRegistryEntryList;
import net.fabricmc.fabric.impl.registry.sync.registryentrylists.util.RegistryWrapperUtils;

public final class CustomRegistryEntryListImpl {
	private CustomRegistryEntryListImpl() {
		throw new UnsupportedOperationException();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomRegistryEntryListImpl.class);

	private static final Map<Identifier, CustomRegistryEntryList.CustomRegistryEntryListSerializer> SERIALIZERS = new HashMap<>();

	public static final Codec<CustomRegistryEntryList.CustomRegistryEntryListSerializer> SERIALIZER_CODEC = Identifier.CODEC.flatXmap(
			id -> Optional.ofNullable(SERIALIZERS.get(id))
					.map(DataResult::success)
					.orElseGet(
							() -> DataResult.error(
									() -> "Unknown CustomRegistryEntryListSerializer: " + id
							)
					),
			serializer -> Optional.of(serializer.getIdentifier())
					.filter(SERIALIZERS::containsKey)
					.map(DataResult::success)
					.orElseGet(
							() -> DataResult.error(
									() -> "Unknown CustomRegistryEntryListSerializer: " + serializer
							)
					)
	);

	public static <T> boolean isSerializedCustomRegistryEntryList(DynamicOps<T> ops, T entryList) {
		return ops.getMap(entryList)
				.map(it -> it.get("fabric:type"))
				.map(Objects::nonNull)
				.result()
				.orElse(false);
	}

	public static void registerSerializer(CustomRegistryEntryList.CustomRegistryEntryListSerializer serializer) {
		CustomRegistryEntryList.CustomRegistryEntryListSerializer previous = SERIALIZERS.put(serializer.getIdentifier(), serializer);

		if (previous != null) {
			LOGGER.warn("Overwriting CustomRegistryEntryListSerializer {} with {}", previous, serializer);
		}
	}

	public static CustomRegistryEntryList.CustomRegistryEntryListSerializer getSerializer(Identifier id) {
		return SERIALIZERS.get(id);
	}

	public static <T> RegistryEntryList<T> union(RegistryEntryList<T>[] parts) {
		return new UnionRegistryEntryList<>(List.of(validateNotNull(parts)));
	}

	public static <T> RegistryEntryList<T> intersection(RegistryEntryList<T>[] parts) {
		return new IntersectionRegistryEntryList<>(List.of(validateNotNull(parts)));
	}

	public static <T> RegistryEntryList<T> inverse(RegistryEntryLookup<T> lookup, RegistryEntryList<T> opposite) {
		Objects.requireNonNull(lookup, "lookup");
		Objects.requireNonNull(opposite, "opposite");
		return new InverseRegistryEntryList<>(RegistryWrapperUtils.castOrCreateFromEntryLookup(lookup), opposite);
	}

	public static <T> RegistryEntryList<T> universal(RegistryEntryLookup<T> lookup) {
		Objects.requireNonNull(lookup, "lookup");
		return new UniversalRegistryEntryList<>(RegistryWrapperUtils.castOrCreateFromEntryLookup(lookup));
	}

	public static <T> RegistryEntryList<T> subtraction(RegistryEntryLookup<T> lookup, RegistryEntryList<T> initial, RegistryEntryList<T> subtracted) {
		Objects.requireNonNull(lookup, "lookup");
		Objects.requireNonNull(initial, "initial");
		Objects.requireNonNull(subtracted, "subtracted");
		return new IntersectionRegistryEntryList<>(List.of(initial, inverse(lookup, subtracted)));
	}

	private static <T> RegistryEntryList<T>[] validateNotNull(RegistryEntryList<T>[] lists) {
		Objects.requireNonNull(lists, "list array");

		for (RegistryEntryList<T> list : lists) {
			Objects.requireNonNull(list, "list");
		}

		return lists;
	}

	public static void init() {
	}

	static {
		DefaultRegistryEntryLists.registerDefaults();
	}
}
