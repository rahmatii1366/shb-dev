package com.shb.example.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Mohammad Rahmati, 5/9/2017 1:54 PM
 */
@WebService(name = "calculator",
        serviceName = "CalculatorService",
        targetNamespace = "v2.jaxws.example.shb.com",
        portName = "CalculatorService2")
public class CalculatorImpl2 {
    @WebMethod(operationName = "sumOf")
    public int sum(int a, int b) {
        return a + b * -1;
    }
}
