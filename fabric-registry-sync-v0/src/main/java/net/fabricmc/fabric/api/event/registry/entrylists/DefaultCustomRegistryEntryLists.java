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

package net.fabricmc.fabric.api.event.registry.entrylists;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;

import net.fabricmc.fabric.impl.registry.entrylists.defaults.DefaultCustomRegistryEntryListsImpl;

public class DefaultCustomRegistryEntryLists {
	@SafeVarargs
	public static <T> RegistryEntryList<T> union(RegistryEntryList<T>... parts) {
		return DefaultCustomRegistryEntryListsImpl.union(parts);
	}

	@SafeVarargs
	public static <T> RegistryEntryList<T> intersection(RegistryEntryList<T>... parts) {
		return DefaultCustomRegistryEntryListsImpl.intersection(parts);
	}

	public static <T> RegistryEntryList<T> inverse(RegistryEntryLookup<T> lookup, RegistryEntryList<T> opposite) {
		return DefaultCustomRegistryEntryListsImpl.inverse(lookup, opposite);
	}

	public static <T> RegistryEntryList<T> universal(RegistryEntryLookup<T> lookup) {
		return DefaultCustomRegistryEntryListsImpl.universal(lookup);
	}

	public static <T> RegistryEntryList<T> subtraction(RegistryEntryLookup<T> lookup, RegistryEntryList<T> initial, RegistryEntryList<T> subtracted) {
		return DefaultCustomRegistryEntryListsImpl.subtraction(lookup, initial, subtracted);
	}
}
