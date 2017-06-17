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

import tech.ferus.amicus.core.config.ConfigKey;
import tech.ferus.amicus.core.storage.MySqlStorage;
import tech.ferus.amicus.core.storage.SqliteStorage;
import tech.ferus.amicus.core.storage.Storage;
import tech.ferus.amicus.core.storage.StorageKeys;
import tech.ferus.amicus.core.storage.StorageType;
import tech.ferus.amicus.core.util.FilesWrapper;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigProfile {

    public static final ConfigKey<String> PROFILE = ConfigKey.define("default", "profile");

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigProfile.class);

    @Nonnull private final Storage storage;

    public ConfigProfile(@Nonnull final Storage storage) {
        this.storage = storage;
    }

    @Nonnull
    public Storage getStorage() {
        return this.storage;
    }

    @Nullable
    public static ConfigProfile parse(@Nonnull final String key, @Nonnull final ConfigurationNode config) {
        switch (StorageType.of(StorageKeys.DATA_STORE.get(config))) {
            case MYSQL:
                return parseMysql(key, config);
            case SQLITE:
                return parseSqlite(key, config);
            default:
                LOGGER.error("Found invalid storage type for key: \"{}\"", key);
                return null;
        }
    }

    @Nullable
    private static ConfigProfile parseSqlite(@Nonnull final String key, @Nonnull final ConfigurationNode config) {
        final String fileName = StorageKeys.SQLITE_FILE.get(config);
        if (fileName.isEmpty()) {
            LOGGER.error("\"sqlite.file-name\" in \"{}\" cannot be empty!", key);
            return null;
        }

        final Path database = AmicusCore.getInstance().getConfigDir().resolve(fileName);
        if (!Files.exists(database)) {
            LOGGER.info("Database file doesn't exist and is being created: ", database.toString());
            if (!FilesWrapper.createFile(database)) {
                LOGGER.warn("Couldn't create database. Scrapping profile for key \"{}\".", key);
                return null;
            }
        }

        return new ConfigProfile(new SqliteStorage(database));
    }

    @Nullable
    private static ConfigProfile parseMysql(@Nonnull final String key, @Nonnull final ConfigurationNode config) {
        final String host = StorageKeys.MYSQL_HOST.get(config);
        final int port = StorageKeys.MYSQL_PORT.get(config);
        final String database = StorageKeys.MYSQL_DATABASE.get(config);
        final String username = StorageKeys.MYSQL_USERNAME.get(config);
        final String password = StorageKeys.MYSQL_PASSWORD.get(config);

        if (host.isEmpty()) {
            LOGGER.error("\"mysql.host\" in \"{}\" cannot be empty!", key);
            return null;
        }
        if (database.isEmpty()) {
            LOGGER.error("\"mysql.database\" in \"{}\" cannot be empty!", key);
            return null;
        }
        if (username.isEmpty()) {
            LOGGER.error("\"mysql.username\" in \"{}\" cannot be empty!", key);
            return null;
        }

        return new ConfigProfile(new MySqlStorage(host, port, database, username, password));
    }
}
