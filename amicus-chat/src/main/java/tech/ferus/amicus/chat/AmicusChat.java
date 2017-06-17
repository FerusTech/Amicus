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

package tech.ferus.amicus.chat;

import tech.ferus.amicus.core.AmicusCore;
import tech.ferus.amicus.core.config.ConfigProfile;
import tech.ferus.amicus.core.config.HoconConfigFile;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;

import static tech.ferus.amicus.chat.AmicusChat.DESCRIPTION;
import static tech.ferus.amicus.chat.AmicusChat.PLUGIN_ID;
import static tech.ferus.amicus.chat.AmicusChat.PLUGIN_NAME;
import static tech.ferus.amicus.chat.AmicusChat.URL;
import static tech.ferus.amicus.chat.AmicusChat.VERSION;

@Plugin(id = PLUGIN_ID, name = PLUGIN_NAME, version = VERSION,
        description = DESCRIPTION, url = URL, authors = {"FerusGrim"},
        dependencies = @Dependency(id = AmicusCore.PLUGIN_ID))
public class AmicusChat {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmicusChat.class);

    public static final String PLUGIN_ID = "@pluginId@";
    public static final String PLUGIN_NAME = "@pluginName@";
    public static final String DESCRIPTION = "@pluginDescription@";
    public static final String VERSION = "@version@";
    public static final String URL = "@url@";
    public static final String GIT_HASH = "@hashShort@";
    public static final String GIT_HASH_LONG = "@hashLong@";
    public static final String GIT_BRANCH = "@gitBranch@";

    @Nonnull private final Path configDir;
    @Nonnull private final Game game;
    private final HoconConfigFile config;

    @Inject
    public AmicusChat(@ConfigDir(sharedRoot = false) @Nonnull final Path configDir, @Nonnull final Game game) {
        this.configDir = configDir;
        this.game = game;

        final Path configPath = configDir.resolve("amicus-chat.conf");
        HoconConfigFile config = null;
        try {
            config = HoconConfigFile.load(configPath, "/amicus-chat.conf");
        } catch (final IOException e) {
            LOGGER.error("Encountered exception while creating configuration file: {}", configPath.toString(), e);
        }

        this.config = config;
    }

    @Listener
    public void onGamePreInitialization(@Nonnull final GamePreInitializationEvent event) {
        final ConfigProfile profile = AmicusCore.getInstance().getConfigProfile(
                ConfigProfile.PROFILE.get(this.config)
        );

        if (profile == null) {
            LOGGER.error("AmicusChat profile failed to load!", new IllegalStateException());
            this.game.getEventManager().unregisterPluginListeners(this);
        } else {
            LOGGER.info("Successfully loaded profile \"{}\".", profile.getName());
        }
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
    public HoconConfigFile getConfig() {
        return this.config;
    }
}
