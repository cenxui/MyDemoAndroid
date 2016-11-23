package lambda;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

/**
 * Created by cenxui on 11/20/16.
 */

public interface MyDemoPost {
    /**
     * Invoke lambda function "MyDemoPost". The function name is the method name
     */
    @LambdaFunction(functionName = "MyDemoPost")
    String putCustomer(Customer customer);
}
