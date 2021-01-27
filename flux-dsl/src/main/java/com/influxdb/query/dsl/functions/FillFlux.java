/*
 * The MIT License
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
package com.influxdb.query.dsl.functions;

import javax.annotation.Nonnull;

import com.influxdb.Arguments;
import com.influxdb.query.dsl.Flux;

import java.time.temporal.ChronoUnit;

/**
 * Counts the number of results. <a href="http://bit.ly/flux-spec#fill">See SPEC</a>.
 *
 * <h3>Options</h3>
 * <ul>
 * <li><b>column</b> - The column to fill. Default to <i>_value</i>.</li>
 * <li><b>value</b> - The constant value to use in place of nulls. The type must match the type of the valueColumn.</li>
 * <li><b>usePrevious</b> - If set, then assign the value set in the previous non-null row. Cannot be used with <i>_value</i>.</li>
 * </ul>
 *
 * <h3>Example</h3>
 * <pre>
 * Flux flux = Flux
 *      .from("telegraf")
 *      .aggregateWindow(5L, ChronoUnit.MINUTES, "mean")
 *      .fill()
 *      .withUsePrevious(true);
 * </pre>
 *
 * @author Szymon Kliniewski (NoneTirex@github) (27/01/2020 18:55)
 */
public final class FillFlux extends AbstractParametrizedFlux {
    public FillFlux(@Nonnull final Flux source) {
        super(source);
    }

    @Nonnull
    @Override
    protected String operatorName() {
        return "fill";
    }

    /**
     * @param column The column to fill.
     * @return this
     */
    @Nonnull
    public FillFlux withColumn(@Nonnull final String column) {
        Arguments.checkNonEmpty(column, "Column");
        this.withPropertyValueEscaped("column", column);
        return this;
    }

    /**
     * @param value The constant value to use in place of nulls.
     * @return this
     */
    @Nonnull
    public FillFunction withValue(Object value) {
        this.withPropertyValue("value", value);
        return this;
    }

    /**
     * @param value The constant value to use in place of nulls.
     * @return this
     */
    @Nonnull
    public FillFunction withValue(@Nonnull final Long every, @Nonnull final ChronoUnit everyUnit) {
        Arguments.checkNotNull(every, "Every is required");
        Arguments.checkNotNull(everyUnit, "Every ChronoUnit is required");
        this.withPropertyValue("every", every, everyUnit);
        return this;
    }

    /**
     * @param usePrevious If set, then assign the value set in the previous non-null row.
     * @return this
     */
    @Nonnull
    public FillFunction withUsePrevious(boolean usePrevious) {
        this.withPropertyValue("usePrevious", usePrevious);
        return this;
    }
}
