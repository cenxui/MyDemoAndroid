package lambda;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cenxui on 11/23/16.
 */

public final class Customers {
    private Customers () {}

    public static Customer newCustomer(String json) {
        Customer customer = new Customer();
        try {
            JSONObject object = new JSONObject(json);
            customer.setId(object.optInt("id"));
            customer.setBirthday(object.optString("birthday"));
            customer.setCost(object.optInt("cost"));
            customer.setCount(object.optInt("count"));
            customer.setHeight(object.optInt("height"));
            customer.setWeight(object.optInt("weight"));
            customer.setName(object.optString("name"));
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public static Customer newCustomer() {
        return new Customer();
    }
}
