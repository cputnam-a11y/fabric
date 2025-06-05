package net.fabricmc.fabric.api.event.registry;

import net.fabricmc.fabric.impl.registry.sync.registryentrylists.defaults.DefaultCustomRegistryEntryListsImpl;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;

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
