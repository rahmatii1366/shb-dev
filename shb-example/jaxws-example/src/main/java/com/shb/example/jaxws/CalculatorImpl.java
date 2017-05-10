package com.shb.example.jaxws;

import javax.ejb.Singleton;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Mohammad Rahmati, 5/9/2017 10:54 AM
 */
@Singleton
@WebService(name = "calculator",
        serviceName = "CalculatorService",
        targetNamespace = "v1.jaxws.example.shb.com")
public class CalculatorImpl {
    @WebMethod(operationName = "sumOf")
    public int sum(int a, int b) {
        return a + b;
    }
}
