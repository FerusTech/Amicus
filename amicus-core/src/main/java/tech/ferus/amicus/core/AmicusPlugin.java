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

import tech.ferus.amicus.core.config.ConfigProfile;
import tech.ferus.amicus.core.config.HoconConfigFile;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public abstract class AmicusPlugin {

    @Nonnull private final Game game;
    @Nonnull private final HoconConfigFile config;
    @Nonnull private Optional<ConfigProfile> profile = Optional.empty();

    public AmicusPlugin(@Nonnull final Game game, @Nonnull final String configName) {
        this.game = game;
        this.config = loadConfig(configName);
    }

    @Nonnull
    public Game getGame() {
        return this.game;
    }

    @Nonnull
    public HoconConfigFile getConfig() {
        return this.config;
    }

    @Nonnull
    public Optional<ConfigProfile> getProfile() {
        return this.profile;
    }

    @Nonnull
    private static HoconConfigFile loadConfig(@Nonnull final String configName) {
        final Path configPath = AmicusCore.getInstance().getConfigDir().resolve(configName);
        HoconConfigFile configFile = null;
        try {
            configFile = HoconConfigFile.load(configPath, "/" + configName);
        } catch (final IOException ignored) {}

        if (configFile != null) {
            return configFile;
        }

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configPath).build();
        return new HoconConfigFile(configPath, loader, loader.createEmptyNode());
    }

    @Listener
    public void onGamePreInitialization(@Nonnull final GamePreInitializationEvent event) {
        final String configName = this.config.getFile().getFileName().toString();
        final ConfigProfile profile = AmicusCore.getInstance().getConfigProfile(
                ConfigProfile.PROFILE.get(this.config)
        );

        if (profile == null) {
            this.game.getEventManager().unregisterPluginListeners(this);
        }

        this.profile = Optional.ofNullable(profile);
        this.displayProfileStatus();
    }

    protected abstract void displayProfileStatus();
}
