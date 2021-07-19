/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides metadata for Railcraft Modules. Any class annotated by this and implementing {@link IRailcraftModule}
 * will be loaded as a Module by Railcraft during its initialization phase.
 *
 * Regarding dependencies, you may use either dependencies() or dependencyClasses() or a combination of both.
 *
 * Created by CovertJaguar on 4/5/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RailcraftModule {
    /**
     * An identifier for the module.
     */
    String value();

    /**
     * A description for the module, used in configurations.
     */
    String description() default "";

    /**
     * The names of any other modules this module depends on.
     */
    String[] dependencies() default {};

    /**
     * The classes of any other modules this module depends on.
     */
    Class<? extends IRailcraftModule>[] dependencyClasses() default {};

    /**
     * The names of any other modules this module loads after.
     */
    String[] softDependencies() default {};

    /**
     * The classes of any other modules this module loads after.
     */
    Class<? extends IRailcraftModule>[] softDependencyClasses() default {};
}
