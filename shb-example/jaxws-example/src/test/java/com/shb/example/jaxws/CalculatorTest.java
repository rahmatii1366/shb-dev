package com.shb.example.jaxws;

import com.shb.example.jaxws.v1.Calculator;
import com.shb.example.jaxws.v1.CalculatorService;
import com.shb.example.jaxws.v2.CalculatorService2;
import org.junit.*;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mohammad Rahmati, 5/9/2017 11:39 AM
 */
public class CalculatorTest {
    static Endpoint publish;

    @BeforeClass
    public static void beforeClass() {
        publish = Endpoint.publish(
                "http://localhost:9999/ws/calculator",
                new CalculatorImpl());
    }

    @AfterClass
    public static void afterClass() {
        publish.stop();
    }

    @Test
    public void testSumPublish()
            throws MalformedURLException {
        URL url = new URL(
                "http://localhost:9999/ws/calculator?wsdl");

        Service calculatorService = CalculatorService
                .create(url, new QName(
                        "v1.jaxws.example.shb.com",
                        "CalculatorService"));
        Calculator calculator = calculatorService
                .getPort(Calculator.class);
        Assert.assertEquals(3, calculator.sumOf(1, 2));
    }

    @Ignore
    @Test
    public void testCalculatorService()
            throws MalformedURLException {
        URL url = new URL(
                "http://localhost:8080/jaxws-example-1.0-SNAPSHOT/CalculatorService/calculator?wsdl");

        Service calculatorService = CalculatorService
                .create(url, new QName(
                        "v1.jaxws.example.shb.com",
                        "CalculatorService"));
        Calculator calculator = calculatorService
                .getPort(Calculator.class);
        Assert.assertEquals(3, calculator.sumOf(1, 2));
    }

    @Ignore
    @Test
    public void testCalculatorService2()
            throws MalformedURLException {
        URL url = new URL(
                "http://localhost:8080/jaxws-example-1.0-SNAPSHOT/CalculatorService2/calculator?wsdl");

        Service calculatorService = CalculatorService2
                .create(url, new QName(
                        "v2.jaxws.example.shb.com",
                        "CalculatorService2"));
        com.shb.example.jaxws.v2.Calculator calculator = calculatorService
                .getPort(com.shb.example.jaxws.v2.Calculator.class);
        Assert.assertEquals(-1, calculator.sumOf(1, 2));
    }
}
