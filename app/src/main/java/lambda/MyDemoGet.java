package lambda;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

/**
 * Created by cenxui on 11/20/16.
 */

public interface MyDemoGet {
    /**
     * Invoke lambda function "MyDemoGet". The function name is the method name
     */
    @LambdaFunction(functionName = "MyDemoGet")
    String getCustomer(Request request);

}
