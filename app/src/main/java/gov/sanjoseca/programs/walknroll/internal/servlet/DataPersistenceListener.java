/*
 * Copyright 2017, Harsha R.
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

package gov.sanjoseca.programs.walknroll.internal.servlet;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import org.jboss.jandex.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet listener for initializing the Objectify libraries. We need to register various persistent models within
 * this class.
 */
@WebListener
public class DataPersistenceListener implements ServletContextListener {

    private static final String CLASS_NAME = DataPersistenceListener.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static Collection<Class<?>> entityDefinitions;

    public void contextInitialized(ServletContextEvent event) {
        final String METHOD_NAME = "contextInitialized";

        // This will be invoked as part of a warmup request, or the first user request if no warmup
        // request.
        loadEntityDefinitions(event.getServletContext());
        if (entityDefinitions != null) {
            entityDefinitions.forEach(ObjectifyService::register);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }

    private void loadEntityDefinitions(ServletContext context) {
        final String METHOD_NAME = "loadEntityDefinitions";

        if (entityDefinitions != null) {
            return;
        }

        Collection<Class<?>> classes = getEntityDefinitionsFromJandex(context);
        if (classes == null) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME,
                    "Unable to identify classes using the Jandex index. Relying on alternate load.");
            classes = scanForEntityDefinitions();
        }
        if (classes != null) {
            // Prune the list and remove any thing that is abstract.
            classes.removeIf(clazz -> Modifier.isAbstract(clazz.getModifiers()));
        }
        entityDefinitions = classes;
    }

    /**
     * Read the Jandex index file and return the classes that have the required annotation on them.
     *
     * @param context the servlet context instance.
     * @return a collection of classes that have the required annotations on them.
     */
    private Collection<Class<?>> getEntityDefinitionsFromJandex(ServletContext context) {
        final String METHOD_NAME = "getEntityDefinitionsFromJandex";
        long startTime = System.currentTimeMillis();
        Set<URL> urlList = new HashSet<>();
        final Set<Class<?>> defs = new HashSet<>();
        try {
            Enumeration<URL> refs =
                    Thread.currentThread().getContextClassLoader().getResources("META-INF/jandex.idx");
            while (refs.hasMoreElements()) {
                urlList.add(refs.nextElement());
            }
            URL idxFile = context.getResource("/META-INF/jandex.idx");
            if (idxFile != null) {
                urlList.add(idxFile);
            }

            if (urlList.isEmpty()) {
                return null;
            }

            urlList.forEach(url -> {
                if (url == null) {
                    return;
                }
                try {
                    IndexReader reader = new IndexReader(url.openStream());
                    Index index = reader.read();
                    DotName entities = DotName.createSimple(Entity.class.getName());
                    List<AnnotationInstance> annotations = index.getAnnotations(entities);
                    annotations.forEach(ann -> {
                        if (ann.target().kind() == AnnotationTarget.Kind.CLASS) {
                            String name = ann.target().asClass().name().toString();
                            try {
                                Class<?> clazz = Class.forName(name);
                                defs.add(clazz);
                            } catch (ClassNotFoundException e) {
                                LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME,
                                        "Unable to locate class that is part of the Jandex index.", e);
                            }
                        }
                    });
                } catch (IOException e) {
                    LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME,
                            "Unable to read Jandex file. Persistence might not work.", e);
                }
            });
        } catch (IOException e) {
            LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME,
                    "Unable to read Jandex file. Persistence might not work.", e);
        } finally {
            LOGGER.logp(Level.INFO, CLASS_NAME, METHOD_NAME, "Class scan completed in {0}ms",
                    (System.currentTimeMillis() - startTime));
        }
        return defs;
    }

    /**
     * Dynamically scan the classpath and return classes that have the required annotation on them.
     *
     * @return a collection of classes that have the required annotation on them.
     */
    private Collection<Class<?>> scanForEntityDefinitions() {
        Reflections reflections = new Reflections("gov.sanjoseca.programs.walknroll",
                new SubTypesScanner(), new TypeAnnotationsScanner());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
        return classes;
    }

}
