//  Copyright 2017 Twitter. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

package com.twitter.heron.dsl.impl.streamlets;

import java.util.Set;

import com.twitter.heron.api.topology.TopologyBuilder;
import com.twitter.heron.dsl.KeyValue;
import com.twitter.heron.dsl.Source;
import com.twitter.heron.dsl.impl.BaseKVStreamlet;
import com.twitter.heron.dsl.impl.sources.ComplexSource;

/**
 * SupplierStreamlet is a very quick and flexible way of creating a Streamlet
 * from a user supplied Supplier Function. The supplier function is the
 * source of all tuples for this Streamlet.
 */
public class SourceKVStreamlet<K, V> extends BaseKVStreamlet<K, V> {
  private Source<KeyValue<K, V>> generator;

  public SourceKVStreamlet(Source<KeyValue<K, V>> generator) {
    this.generator = generator;
    setNumPartitions(1);
  }

  @Override
  public boolean doBuild(TopologyBuilder bldr, Set<String> stageNames) {
    if (getName() == null) {
      setName(defaultNameCalculator("kvgenerator", stageNames));
    }
    if (stageNames.contains(getName())) {
      throw new RuntimeException("Duplicate Names");
    }
    stageNames.add(getName());
    bldr.setSpout(getName(), new ComplexSource<KeyValue<K, V>>(generator), getNumPartitions());
    return true;
  }
}
