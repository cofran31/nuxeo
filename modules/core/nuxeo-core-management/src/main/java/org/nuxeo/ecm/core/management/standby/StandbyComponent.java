/*
 * (C) Copyright 2017 Nuxeo SA (http://nuxeo.com/) and others.
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
 *
 */
package org.nuxeo.ecm.core.management.standby;

import javax.management.JMException;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.management.ServerLocator;
import org.nuxeo.runtime.metrics.MetricsService;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.trackers.concurrent.ThreadEventHandler;
import org.nuxeo.runtime.trackers.concurrent.ThreadEventListener;

import io.dropwizard.metrics5.Counter;
import io.dropwizard.metrics5.Meter;
import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.SharedMetricRegistries;
import io.dropwizard.metrics5.Timer;
import io.dropwizard.metrics5.Timer.Context;

/**
 * @since 9.2
 */
public class StandbyComponent extends DefaultComponent {

    protected final StandbyCommand command = new StandbyCommand();

    protected MetricRegistry registry = SharedMetricRegistries.getOrCreate(MetricsService.class.getName());

    protected final Counter active = registry.counter(MetricRegistry.name(StandbyComponent.class, "active"));

    protected final Meter meter = registry.meter(MetricRegistry.name(StandbyComponent.class, "meter"));

    protected final Timer timer = registry.timer(MetricRegistry.name(StandbyComponent.class, "timer"));

    protected final ThreadLocal<Context> holder = new ThreadLocal<>();

    protected final ThreadEventListener threadsListener = new ThreadEventListener(new ThreadEventHandler() {

        @Override
        public void onEnter(boolean isLongRunning) {
            if (isLongRunning) {
                return;
            }
            holder.set(timer.time());
            meter.mark();
            active.inc();
        }

        @Override
        public void onLeave() {
            active.dec();
            holder.get().close();
        }

    });

    @Override
    public void activate(ComponentContext context) {
        threadsListener.install();
        try {
            command.registration.with(Framework.getService(ServerLocator.class).lookupServer()).register();
        } catch (JMException cause) {
            throw new NuxeoException("Cannot register standby command", cause);
        }
    }

    @Override
    public void deactivate(ComponentContext context) {
        threadsListener.uninstall();
        try {
            command.registration.unregister();
        } catch (JMException cause) {
            throw new NuxeoException("Cannot unregister standby command", cause);
        }
    }

}
