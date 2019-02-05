/**
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
package org.influxdata.kotlin.client

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import org.influxdata.client.LogLevel
import org.influxdata.client.exceptions.InfluxException
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith


/**
 * @author Jakub Bednar (bednar@github) (30/10/2018 09:19)
 */
@RunWith(JUnitPlatform::class)
internal class ITFluxClientKotlin : AbstractITFluxClientKotlin() {

    @Test
    fun logLevel() {
        assert(fluxClient.getLogLevel()).isEqualTo(LogLevel.NONE)

        // set HEADERS
        fluxClient.setLogLevel(LogLevel.HEADERS)

        assert(fluxClient.getLogLevel()).isEqualTo(LogLevel.HEADERS)
    }

    @Test
    fun pingHealthy() {

        assert(fluxClient.ping()).isTrue()
    }

    @Test
    fun pingNotRunningServer() {

        val clientNotRunning = FluxClientKotlinFactory.create("http://localhost:8099")

        assert(clientNotRunning.ping()).isFalse()

        clientNotRunning.close()
    }

    @Test
    fun version() {

        assert(fluxClient.version()).isNotEmpty()
    }

    @Test
    fun versionNotRunningServer() {

        val clientNotRunning = FluxClientKotlinFactory.create("http://localhost:8099")

        assert { clientNotRunning.version() }
                .thrownError { isInstanceOf(InfluxException::class.java) }

        clientNotRunning.close()
    }
}