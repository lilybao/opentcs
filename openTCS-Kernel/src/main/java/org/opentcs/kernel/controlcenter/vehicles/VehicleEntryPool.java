/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.kernel.controlcenter.vehicles;

import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.opentcs.access.LocalKernel;
import org.opentcs.components.Lifecycle;
import org.opentcs.data.model.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a pool of {@link VehicleEntry}s with an entry for every {@link Vehicle} object in the
 * kernel.
 * 
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class VehicleEntryPool
    implements Lifecycle {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(VehicleEntryPool.class);
  /**
   * The kernel.
   */
  private final LocalKernel kernel;
  /**
   * The entries of this pool.
   */
  private final Map<String, VehicleEntry> entries = new TreeMap<>();
  /**
   * Whether the pool is initialized or not.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   * 
   * @param kernel The kernel.
   */
  @Inject
  public VehicleEntryPool(@Nonnull LocalKernel kernel) {
    this.kernel = requireNonNull(kernel, "kernel");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      LOG.debug("Already initialized.");
      return;
    }
    
    kernel.getTCSObjects(Vehicle.class).stream()
        .forEach(vehicle -> entries.put(vehicle.getName(), new VehicleEntry(vehicle)));
    LOG.debug("Initialized vehicle entry pool: {}", entries);
    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      LOG.debug("Not initialized.");
      return;
    }

    entries.clear();
    initialized = false;
  }

  @Nonnull
  public Map<String, VehicleEntry> getEntries() {
    return entries;
  }

  @Nullable
  public VehicleEntry getEntryFor(@Nonnull String vehicleName) {
    return entries.get(vehicleName);
  }
}
