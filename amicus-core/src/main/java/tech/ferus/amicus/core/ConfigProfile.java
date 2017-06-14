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

package tech.ferus.amicus.core;

import tech.ferus.amicus.core.storage.MySqlStorage;
import tech.ferus.amicus.core.storage.SqliteStorage;
import tech.ferus.amicus.core.storage.Storage;
import tech.ferus.amicus.core.storage.StorageType;

import ninja.leaping.configurate.ConfigurationNode;
import javax.annotation.Nonnull;

public class ConfigProfile {

    @Nonnull private final Storage storage;

    public ConfigProfile(@Nonnull final ConfigurationNode node,
                         @Nonnull final StorageType type) {
        if (type == StorageType.SQLITE) {
            this.storage = new SqliteStorage(AmicusCore.getInstance().getConfigDir()
                    .resolve(node.getNode("file-name").getString()));
        } else {
            this.storage = new MySqlStorage(
                    node.getNode("host").getString(),
                    node.getNode("port").getInt(),
                    node.getNode("database").getString(),
                    node.getNode("username").getString(),
                    node.getNode("password").getString()
            );
        }
    }

    @Nonnull
    public Storage getStorage() {
        return this.storage;
    }
}
