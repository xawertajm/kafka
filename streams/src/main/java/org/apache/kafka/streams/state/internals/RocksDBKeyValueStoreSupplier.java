/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kafka.streams.state.internals;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.processor.StateStoreSupplier;

/**
 * A {@link org.apache.kafka.streams.state.KeyValueStore} that stores all entries in a local RocksDB database.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 *
 * @see org.apache.kafka.streams.state.Stores#create(String)
 */
public class RocksDBKeyValueStoreSupplier<K, V> implements StateStoreSupplier {

    private final String name;
    private final Serde<K> keySerde;
    private final Serde<V> valueSerde;
    private final Time time;

    public RocksDBKeyValueStoreSupplier(String name, Serde<K> keySerde, Serde<V> valueSerde) {
        this(name, keySerde, valueSerde, null);
    }

    public RocksDBKeyValueStoreSupplier(String name, Serde<K> keySerde, Serde<V> valueSerde, Time time) {
        this.name = name;
        this.keySerde = keySerde;
        this.valueSerde = valueSerde;
        this.time = time;
    }

    public String name() {
        return name;
    }

    public StateStore get() {
        RocksDBStore<K, V> store = new RocksDBStore<>(name, keySerde, valueSerde).enableCaching();

        return new MeteredKeyValueStore<>(store.enableLogging(), "rocksdb-state", time);
    }
}
