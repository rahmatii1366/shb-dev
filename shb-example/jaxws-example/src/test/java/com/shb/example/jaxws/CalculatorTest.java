package com.shb.example.jaxws;

import com.shb.example.jaxws.v1.Calculator;
import com.shb.example.jaxws.v1.CalculatorService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
//        System.out.println(calculator.sumOf(1, 2));
        Assert.assertEquals(3, calculator.sumOf(1, 2));
    }

//    @Test
    public void testSum()
            throws MalformedURLException {
        URL url = new URL(
                "http://localhost:8080/dml-ejb-1.0-SNAPSHOT/CalculatorService/Calculator?wsdl");

        Service calculatorService = CalculatorService
                .create(url, new QName(
                        "v1.jaxws.example.shb.com",
                        "CalculatorService"));
        Calculator calculator = calculatorService
                .getPort(Calculator.class);
//        System.out.println(calculator.sumOf(1, 2));
        Assert.assertEquals(5, calculator.sumOf(3, 2));
    }
}
