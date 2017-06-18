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

package tech.ferus.amicus.core.storage;

import tech.ferus.amicus.core.config.ConfigKey;

public class StorageKeys {

    public static final ConfigKey<String> DATA_STORE = ConfigKey.define("sqlite", "data-store");

    // SQLite settings
    public static final ConfigKey<String> SQLITE_FILE = ConfigKey.define("default.db", "sqlite", "file-name");

    // MySQL settings
    public static final ConfigKey<String> MYSQL_HOST = ConfigKey.define("localhost", "mysql", "host");
    public static final ConfigKey<Integer> MYSQL_PORT = ConfigKey.define(3306, "mysql", "port");
    public static final ConfigKey<String> MYSQL_DATABASE = ConfigKey.define("amicus", "mysql", "database");
    public static final ConfigKey<String> MYSQL_USERNAME = ConfigKey.define("root", "mysql", "username");
    public static final ConfigKey<String> MYSQL_PASSWORD = ConfigKey.define("", "mysql", "password");
}
