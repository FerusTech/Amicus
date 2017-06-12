/*
 * This file is part of Amicus, licensed under the MIT License (MIT).
 *
 * Copyright (c) FerusTech LLC <https://ferus.tech>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tech.ferus.amicus.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;

public class ConfigKey<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigKey.class);

    @Nonnull private final Object[] key;
    @Nonnull private final T def;

    private ConfigKey(@Nonnull final Object[] key, @Nonnull final T def) {
        this.key = key;
        this.def = def;
    }

    @Nonnull
    public Object[] getKey() {
        return this.key;
    }

    @Nonnull
    public T get(@Nonnull final ConfigFile config) {
        try {
            return (T) config.getNode(this.key).getValue(this.def);
        } catch (final ClassCastException e) {
            LOGGER.error("Improper value type for \"{}\"!", e);
            return this.def;
        }
    }

    public static <T> ConfigKey<T> define(@Nonnull final T def, @Nonnull final Object key) {
        return new ConfigKey<>(new Object[]{key}, def);
    }

    public static <T> ConfigKey<T> define(@Nonnull final T def, @Nonnull final String... key) {
        return new ConfigKey<>(key, def);
    }
}
