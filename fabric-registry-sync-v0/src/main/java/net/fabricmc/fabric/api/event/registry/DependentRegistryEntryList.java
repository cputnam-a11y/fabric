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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import net.minecraft.registry.entry.RegistryEntryList;

// Injected to RegistryEntryList<T>
@ApiStatus.NonExtendable
public interface DependentRegistryEntryList<T> {
	// creating a cycle in the graph will lead to stack overflow, do with this knowledge what you will
	Map<RegistryEntryList<?>, List<RegistryEntryList<?>>> DEPENDENCIES = new MapMaker()
			.weakKeys()
			.weakValues()
			.makeMap();

	default List<RegistryEntryList<T>> getDependencies() {
		return Collections.unmodifiableList(castToT(DEPENDENCIES.get(this.asSelf())));
	}

	/**
	 * Unregisters a dependency.
	 * Does nothing is the dependency is not registered.
	 *
	 * @param dependency the dependency to unregister
	 */
	default void unregisterDependency(RegistryEntryList<T> dependency) {
		List<RegistryEntryList<T>> dependencies = castToT(DEPENDENCIES.get(this.asSelf()));

		if (dependencies != null) {
			dependencies.remove(dependency);
		}
	}

	default void registerDependency(RegistryEntryList<T> dependency) {
		DEPENDENCIES.computeIfAbsent(
						this.asSelf(),
						k -> new ArrayList<>()
				)
				.add(dependency);
	}

	/**
	 * Invalidate dependents is called by this list when it is invalidated.
	 */
	private void invalidateDependents() {
		DEPENDENCIES.getOrDefault(this.asSelf(), List.of())
				.forEach(DependentRegistryEntryList::invalidate);
	}

	/**
	 * Invalidate is called by any list that this depends on when the parent list is invalidated.
	 */
	@MustBeInvokedByOverriders
	default void invalidate() {
		this.invalidateDependents();
	}

	// this interface should only be implemented on RegistryEntryList, so the case **should** be safe...
	private RegistryEntryList<T> asSelf() {
		return (RegistryEntryList<T>) this;
	}

	// why can't java just have field generics or something?
	@SuppressWarnings("unchecked")
	private static <T> List<RegistryEntryList<T>> castToT(List<RegistryEntryList<?>> entryList) {
		return (List<RegistryEntryList<T>>) (Object) entryList;
	}
}
