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

import tech.ferus.amicus.core.config.HoconConfigFile;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static tech.ferus.amicus.core.AmicusCore.DESCRIPTION;
import static tech.ferus.amicus.core.AmicusCore.PLUGIN_ID;
import static tech.ferus.amicus.core.AmicusCore.PLUGIN_NAME;
import static tech.ferus.amicus.core.AmicusCore.URL;
import static tech.ferus.amicus.core.AmicusCore.VERSION;

@Plugin(id = PLUGIN_ID, name = PLUGIN_NAME, version = VERSION,
        description = DESCRIPTION, url = URL, authors = {"FerusGrim"})
public class AmicusCore {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmicusCore.class);

    public static final String PLUGIN_ID = "@pluginId@";
    public static final String PLUGIN_NAME = "@pluginName@";
    public static final String DESCRIPTION = "@pluginDescription@";
    public static final String VERSION = "@version@";
    public static final String URL = "@url@";
    public static final String GIT_HASH = "@hashShort@";
    public static final String GIT_HASH_LONG = "@hashLong@";
    public static final String GIT_BRANCH = "@gitBranch@";

    @Nonnull private final Map<String, ConfigProfile> configProfiles;
    @Nonnull private final Path configDir;
    @Nonnull private final Game game;
    @Nullable private final HoconConfigFile configProfilesFile;

    @Inject
    public AmicusCore(@ConfigDir(sharedRoot = false) @Nonnull final Path configDir,
                      @Nonnull final Game game) {
        instance = this;
        this.configProfiles = Maps.newHashMap();
        this.configDir = configDir;
        this.game = game;

        this.configProfilesFile = this.loadConfig();
    }

    private HoconConfigFile loadConfig() {
        final Path configPath = this.configDir.resolve("profiles.conf");
        HoconConfigFile config = null;
        try {
            config = HoconConfigFile.load(configPath, "/profiles.conf");
        } catch (final IOException e) {
            LOGGER.error("Encountered exception while creating configuration file: {}", configPath.toString(), e);
        }

        return config;
    }

    @Listener(order = Order.EARLY)
    public void onGamePreInitialization(@Nonnull final GamePreInitializationEvent event) {
        if (this.configProfilesFile == null) {
            LOGGER.error("Failed to properly load configuration profiles.", new IllegalStateException());
            return;
        }

        this.configProfiles.clear();
        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : this.configProfilesFile.getNode().getChildrenMap().entrySet()) {
            if (!entry.getValue().hasMapChildren()) {
                continue;
            }

            final String key = "" + entry.getKey();
            final ConfigurationNode config = entry.getValue();

            final ConfigProfile profile = ConfigProfile.parse(key, config);
            if (profile == null) {
                continue;
            }

            LOGGER.info("Found \"{}\" profile, using \"{}\".", key,
                    profile.getStorage().getStorageType().toString());

            this.configProfiles.put(key, profile);
        }

        if (!this.hasConfigProfile("default")) {
            LOGGER.warn("Couldn't find a \"default\" configuration profile.");
        }
    }

    @Nonnull
    public Map<String, ConfigProfile> getConfigProfiles() {
        return this.configProfiles;
    }

    @Nullable
    public ConfigProfile getConfigProfile(@Nonnull final String key) {
        if (this.hasConfigProfile(key)) {
            return this.configProfiles.get(key);
        } else {
            if (!this.hasConfigProfile("default")) {
                LOGGER.error("Default profile doesn't exist and \"{}\" can't be found!", key, new IllegalStateException());
                return null;
            }

            return this.configProfiles.get("default");
        }
    }

    public boolean hasConfigProfile(@Nonnull final String profile) {
        return this.configProfiles.keySet().stream()
                .anyMatch(key -> key.equalsIgnoreCase(profile));
    }

    @Nonnull
    public Path getConfigDir() {
        return this.configDir;
    }

    @Nonnull
    public Game getGame() {
        return this.game;
    }

    @Nonnull
    public HoconConfigFile getConfigProfilesFile() {
        return this.configProfilesFile;
    }

    private static AmicusCore instance;
    public static AmicusCore getInstance() {
        return instance;
    }
}
