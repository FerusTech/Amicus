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

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigFile<T extends ConfigurationNode> {

    @Nonnull private final Path file;
    @Nonnull private final ConfigurationLoader<T> loader;
    @Nonnull private final T root;

    public ConfigFile(@Nonnull final Path file,
                      @Nonnull final ConfigurationLoader<T> loader,
                      @Nonnull final T root) {
        this.file = file;
        this.loader = loader;
        this.root = root;
    }

    @Nonnull
    public Path getFile() {
        return this.file;
    }

    @Nonnull
    public Path getDirectory() {
        return this.file.getParent();
    }

    @Nonnull
    public ConfigurationLoader<T> getLoader() {
        return this.loader;
    }

    @Nonnull
    public T getNode() {
        return this.root;
    }

    @Nonnull
    public T getNode(final Object path) {
        return (T) this.root.getNode(path);
    }

    @Nonnull
    public T getNode(final Object... path) {
        return (T) this.root.getNode((Object[]) path);
    }

    public void save() throws IOException {
        this.loader.save(this.root);
    }
}
