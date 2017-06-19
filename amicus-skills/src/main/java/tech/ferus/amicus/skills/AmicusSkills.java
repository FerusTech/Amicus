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

package tech.ferus.amicus.skills;

import tech.ferus.amicus.core.AmicusCore;
import tech.ferus.amicus.core.AmicusPlugin;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import javax.annotation.Nonnull;

import static tech.ferus.amicus.skills.AmicusSkills.DESCRIPTION;
import static tech.ferus.amicus.skills.AmicusSkills.PLUGIN_ID;
import static tech.ferus.amicus.skills.AmicusSkills.PLUGIN_NAME;
import static tech.ferus.amicus.skills.AmicusSkills.URL;
import static tech.ferus.amicus.skills.AmicusSkills.VERSION;

@Plugin(id = PLUGIN_ID, name = PLUGIN_NAME, version = VERSION,
        description = DESCRIPTION, url = URL, authors = {"FerusGrim"},
        dependencies = @Dependency(id = AmicusCore.PLUGIN_ID))
public class AmicusSkills extends AmicusPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmicusSkills.class);

    public static final String PLUGIN_ID = "@pluginId@";
    public static final String PLUGIN_NAME = "@pluginName@";
    public static final String DESCRIPTION = "@pluginDescription@";
    public static final String VERSION = "@version@";
    public static final String URL = "@url@";
    public static final String GIT_HASH = "@hashShort@";
    public static final String GIT_HASH_LONG = "@hashLong@";
    public static final String GIT_BRANCH = "@gitBranch@";

    @Inject
    public AmicusSkills(@Nonnull final Game game) {
        super(game, "skills.conf");
    }

    @Override
    protected void displayProfileStatus() {
        if (this.getProfile().isPresent()) {
            LOGGER.info("Successfully loaded profile \"{}\"!", this.getProfile().get().getName());
        } else {
            LOGGER.error("Failed to load a profile from \"{}\"!",
                    this.getConfig().getFile().getFileName().toString(),
                    new IllegalStateException());
        }
    }
}
