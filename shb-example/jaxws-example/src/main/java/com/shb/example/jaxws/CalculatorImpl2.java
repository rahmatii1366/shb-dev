package com.shb.example.jaxws;

import javax.ejb.Singleton;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Mohammad Rahmati, 5/9/2017 1:54 PM
 */
@Singleton
@WebService(name = "calculator",
        serviceName = "CalculatorService2",
        targetNamespace = "v2.jaxws.example.shb.com")
public class CalculatorImpl2 {
    @WebMethod(operationName = "sumOf")
    public int sum(int a, int b) {
        return a + b * -1;
    }
}
