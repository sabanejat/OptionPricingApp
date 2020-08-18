package sabanejat.com;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class First_Frag extends Fragment {

    double sharePrice;
    double strikePrice;
    double riskFree;
    double vol;
    double time;


    EditText optionPrice;
    EditText optionStrikePrice;
    EditText riskFreeInterestRate;
    EditText volatility;
    EditText optionDate;


    Button calculate;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_, container, false);


        optionPrice = view.findViewById(R.id.optionPrice);
        optionStrikePrice = view.findViewById(R.id.optionStrikePrice);
        riskFreeInterestRate = view.findViewById(R.id.riskFreeInterestRate);
        volatility = view.findViewById(R.id.volatility);
        optionDate = view.findViewById(R.id.optionDate);


        calculate = view.findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePrice = Double.parseDouble(optionPrice.getText().toString());
                strikePrice = Double.parseDouble(optionStrikePrice.getText().toString());
                riskFree = Double.parseDouble(riskFreeInterestRate.getText().toString());
                vol = Double.parseDouble(volatility.getText().toString());
                time = Double.parseDouble(optionDate.getText().toString());



                showToast(String.valueOf(calculate(true,sharePrice, strikePrice, riskFree/100, vol/100, time)));
            }
        });

        return view;
    }


    public void showToast(String calculatedNum){
        Toast.makeText(getActivity(),calculatedNum,Toast.LENGTH_LONG).show();

    }


    private static final double P = 0.2316419;
    private static final double B1 = 0.319381530;
    private static final double B2 = -0.356563782;
    private static final double B3 = 1.781477937;
    private static final double B4 = -1.821255978;
    private static final double B5 = 1.330274429;

    public static double calculate(boolean callOption,
                                   double s, double k, double r, double t, double v) {
        if (callOption) {
            double cd1 = cumulativeDistribution(d1(s, k, r, t, v));
            double cd2 = cumulativeDistribution(d2(s, k, r, t, v));
            return s * cd1 - k * exp(-r*t) *cd2;

        } else {
            double cd1 = cumulativeDistribution(-d1(s, k, r, t, v));
            double cd2 = cumulativeDistribution(-d2(s, k, r, t, v));
            return k*exp(-r*t) * cd2 - s * cd1;

        }
    }

    private static double d1(double s, double k, double r, double t, double v) {
        double top = log(s / k) + (r + pow(v, 2) / 2) * t;
        double bottom = v * sqrt(t);
        return top / bottom;
    }

    private static double d2(double s, double k, double r, double t, double v) {
        return  d1(s,k,r,t,v) - v * sqrt(t);

    }

    public static double cumulativeDistribution(double x) {
        double t = 1 / (1 + P * abs(x));
        double t1 = B1 * pow(t, 1);
        double t2 = B2 * pow(t, 2);
        double t3 = B3 * pow(t, 3);
        double t4 = B4 * pow(t, 4);
        double t5 = B5 * pow(t, 5);
        double b = t1 + t2 + t3 + t4 + t5;
        double cd = 1 - standardNormalDistribution(x) * b;
        return x<0 ? 1-cd :cd;

    }

    public static double standardNormalDistribution(double x) {
        double top = exp(-0.5 * pow(x, 2));
        double bottom = sqrt(2 * PI);
        return top / bottom;
    }

}

