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
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigFile extends ConfigFile<ConfigurationNode> {

    public JsonConfigFile(@Nonnull final Path file,
                          @Nonnull final ConfigurationLoader<ConfigurationNode> loader,
                          @Nonnull final ConfigurationNode root) {
        super(file, loader, root);
    }

    public static JsonConfigFile load(final Path file) throws IOException {
        return load(file, null, false, false);
    }

    public static JsonConfigFile load(@Nonnull final Path file,
                                      @Nonnull final String resource) throws IOException {
        return load(file, resource, false, true);
    }

    public static JsonConfigFile load(@Nonnull final Path file,
                                      @Nonnull final String resource,
                                      final boolean overwrite) throws IOException {
        return load(file, resource, overwrite, true);
    }

    public static JsonConfigFile load(@Nonnull final Path file,
                                      @Nullable final String resource,
                                      final boolean overwrite,
                                      final boolean merge) throws IOException {
        if (overwrite) {
            Files.deleteIfExists(file);
        }

        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        }

        final JSONConfigurationLoader fileLoader = JSONConfigurationLoader.builder().setPath(file).build();
        ConfigurationNode root = fileLoader.load();

        if (merge) {
            root.mergeValuesFrom(JSONConfigurationLoader.builder().setURL(JsonConfigFile.class.getResource(resource)).build().load());
            fileLoader.save(root);
        }

        return new JsonConfigFile(file, fileLoader, root);
    }
}
