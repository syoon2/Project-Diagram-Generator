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

import guru.nidi.graphviz.engine.*;

/**
 * Implementation of {@link GraphvizEngine} that uses
 * {@link NashornStandaloneEngine}.
 * 
 * @author Sung Ho Yoon
 * @since 2.0.2
 */
public class NashornGraphvizEngine extends AbstractJsGraphvizEngine {

    /**
     * Constructs a new {@code NashornGraphvizEngine}.
     */
    public NashornGraphvizEngine() {
        super(false, () -> new NashornStandaloneEngine());
    }

    @Override
    protected void doInit() {
        final JavascriptEngine engine = engine();
        engine.executeJavascript(promiseJsCode());
        super.doInit();
    }
}
 