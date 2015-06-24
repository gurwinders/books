//package com.rajpal.books;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.RemoteException;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class ExampleActivity extends Activity {
//    private Button btnStart, btnStop, btnUpby1, btnUpby10;
//    private TextView textValue1, textValue2;
//
//    private ServiceManager service;
//    private ServiceManager service2;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_fragment);
//        btnStart = (Button) findViewById(R.id.btnStart);
//        btnStop = (Button) findViewById(R.id.btnStop);
//        textValue1 = (TextView) findViewById(R.id.textValue1);
//        textValue2 = (TextView) findViewById(R.id.textValue2);
//        btnUpby1 = (Button) findViewById(R.id.btnUpby1);
//        btnUpby10 = (Button) findViewById(R.id.btnUpby10);
//
//        btnStart.setOnClickListener(btnStartListener);
//        btnStop.setOnClickListener(btnStopListener);
//        btnUpby1.setOnClickListener(btnUpby1Listener);
//        btnUpby10.setOnClickListener(btnUpby10Listener);
//
//        restoreMe(savedInstanceState);
//
//        this.service = new ServiceManager(this, SomeService1.class, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case SomeService1.MSG_COUNTER:
//                        // Receive counter value from service 1 and send it to service 2
////                        textValue1.setText("Counter @ Service1: " + msg.arg1);
////                        try {
////                            service2.send(Message.obtain(null, SomeService2.MSG_VALUE, msg.arg1, 0));
////                        } catch (RemoteException e) {
////                        }
//                        break;
//
//                    default:
//                        super.handleMessage(msg);
//                }
//            }
//        });
//
////        this.service2 = new ServiceManager(this, SomeService2.class, new Handler() {
////        	@Override
////        	public void handleMessage(Message msg) {
////	            switch (msg.what) {
////		            case SomeService2.MSG_SQRT:
////		                double sqrt = msg.getData().getDouble("sqrt");
////		                textValue2.setText("Sqrt(Counter) @ Service2: " + String.format("%.2f", sqrt));
////		                break;
////
////		            default:
////		                super.handleMessage(msg);
////	            }
////        	}
////        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            service.unbind();
//            service2.unbind();
//        } catch (Throwable t) {
//            Log.e("MainActivity", "Failed to unbind from the service", t);
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("textValue1", textValue1.getText().toString());
//        outState.putString("textValue2", textValue2.getText().toString());
//    }
//
//    private void restoreMe(Bundle state) {
//        if (state != null) {
//            textValue1.setText(state.getString("textValue1"));
//            textValue2.setText(state.getString("textValue2"));
//        }
//    }
//
//    private OnClickListener btnStartListener = new OnClickListener() {
//        public void onClick(View v) {
//            service.start();
//            service2.start();
//        }
//    };
//    private OnClickListener btnStopListener = new OnClickListener() {
//        public void onClick(View v) {
//            service.stop();
//            service2.stop();
//        }
//    };
//    private OnClickListener btnUpby1Listener = new OnClickListener() {
//        public void onClick(View v) {
//            sendMessageToService(1);
//        }
//    };
//    private OnClickListener btnUpby10Listener = new OnClickListener() {
//        public void onClick(View v) {
//            sendMessageToService(10);
//        }
//    };
//
//    private void sendMessageToService(int intvaluetosend) {
//        try {
//            service.send(Message.obtain(null, SomeService1.MSG_INCREMENT, intvaluetosend, 0));
//
//        } catch (RemoteException e) {
//        }
//    }
//}