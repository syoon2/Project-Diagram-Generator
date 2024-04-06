/*
 * This file is part of the Project-Diagram-Generator distribution
 * (https://github.com/syoon2/Project-Diagram-Generator).
 * Copyright (c) 2024 Sung Ho Yoon.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ui.util.graphviz;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.lang3.reflect.*;
import org.apache.logging.log4j.*;

import guru.nidi.graphviz.engine.*;

/**
 * Sets up Graphviz engines, including the builtin engines and
 * the {@link JSGraphvizEngine Nashorn standalone engine}.
 * 
 * @see Graphviz#useEngine(GraphvizEngine, GraphvizEngine...)
 * 
 * @author Sung Ho Yoon
 * 
 * @since 2.0.2
 */
public class GraphvizEngineInitializer {

    private static Logger logger = LogManager.getLogger();

    private GraphvizEngineInitializer() {
    }

    public static final boolean GRAPHVIZ_AVAILABLE = setupGraphvizEngines();

    /**
     * Attempts to set up {@link GraphvizCmdLineEngine CMD line engine}, Graal-based
     * engine, and
     * {@link NashornGraphvizEngine Nashorn-based engine}.
     * 
     * @return {@code true} if there is at least one engine available for use
     */
    @SuppressWarnings("resource")
    public static boolean setupGraphvizEngines() {
        List<GraphvizEngine> engines = new ArrayList<>();
        try {
            boolean cmdLineEngineAvailable = (boolean) FieldUtils.readStaticField(
                    GraphvizCmdLineEngine.class, "AVAILABLE", true);
            if (cmdLineEngineAvailable) {
                engines.add(new GraphvizCmdLineEngine().timeout(10, TimeUnit.MINUTES));
            }
        } catch (ReflectiveOperationException ref) {
            logger.info("CMD line engine is not available", ref);
        }
        try {
            Method graalEngineGetter = MethodUtils.getMatchingMethod(GraphvizJdkEngine.class, "tryGraal");
            graalEngineGetter.setAccessible(true);
            if (Objects.nonNull(graalEngineGetter.invoke(null))) {
                Supplier<JavascriptEngine> graalEngineSupplier = () -> {
                    try {
                        return (JavascriptEngine) graalEngineGetter.invoke(null);
                    } catch (ReflectiveOperationException e) {
                        return null;
                    }
                };
                engines.add(new AbstractJsGraphvizEngine(false, graalEngineSupplier) {
                });
            } else {
                logger.info("Graal engine is not available");
            }
        } catch (Exception e) {
            logger.info("Graal engine is not available", e);
        }
        try {
            engines.add(new NashornGraphvizEngine());
        } catch (MissingDependencyException mis) {
            logger.info("Nashorn engine is not available", mis);
        }
        if (engines.isEmpty()) {
            logger.error("No Graphviz engine available. Diagram will not be generated.");
            return false;
        }
        Graphviz.useEngine(engines);
        return true;
    }
}
