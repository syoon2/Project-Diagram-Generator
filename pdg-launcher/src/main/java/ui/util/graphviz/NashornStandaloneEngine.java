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

import javax.script.*;

import guru.nidi.graphviz.engine.*;

/**
 * Provides an implementation of {@link JavascriptEngine} that uses the
 * standalone <a href="https://github.com/openjdk/nashorn">Nashorn Engine</a>.
 * 
 * @author Sung Ho Yoon
 * @since 2.0.2
 */
public class NashornStandaloneEngine extends AbstractJavascriptEngine {
    private static final ScriptEngine ENGINE = new ScriptEngineManager().getEngineByName("nashorn");
    private final ScriptContext context = new SimpleScriptContext();
    private final ResultHandler resultHandler = new ResultHandler();

    /**
     * Constructs a new {@code NashornStandaloneEngine}.
     * 
     * @throws MissingDependencyException if the Nashorn engine is not available
     */
    public NashornStandaloneEngine() {
        if (ENGINE == null) {
            throw new MissingDependencyException("Nashorn engine is not available", "org.openjdk.nashorn:nashorn-core");
        }
        context.getBindings(ScriptContext.ENGINE_SCOPE).put("handler", resultHandler);
        eval("function result(r){ handler.setResult(r); }"
                + "function error(r){ handler.setError(r); }"
                + "function log(r){ handler.log(r); }");
    }

    /**
     * Executes the specified Javascript snippet.
     * 
     * @param js a Javascript snippet
     * @throws GraphvizException if there is a problem executing the argument
     */
    @Override
    protected String execute(String js) {
        eval(js);
        return resultHandler.waitFor();
    }

    private void eval(String js) {
        try {
            ENGINE.eval(js, context);
        } catch (ScriptException e) {
            throw new GraphvizException("Problem executing javascript", e);
        }
    }
}
