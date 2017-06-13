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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class SqlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlUtil.class);

    public static void execute(@Nonnull final Storage database,
                               @Nonnull final String statement) {
        execute(database, statement, s -> {});
    }

    public static void execute(@Nonnull final Storage database,
                               @Nonnull final String statement,
                               @Nonnull final Preparer preparer) {
        Connection c = null;
        PreparedStatement s = null;

        LOGGER.debug("Attempting to execute statement: {}", statement);
        try {
            c = database.getConnection();
            s = c.prepareStatement(statement);
            preparer.prepare(s);
            s.execute();
            LOGGER.debug("Finished executing statement: {}", statement);
        } catch (final SQLException e) {
            LOGGER.error("Failed to execute statement: {}", statement, e);
        } finally {
            close(statement, c, s, null);
        }
    }

    public static void query(@Nonnull final Storage database,
                             @Nonnull final String statement,
                             @Nonnull final HandleResults handle) {
        query(database, statement, s -> {}, handle);
    }

    public static void query(@Nonnull final Storage database,
                             @Nonnull final String statement,
                             @Nonnull final Preparer preparer,
                             @Nonnull final HandleResults handle) {
        Connection c = null;
        PreparedStatement s = null;
        ResultSet r = null;

        LOGGER.debug("Attempting to query: {}", statement);
        try {
            c = database.getConnection();
            s = c.prepareStatement(statement);
            preparer.prepare(s);
            r = s.executeQuery();
            handle.execute(r);
            LOGGER.debug("Finished querying: {}", statement);
        } catch (final SQLException e) {
            LOGGER.error("Failed to execute query: {}", statement, e);
        } finally {
            close(statement, c, s, r);
        }
    }

    public static <T> Optional<T> returnQuery(@Nonnull final Storage database,
                                              @Nonnull final String statement,
                                              @Nonnull final ReturnResults<T> handle) {
        return returnQuery(database, statement, s -> {}, handle);
    }

    public static <T> Optional<T> returnQuery(@Nonnull final Storage database,
                                              @Nonnull final String statement,
                                              @Nonnull final Preparer preparer,
                                              @Nonnull final ReturnResults<T> handle) {
        Connection c = null;
        PreparedStatement s = null;
        ResultSet r = null;

        LOGGER.debug("Attempting to query: {}", statement);
        try {
            c = database.getConnection();
            s = c.prepareStatement(statement);
            preparer.prepare(s);
            r = s.executeQuery();
            final Optional<T> results = Optional.ofNullable(handle.execute(r));
            LOGGER.debug("Finished querying: {}", statement);
            return results;
        } catch (final SQLException e) {
            LOGGER.error("Failed to execute query: {}", statement, e);
            return Optional.empty();
        } finally {
            close(statement, c, s, r);
        }
    }

    private static void close(@Nonnull final String statement,
                              @Nullable final Connection c,
                              @Nullable final Statement s,
                              @Nullable final ResultSet r) {
        if (r != null) try {
            r.close();
        } catch (final SQLException e) {
            LOGGER.error("Failed to close ResultSet for statement: {}", statement);
        }

        if (s != null) try {
            s.close();
        } catch (final SQLException e) {
            LOGGER.error("Failed to close Statement for statement: {}", statement);
        }

        if (c != null) try {
            c.close();
        } catch (final SQLException e) {
            LOGGER.error("Failed to close Connection for statement: {}", statement);
        }

        LOGGER.debug("Finished closing objects related to statement: {}", statement);
    }
}
