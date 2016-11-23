package com.example.cenxui.mydemo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import lambda.*;

/**
 * @author cenxui
 * 2016/11/23
 */

public class MainActivity extends AppCompatActivity {
    private CognitoCachingCredentialsProvider mCredentialsProvider;
    private LambdaInvokerFactory mLambda;
    private MyDemoGet mMyDemoGet;
    private MyDemoPost mMyDemoPost;

    private Button mButtonLast;
    private Button mButtonNext;
    private Button mButtonAdd;

    private TextView mTextView;

    private EditText mEditId;
    private EditText mEditName;
    private EditText mEditBirthday;
    private EditText mEditCost;
    private EditText mEditCount;
    private EditText mEditHeight;
    private EditText mEditWeight;

    private int id = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialView();

        initialCongnito();

        initialLambdaFactory();

        initialOnclick();

    }

    private void initialOnclick() {
        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.id--;
                new Thread(new InvokeGetTask(MainActivity.this.id)).start();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.id++;
                new Thread(new InvokeGetTask(MainActivity.this.id)).start();
            }
        });

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new InvokePostTask()).start();
            }
        });
    }

    private void initialView() {
        mButtonLast = (Button)this.findViewById(R.id.buttonLast);
        mButtonNext = (Button)this.findViewById(R.id.buttonNext);
        mButtonAdd = (Button) this.findViewById(R.id.buttonAdd);

        mTextView = (TextView)this.findViewById(R.id.textView);

        mEditId = (EditText)this.findViewById(R.id.editId);
        mEditName = (EditText)this.findViewById(R.id.editName);
        mEditBirthday = (EditText)this.findViewById(R.id.editBirthday);
        mEditCost = (EditText)this.findViewById(R.id.editCost);
        mEditCount = (EditText)this.findViewById(R.id.editCount);
        mEditHeight = (EditText)this.findViewById(R.id.editHeight);
        mEditWeight = (EditText)this.findViewById(R.id.editWeight);

    }

    private void initialLambdaFactory() {
        // Create a LambdaInvokerFactory, to be used to instantiate the Lambda proxy
        mLambda = new LambdaInvokerFactory(
                this,
                Regions.AP_NORTHEAST_1,
                mCredentialsProvider);
        lambdaFunction();

    }

    private void lambdaFunction() {
        // Create the Lambda proxy object with default Json data binder.
        // You can provide your own data binder by implementing
        // LambdaDataBinder
        mMyDemoGet = mLambda.build(MyDemoGet.class);
        mMyDemoPost = mLambda.build(MyDemoPost.class);
    }

    private void initialCongnito() {
        // Create an instance of CognitoCachingCredentialsProvider
        mCredentialsProvider= new CognitoCachingCredentialsProvider(
                this,
                "ap-northeast-1:f10e4816-3470-43f2-8bbe-6e261577fc62", // Identity Pool ID
                Regions.AP_NORTHEAST_1 // Region tokyo
        );
    }

    private class InvokeGetTask implements  Runnable {
        private int id;
        public InvokeGetTask(int queryID) {
            id = queryID;
        }
        @Override
        public void run() {
            Request request = new Request();
            request.setId(id);
            final String result = mMyDemoGet.getCustomer(request);
            Customer customer = Customers.newCustomer(result);
            final StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.id)).append(customer.getId()).append("\n")
                    .append(getString(R.string.name)).append(customer.getName()).append("\n")
                    .append(getString(R.string.birthday)).append(customer.getBirthday()).append("\n")
                    .append(getString(R.string.cost)).append(customer.getCost()).append("\n")
                    .append(getString(R.string.count)).append(customer.getCount()).append("\n")
                    .append(getString(R.string.height)).append(customer.getHeight()).append("\n")
                    .append(getString(R.string.weight)).append(customer.getWeight());
            if (TextUtils.isEmpty(result)) return;

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(builder.toString());

                }
            });
        }
    }

    private class InvokePostTask implements  Runnable {
        @Override
        public void run() {
            String id = mEditId.getText().toString();
            if (TextUtils.isEmpty(id)) return;
            String name = mEditName.getText().toString();
            if (TextUtils.isEmpty(name)) return;
            String birthday = mEditBirthday.getText().toString();
            if (TextUtils.isEmpty(birthday)) return;
            String count = mEditCount.getText().toString();
            if (TextUtils.isEmpty(count)) return;
            String cost = mEditCost.getText().toString();
            if (TextUtils.isEmpty(cost)) return;
            String height = mEditHeight.getText().toString();
            if (TextUtils.isEmpty(height)) return;
            String weight = mEditWeight.getText().toString();
            if (TextUtils.isEmpty(weight)) return;

            Customer customer = Customers.newCustomer();
            customer.setId(Integer.valueOf(id));
            customer.setName(name);
            customer.setBirthday(birthday);
            customer.setCost(Integer.valueOf(cost));
            customer.setCount(Integer.valueOf(count));
            customer.setHeight(Integer.valueOf(height));
            customer.setWeight(Integer.valueOf(weight));

            final String result = mMyDemoPost.putCustomer(customer);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
