/*
 * Copyright 2018 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.spi.cluster.hazelcast.tests.servicediscovery;

import io.vertx.spi.cluster.hazelcast.tests.LoggingTestWatcher;
import io.vertx.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.impl.DiscoveryImplTestBase;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.junit.Before;
import org.junit.Rule;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Thomas Segismont
 */
public class HazelcastDiscoveryImplClusteredTest extends DiscoveryImplTestBase {

  static {
    System.setProperty("hazelcast.wait.seconds.before.join", "0");
    System.setProperty("hazelcast.local.localAddress", "127.0.0.1");
  }

  @Rule
  public LoggingTestWatcher watchman = new LoggingTestWatcher();

  @Before
  public void setUp() throws Exception {
    Random random = new Random();
    System.setProperty("vertx.hazelcast.test.group.name", new BigInteger(128, random).toString(32));
    Vertx.builder()
      .withClusterManager(new HazelcastClusterManager())
      .buildClustered().onComplete(ar -> {
      vertx = ar.result();
    });
    while (vertx == null) {
      Thread.sleep(10);
    }
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());
  }
}
